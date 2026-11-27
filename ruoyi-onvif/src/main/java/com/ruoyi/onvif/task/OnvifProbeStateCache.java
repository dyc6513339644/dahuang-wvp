package com.ruoyi.onvif.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ONVIF 探测状态缓存（对齐 ISUP 的 Redis 思路）。
 * <p>
 * 与 ISUP 的区别：ISUP 是设备主动上报心跳，用 {@code DynamicTask} 到期判离线；
 * ONVIF 只能由平台主动探测，因此用 Redis ZSET 当「到期探测队列」——
 * member = deviceId，score = 下次探测的毫秒时间戳。效果等价于「每台设备一个延时任务」，
 * 但任务状态在 Redis 里，服务重启、多实例部署都不丢，也不会重复探测。
 * <p>
 * 状态 Hash：field = deviceId，value = {@code 连续失败次数|最近探测成功毫秒|是否在线(1/0)}，
 * 用纯字符串避免序列化差异，跨库/跨版本都稳。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OnvifProbeStateCache {

    /** 到期探测队列（ZSET）：member=deviceId，score=下次探测时间戳（毫秒） */
    public static final String QUEUE_KEY = "ONVIF_PROBE_QUEUE";

    /** 探测状态（HASH）：deviceId -> failCount|lastOkMillis|online */
    public static final String STATE_KEY = "ONVIF_PROBE_STATE";

    private final RedisTemplate<Object, Object> redisTemplate;

    /** 放入/更新探测时间 */
    public void schedule(String deviceId, long nextProbeAtMs) {
        if (deviceId == null) {
            return;
        }
        try {
            redisTemplate.opsForZSet().add(QUEUE_KEY, deviceId, nextProbeAtMs);
        } catch (Exception e) {
            log.warn("ONVIF 探测队列入队失败, deviceId:{}, err:{}", deviceId, e.getMessage());
        }
    }

    /**
     * 取出到期设备并原子抢占（ZREM 成功才算抢到，多实例不会重复探测）。
     *
     * @return 到期设备列表，无到期或 Redis 异常时返回空列表
     */
    public List<String> takeDue(long nowMs, int limit) {
        List<String> result = new ArrayList<>();
        try {
            Set<Object> members = redisTemplate.opsForZSet()
                    .rangeByScore(QUEUE_KEY, Double.NEGATIVE_INFINITY, nowMs, 0, limit);
            if (members == null || members.isEmpty()) {
                return result;
            }
            for (Object member : members) {
                String deviceId = String.valueOf(member);
                Long removed = redisTemplate.opsForZSet().remove(QUEUE_KEY, member);
                if (removed != null && removed > 0) {
                    result.add(deviceId);
                }
            }
        } catch (Exception e) {
            log.warn("ONVIF 到期探测队列读取失败: {}", e.getMessage());
        }
        return result;
    }

    /**
     * 当前队列中的全部设备与到期时间。
     *
     * @return deviceId -> 下次探测时间戳；Redis 异常时返回空 Map
     */
    public Map<String, Long> allScheduled() {
        Map<String, Long> map = new HashMap<>();
        try {
            Set<ZSetOperations.TypedTuple<Object>> tuples =
                    redisTemplate.opsForZSet().rangeWithScores(QUEUE_KEY, 0, -1);
            if (tuples != null) {
                for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
                    if (tuple.getValue() != null && tuple.getScore() != null) {
                        map.put(String.valueOf(tuple.getValue()), tuple.getScore().longValue());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("ONVIF 探测队列全量读取失败: {}", e.getMessage());
        }
        return map;
    }

    /** 当前队列里的设备 ID 集合（服务重启后用于补齐） */
    public Set<String> scheduledDeviceIds() {
        return new HashSet<>(allScheduled().keySet());
    }

    /** 设备删除时从队列和状态里移除 */
    public void evict(String deviceId) {
        if (deviceId == null) {
            return;
        }
        try {
            redisTemplate.opsForZSet().remove(QUEUE_KEY, deviceId);
            redisTemplate.opsForHash().delete(STATE_KEY, deviceId);
        } catch (Exception e) {
            log.warn("ONVIF 探测状态清理失败, deviceId:{}, err:{}", deviceId, e.getMessage());
        }
    }

    public void saveState(String deviceId, int failCount, long lastOkMs, boolean online) {
        try {
            redisTemplate.opsForHash().put(STATE_KEY, deviceId,
                    failCount + "|" + lastOkMs + "|" + (online ? "1" : "0"));
        } catch (Exception e) {
            log.warn("ONVIF 探测状态写入失败, deviceId:{}, err:{}", deviceId, e.getMessage());
        }
    }

    public ProbeState getState(String deviceId) {
        try {
            Object value = redisTemplate.opsForHash().get(STATE_KEY, deviceId);
            if (value != null) {
                String[] parts = String.valueOf(value).split("\\|");
                if (parts.length >= 3) {
                    return new ProbeState(Integer.parseInt(parts[0].trim()),
                            Long.parseLong(parts[1].trim()), "1".equals(parts[2].trim()));
                }
            }
        } catch (Exception e) {
            log.warn("ONVIF 探测状态读取失败, deviceId:{}, err:{}", deviceId, e.getMessage());
        }
        return new ProbeState(0, 0L, false);
    }

    /** 探测状态快照 */
    public static class ProbeState {
        /** 连续失败次数 */
        public final int failCount;
        /** 最近一次探测成功的毫秒时间戳 */
        public final long lastOkMs;
        /** 最近一次已知的在线状态 */
        public final boolean online;

        public ProbeState(int failCount, long lastOkMs, boolean online) {
            this.failCount = failCount;
            this.lastOkMs = lastOkMs;
            this.online = online;
        }
    }
}
