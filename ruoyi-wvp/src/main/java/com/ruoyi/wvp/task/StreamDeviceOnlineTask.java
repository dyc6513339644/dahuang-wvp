package com.ruoyi.wvp.task;

import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.media.zlm.dto.hook.OriginType;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.mapper.CommonGBChannelMapper;
import com.ruoyi.wvp.mapper.DeviceMapper;
import com.ruoyi.wvp.mapper.StreamProxyMapper;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 推流 / 拉流设备在线状态巡检（对齐 ISUP 的在离线机制）。
 * <p>
 * 推流、拉流设备的在线状态完全由 ZLM 的流决定，原来只在 ZLM 节点上下线、流状态变更等 hook 里更新：
 * <ul>
 *   <li>服务重启后 ZLM 没有重新连上来，DB 里的在线记录没人核对，列表长期“假在线”；</li>
 *   <li>hook 丢失（ZLM 重启、网络抖动）时同样不会自动纠正。</li>
 * </ul>
 * 本任务每 60 秒以 ZLM 的 {@code getMediaList} 为准做一次对账（等价于“心跳”）：
 * <ol>
 *   <li>流列表里能查到该设备的 app → 刷新 Redis 心跳，DB 离线则恢复在线并联动通道 ON；</li>
 *   <li>查不到 → 连续失败计数 +1，连续 {@value #OFFLINE_FAIL_THRESHOLD} 轮都查不到才置离线，
 *       避免 ZLM 接口偶发超时导致误判；</li>
 *   <li>失败计数存 Redis（{@link #STATE_KEY}），服务重启不清零、多实例共享。</li>
 * </ol>
 * 注意：本任务只对账状态，不会停止或关闭任何流。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StreamDeviceOnlineTask {

    /** Redis Hash：field=deviceId，value="连续失败次数|最近一次探到流的毫秒时间戳" */
    public static final String STATE_KEY = "STREAM_DEVICE_ONLINE_STATE";

    /** 连续多少轮在 ZLM 找不到流才判离线 */
    private static final int OFFLINE_FAIL_THRESHOLD = 2;

    private final IMediaServerService mediaServerService;
    private final DeviceMapper deviceMapper;
    private final StreamProxyMapper streamProxyMapper;
    private final CommonGBChannelMapper commonGBChannelMapper;
    private final RedisTemplate<Object, Object> redisTemplate;

    @Scheduled(fixedDelay = 60_000, initialDelay = 30_000)
    public void checkOnlineStatus() {
        List<MediaServer> onlineServers;
        try {
            onlineServers = mediaServerService.getAllOnline();
        } catch (Exception e) {
            log.warn("[流设备巡检] 查询在线媒体节点失败: {}", e.getMessage());
            return;
        }
        if (onlineServers == null || onlineServers.isEmpty()) {
            return;
        }
        int offlineCount = 0;
        for (MediaServer server : onlineServers) {
            try {
                offlineCount += checkServer(server);
            } catch (Exception e) {
                log.warn("[流设备巡检] 节点 {} 处理失败: {}", server.getId(), e.getMessage());
            }
        }
        // 所属节点已不在在线列表（被踢下线/删除）的在线设备，收尾置离线
        try {
            offlineCount += offlineOrphanDevices(onlineServers);
        } catch (Exception e) {
            log.warn("[流设备巡检] 孤岛在线设备处理失败: {}", e.getMessage());
        }
        if (offlineCount > 0) {
            log.info("[流设备巡检] 本轮标记 {} 台推拉流设备离线", offlineCount);
        }
    }

    /**
     * 处理「DB 里在线、但所属媒体节点已不可用」的设备。
     * 这类设备不属于任何在线节点，上面的对账循环覆盖不到。
     */
    private int offlineOrphanDevices(List<MediaServer> onlineServers) {
        Set<String> onlineServerIds = new HashSet<>();
        for (MediaServer server : onlineServers) {
            if (server.getId() != null) {
                onlineServerIds.add(server.getId());
            }
        }

        List<Device> onlineDevices = new ArrayList<>();
        try {
            List<Device> proxyDevices = deviceMapper.getProxyDeviceListByCondition(null, true);
            if (proxyDevices != null) {
                onlineDevices.addAll(proxyDevices);
            }
            List<Device> pushDevices = deviceMapper.getPushDeviceListByCondition(null, true, null);
            if (pushDevices != null) {
                onlineDevices.addAll(pushDevices);
            }
        } catch (Exception e) {
            log.warn("[流设备巡检] 查询在线推拉流设备失败: {}", e.getMessage());
            return 0;
        }

        int count = 0;
        for (Device device : onlineDevices) {
            String serverId = device.getMediaServerId();
            if (serverId != null && !serverId.isEmpty() && onlineServerIds.contains(serverId)) {
                continue;
            }
            device.setOnLine(false);
            device.setUpdateTime(DateUtil.getNow());
            updateDeviceStatus(device);
            updateChannelStatus(device.getDeviceId(), "OFF");
            saveState(device.getDeviceId(), 0, 0L);
            log.info("[流设备巡检] 设备所属媒体节点不可用，判离线: {}, server:{}", device.getDeviceId(), serverId);
            count++;
        }
        return count;
    }

    /**
     * 按节点对账：一次 getMediaList 取全部流，再和该节点上的推流/拉流设备比对。
     *
     * @return 本轮判离线的设备数
     */
    private int checkServer(MediaServer server) {
        List<StreamInfo> streamList;
        try {
            streamList = mediaServerService.getMediaList(server, null, null, null);
        } catch (Exception e) {
            // 节点不可达：本轮整节点跳过，不参与失败计数，避免把节点故障当成设备离线
            log.warn("[流设备巡检] 获取流列表失败，跳过该节点, server:{}, err:{}", server.getId(), e.getMessage());
            return 0;
        }

        Set<String> aliveApps = new HashSet<>();
        if (streamList != null) {
            for (StreamInfo info : streamList) {
                int originType = info.getOriginType();
                if (originType == OriginType.PULL.ordinal()
                        || originType == OriginType.FFMPEG_PULL.ordinal()
                        || originType == OriginType.RTSP_PUSH.ordinal()
                        || originType == OriginType.RTMP_PUSH.ordinal()
                        || originType == OriginType.RTC_PUSH.ordinal()) {
                    aliveApps.add(info.getApp());
                }
            }
        }

        List<Device> devices = new ArrayList<>();
        try {
            List<Device> proxyDevices = streamProxyMapper.selectForPushingInMediaServer(server.getId(), true);
            if (proxyDevices != null) {
                devices.addAll(proxyDevices);
            }
            List<Device> pushDevices = deviceMapper.getPushDeviceListByMediaServerId(server.getId());
            if (pushDevices != null) {
                devices.addAll(pushDevices);
            }
        } catch (Exception e) {
            log.warn("[流设备巡检] 查询节点设备失败, server:{}, err:{}", server.getId(), e.getMessage());
            return 0;
        }
        if (devices.isEmpty()) {
            return 0;
        }

        int offlineCount = 0;
        for (Device device : devices) {
            try {
                if (checkDevice(device, aliveApps.contains(device.getDeviceId()), server.getId())) {
                    offlineCount++;
                }
            } catch (Exception e) {
                log.warn("[流设备巡检] 设备处理失败, device:{}, err:{}", device.getDeviceId(), e.getMessage());
            }
        }
        return offlineCount;
    }

    /**
     * 单设备对账。
     *
     * @param serverId 当前对账的媒体节点 id，命中流时若与设备的 media_server_id 不一致会顺手修正
     * @return 本轮是否把设备从在线改成了离线
     */
    private boolean checkDevice(Device device, boolean alive, String serverId) {
        String deviceId = device.getDeviceId();
        long now = System.currentTimeMillis();
        int failCount = 0;
        long lastOkMs = 0L;
        String state = getState(deviceId);
        if (state != null) {
            String[] parts = state.split("\\|");
            if (parts.length >= 2) {
                try {
                    failCount = Integer.parseInt(parts[0].trim());
                    lastOkMs = Long.parseLong(parts[1].trim());
                } catch (NumberFormatException ignore) {
                    // 脏数据按初始状态处理
                }
            }
        }

        if (alive) {
            // Device.onLine 是基本类型 boolean，Lombok 生成的是 isOnLine()
            boolean wasOffline = !device.isOnLine();
            // media_server_id 可能残留 auto 或旧节点，命中流时顺手修正，避免下一轮被当成孤岛设备判离线
            boolean serverChanged = serverId != null && !Objects.equals(serverId, device.getMediaServerId());
            if (wasOffline || serverChanged) {
                device.setOnLine(true);
                if (serverChanged) {
                    device.setMediaServerId(serverId);
                }
                device.setUpdateTime(DateUtil.getNow());
                if ("STREAM_PUSH".equals(device.getProtocolType())) {
                    // 推流设备用 updatePushDevice，才会写回 media_server_id
                    deviceMapper.updatePushDevice(device);
                } else {
                    deviceMapper.updateProxyStream(device);
                }
                if (wasOffline) {
                    updateChannelStatus(deviceId, "ON");
                    log.info("[流设备巡检] ZLM 中已存在该设备的流，恢复在线: {}", deviceId);
                }
            }
            saveState(deviceId, 0, now);
            return false;
        }

        failCount++;
        if (failCount >= OFFLINE_FAIL_THRESHOLD) {
            saveState(deviceId, 0, lastOkMs);
            if (device.isOnLine()) {
                device.setOnLine(false);
                device.setUpdateTime(DateUtil.getNow());
                updateDeviceStatus(device);
                updateChannelStatus(deviceId, "OFF");
                log.info("[流设备巡检] 连续 {} 轮未在 ZLM 找到流，判离线: {}", failCount, deviceId);
                return true;
            }
            return false;
        }

        saveState(deviceId, failCount, lastOkMs);
        return false;
    }

    private void updateDeviceStatus(Device device) {
        if ("STREAM_PROXY".equals(device.getProtocolType())) {
            deviceMapper.updateProxyStream(device);
        } else {
            deviceMapper.updatePushDeviceStatus(device);
        }
    }

    private void updateChannelStatus(String deviceId, String status) {
        try {
            String channelDeviceId = deviceId + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            if (channel != null && !status.equals(channel.getGbStatus())) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), status);
            }
        } catch (Exception e) {
            log.warn("[流设备巡检] 通道状态同步失败, device:{}, err:{}", deviceId, e.getMessage());
        }
    }

    private String getState(String deviceId) {
        try {
            Object value = redisTemplate.opsForHash().get(STATE_KEY, deviceId);
            return value == null ? null : String.valueOf(value);
        } catch (Exception e) {
            log.warn("[流设备巡检] 读取状态失败, device:{}, err:{}", deviceId, e.getMessage());
            return null;
        }
    }

    private void saveState(String deviceId, int failCount, long lastOkMs) {
        try {
            redisTemplate.opsForHash().put(STATE_KEY, deviceId, failCount + "|" + lastOkMs);
        } catch (Exception e) {
            log.warn("[流设备巡检] 写入状态失败, device:{}, err:{}", deviceId, e.getMessage());
        }
    }
}
