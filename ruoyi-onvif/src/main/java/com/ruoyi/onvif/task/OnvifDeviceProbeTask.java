package com.ruoyi.onvif.task;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * ONVIF 设备在线状态探测任务（ruoyi-onvif 版本）
 */
@Component
public class OnvifDeviceProbeTask {

    private static final Logger log = LoggerFactory.getLogger(OnvifDeviceProbeTask.class);

    /** 时间格式化（yyyy-MM-dd HH:mm:ss，与 keepalive_time 列格式一致，字符串可比） */
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 连续探测失败计数（Key: deviceId, 内存级，重启清空） */
    private final Map<String, Integer> failCountMap = new ConcurrentHashMap<>();

    /**
     * 探测线程池
     * core=20 / max=50 / queue=5000 / CallerRunsPolicy
     */
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            20, 50, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(5000),
            r -> { Thread t = new Thread(r, "onvif-probe"); t.setDaemon(true); return t; },
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private IDeviceOnvifService deviceOnvifService;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    /**
     * 每 30 秒执行一次在线探测
     */
    @Scheduled(fixedDelay = 30_000, initialDelay = 10_000)
    public void probeTask() {
        PageHelper.clearPage();
        // 预计算时间阈值，避免 SQL 中使用 TIMESTAMPDIFF/NOW（SQLite/达梦不兼容）
        LocalDateTime now = LocalDateTime.now();
        String heartbeatThreshold = now.minusSeconds(30).format(FMT);   // 在线设备心跳超时
        String offlineThreshold = now.minusSeconds(300).format(FMT);    // 离线设备 5 分钟后重探
        List<DeviceOnvif> devices = deviceOnvifService.selectOnvifDevicesForProbe(heartbeatThreshold, offlineThreshold);
        if (devices.isEmpty()) return;
        CountDownLatch latch = new CountDownLatch(devices.size());
        for (DeviceOnvif dev : devices) {
            executor.execute(() -> {
                try {
                    probeOne(latch, dev);
                } catch (Exception e) {
                    latch.countDown();
                }
            });
        }
    }

    private void probeOne(CountDownLatch latch, DeviceOnvif device) {
        String now = DateUtils.getTime();
        boolean online = deviceOnvifService.probeDeviceOnline(device);
        long maxFail = device.getHeartBeatCount() != null ? device.getHeartBeatCount() : 2L;

        device.setKeepaliveTime(now);

        if (online) {
            failCountMap.remove(device.getDeviceId());
            if (!device.getOnLine() ) {
                setChannelsOnline(device);
            }
            device.setOnLine(true);
        } else {
            int failCount = failCountMap.getOrDefault(device.getDeviceId(), 0) + 1;
            failCountMap.put(device.getDeviceId(), failCount);
            if (failCount >= maxFail) {
                if ( device.getOnLine()) {
                    setChannelsOffline(device);
                }
                device.setOnLine(false);
            }
        }

        deviceOnvifService.updateDeviceOnvifProbe(device);
        latch.countDown();
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
