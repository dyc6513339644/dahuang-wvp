package com.ruoyi.onvif.task;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ONVIF 设备在线探测（对齐 ISUP 的 Redis 在离线机制）。
 * <p>
 * 原实现每 30 秒对「心跳超 30s」的设备做一次轮询，失败次数只存在内存 Map：
 * 服务重启计数清零、多实例重复探测、在线设备也会被高频探测。现改为：
 * <ol>
 *   <li>Redis ZSET 当「到期探测队列」（{@link OnvifProbeStateCache#QUEUE_KEY}），
 *       member=deviceId、score=下次探测时间，等价于每台设备一个延时任务；</li>
 *   <li>探测成功 → 按设备心跳间隔（{@code heart_beat_interval}，默认 60s）重排；</li>
 *   <li>探测失败 → 按失败重试间隔（默认 15s）重排，连续失败达 {@code heart_beat_count}
 *       （默认 2 次）才置离线，之后按离线重探间隔（默认 300s）继续探，设备恢复自动上线；</li>
 *   <li>每 60 秒做一次兜底：DB 里存在但队列里没有的设备重新入队（服务重启、Redis 清空后自愈）。</li>
 * </ol>
 * 多实例通过 ZREM 抢占：只有从 ZSET 里删成功的实例才去探测，不会重复打设备。
 * <p>
 * 队列与状态只以 Redis 为准，不再保留 Redis 故障时回退 SQL 轮询的降级分支。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnvifDeviceProbeTask {

    /** 每轮最多处理的到期设备数，避免瞬时打满线程池 */
    private static final int BATCH_LIMIT = 200;

    /** 兜底补齐队列的间隔（毫秒） */
    private static final long BOOTSTRAP_INTERVAL_MS = 60_000L;

    private final IDeviceOnvifService deviceOnvifService;
    private final IDeviceOnvifChannelService deviceOnvifChannelService;
    private final OnvifProbeStateCache probeStateCache;

    /** 在线设备的探测间隔（秒），设备未配置心跳间隔时使用 */
    @Value("${onvif.probe.online-interval:60}")
    private long onlineIntervalSec = 60;

    /** 探测失败后的重试间隔（秒） */
    @Value("${onvif.probe.fail-retry-interval:15}")
    private long failRetryIntervalSec = 15;

    /** 判定离线后的重探间隔（秒） */
    @Value("${onvif.probe.offline-retry-interval:300}")
    private long offlineRetryIntervalSec = 300;

    /** 探测线程池：core=20 / max=50 / queue=5000，队列满时由调度线程兜底执行 */
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            20, 50, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5000),
            r -> {
                Thread t = new Thread(r, "onvif-probe");
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy());

    private volatile long lastBootstrapAt = 0L;

    /**
     * 每 3 秒从 Redis 取到期设备并发探测。
     * 单轮只处理到期的，绝大部分轮次是空转，开销很小。
     */
    @Scheduled(fixedDelay = 3_000, initialDelay = 15_000)
    public void probeTask() {
        long now = System.currentTimeMillis();
        bootstrapIfNeeded(now);

        List<String> dueList = probeStateCache.takeDue(now, BATCH_LIMIT);
        if (dueList.isEmpty()) {
            return;
        }
        for (String deviceId : dueList) {
            try {
                executor.execute(() -> probeOne(deviceId));
            } catch (RejectedExecutionException e) {
                // 线程池彻底拒绝：放回队列，5 秒后再探
                probeStateCache.schedule(deviceId, System.currentTimeMillis() + 5_000L);
            }
        }
    }

    /**
     * 兜底补齐：服务重启后队列是空的，或部分设备没入队（历史数据），
     * 定期把 DB 中缺失的 ONVIF 设备补进队列。
     */
    private void bootstrapIfNeeded(long now) {
        if (now - lastBootstrapAt < BOOTSTRAP_INTERVAL_MS) {
            return;
        }
        lastBootstrapAt = now;

        List<DeviceOnvif> devices;
        try {
            // 兜底查询不能被上层的分页参数污染
            PageHelper.clearPage();
            DeviceOnvif query = new DeviceOnvif();
            query.setProtocolType("ONVIF");
            devices = deviceOnvifService.selectDeviceOnvifList(query);
        } catch (Exception e) {
            log.warn("ONVIF 探测队列补齐查询失败: {}", e.getMessage());
            return;
        }
        if (devices == null || devices.isEmpty()) {
            return;
        }

        Set<String> scheduled = probeStateCache.scheduledDeviceIds();
        int added = 0;
        for (DeviceOnvif device : devices) {
            String deviceId = device.getDeviceId();
            if (deviceId == null || scheduled.contains(deviceId)) {
                continue;
            }
            // 加 0~3 秒随机偏移，避免重启后同时涌向同一批设备
            probeStateCache.schedule(deviceId, now + ThreadLocalRandom.current().nextInt(3000));
            added++;
        }
        if (added > 0) {
            log.info("ONVIF 探测队列补齐 {} 台设备（库中共 {} 台）", added, devices.size());
        }
    }

    /**
     * 单台设备探测。
     */
    private void probeOne(String deviceId) {
        DeviceOnvif device;
        try {
            device = deviceOnvifService.selectDeviceOnvifByDeviceId(deviceId);
        } catch (Exception e) {
            log.warn("ONVIF 探测查询设备失败, deviceId:{}, err:{}", deviceId, e.getMessage());
            probeStateCache.schedule(deviceId, System.currentTimeMillis() + 30_000L);
            return;
        }
        if (device == null) {
            // 设备已删除，清理队列与状态
            probeStateCache.evict(deviceId);
            return;
        }

        long now = System.currentTimeMillis();
        boolean online;
        try {
            online = deviceOnvifService.probeDeviceOnline(device);
        } catch (Exception e) {
            online = false;
            log.debug("ONVIF 探测异常, deviceId:{}, err:{}", deviceId, e.getMessage());
        }

        OnvifProbeStateCache.ProbeState state = probeStateCache.getState(deviceId);

        if (online) {
            boolean wasOffline = !Boolean.TRUE.equals(device.getOnLine());
            device.setOnLine(true);
            device.setKeepaliveTime(DateUtils.getTime());
            updateProbe(device);
            if (wasOffline) {
                setChannelsOnline(device);
                log.info("ONVIF 设备恢复在线: {}", deviceId);
            }
            probeStateCache.saveState(deviceId, 0, now, true);
            probeStateCache.schedule(deviceId, now + resolveOnlineInterval(device));
            return;
        }

        int failCount = state.failCount + 1;
        int maxFail = resolveMaxFail(device);
        if (failCount >= maxFail) {
            boolean wasOnline = Boolean.TRUE.equals(device.getOnLine());
            device.setOnLine(false);
            device.setKeepaliveTime(DateUtils.getTime());
            updateProbe(device);
            if (wasOnline) {
                setChannelsOffline(device);
                log.info("ONVIF 设备判离线: {}, 连续探测失败 {} 次", deviceId, failCount);
            }
            probeStateCache.saveState(deviceId, 0, state.lastOkMs, false);
            probeStateCache.schedule(deviceId, now + Math.max(offlineRetryIntervalSec, 30) * 1000L);
            return;
        }

        // 未达阈值：保持原状态，短暂后重试（网络抖动、设备重启中）
        log.debug("ONVIF 探测失败 {} 次，暂不判离线: {}", failCount, deviceId);
        probeStateCache.saveState(deviceId, failCount, state.lastOkMs, state.online);
        probeStateCache.schedule(deviceId, now + Math.max(failRetryIntervalSec, 5) * 1000L);
    }

    /**
     * 在线探测间隔：优先用设备上报的心跳间隔（秒），限制在 10~300 秒，避免异常值把探测打得太密。
     */
    private long resolveOnlineInterval(DeviceOnvif device) {
        Long heartBeatInterval = device.getHeartBeatInterval();
        long seconds = heartBeatInterval != null && heartBeatInterval > 0
                ? heartBeatInterval
                : Math.max(onlineIntervalSec, 10);
        return Math.min(Math.max(seconds, 10L), 300L) * 1000L;
    }

    /**
     * 判离线所需的连续失败次数：优先用设备配置，限制在 1~10。
     */
    private int resolveMaxFail(DeviceOnvif device) {
        Long heartBeatCount = device.getHeartBeatCount();
        int max = heartBeatCount != null && heartBeatCount > 0 ? heartBeatCount.intValue() : 2;
        return Math.min(Math.max(max, 1), 10);
    }

    private void updateProbe(DeviceOnvif device) {
        try {
            deviceOnvifService.updateDeviceOnvifProbe(device);
        } catch (Exception e) {
            log.warn("ONVIF 探测结果落库失败, deviceId:{}, err:{}", device.getDeviceId(), e.getMessage());
        }
    }

    private void setChannelsOnline(DeviceOnvif device) {
        try {
            deviceOnvifChannelService.updateStatusByDataDeviceId(device.getId(), "ON");
        } catch (Exception e) {
            log.error("[ONVIF探测] 联动通道上线失败: {}", e.getMessage());
        }
    }

    private void setChannelsOffline(DeviceOnvif device) {
        try {
            deviceOnvifChannelService.updateStatusByDataDeviceId(device.getId(), "OFF");
        } catch (Exception e) {
            log.error("[ONVIF探测] 联动通道离线失败: {}", e.getMessage());
        }
    }
}
