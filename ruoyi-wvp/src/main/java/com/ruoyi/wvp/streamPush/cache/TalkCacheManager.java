package com.ruoyi.wvp.streamPush.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.ruoyi.media.event.media.MediaArrivalEvent;
import com.ruoyi.media.event.media.MediaDepartureEvent;

/**
 * 语音/广播对讲设备缓存管理器（纯内存，不落DB）
 *
 * <p>背景：broadcast/talk 流是临时语音流，其 Device + Channel 不需要持久化到 wvp_device/wvp_device_channel 表。
 * 每次语音对讲开始写入缓存，结束时从缓存移除。服务重启后缓存自然丢失（符合预期——重启后流已不存在）。</p>
 *
 * <p>线程安全：使用 ConcurrentHashMap，支持并发 put/remove。</p>
 *
 * <p>生命周期：<br>
 *   上线：MediaArrivalEvent → put<br>
 *   离线：MediaDepartureEvent → remove<br>
 *   ZLM 离线：zlmServerOffline → removeByMediaServerId</p>
 */
@Component
@Slf4j
public class TalkCacheManager {

    /** key = deviceId (app + "_" + stream) */
    private final ConcurrentHashMap<String, TalkCacheItem> cache = new ConcurrentHashMap<>();

    /**
     * 添加缓存条目（幂等：已存在则覆盖）
     */
    public void put(String app, String stream, String name, String mediaServerId, String createTime) {
        TalkCacheItem item = TalkCacheItem.of(app, stream, name, mediaServerId, createTime);
        TalkCacheItem old = cache.put(item.getDeviceId(), item);
        if (old != null) {
            log.info("[TalkCache] 覆盖条目: {}", item.getDeviceId());
        } else {
            log.info("[TalkCache] 新增条目: {}", item.getDeviceId());
        }
    }

    /**
     * 获取缓存条目
     */
    public TalkCacheItem get(String deviceId) {
        return cache.get(deviceId);
    }

    /**
     * 通过 app + stream 获取
     */
    public TalkCacheItem get(String app, String stream) {
        return cache.get(app + "_" + stream);
    }

    /**
     * 移除缓存条目
     */
    public TalkCacheItem remove(String deviceId) {
        TalkCacheItem removed = cache.remove(deviceId);
        if (removed != null) {
            log.info("[TalkCache] 移除条目: {}", deviceId);
        }
        return removed;
    }

    /**
     * 移除指定 ZLM 节点上的所有缓存条目（ZLM 离线时调用）
     */
    public void removeByMediaServerId(String mediaServerId) {
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, TalkCacheItem> entry : cache.entrySet()) {
            if (mediaServerId.equals(entry.getValue().getMediaServerId())) {
                keysToRemove.add(entry.getKey());
            }
        }
        for (String key : keysToRemove) {
            cache.remove(key);
        }
        if (!keysToRemove.isEmpty()) {
            log.info("[TalkCache] ZLM({}) 离线，清理 {} 个条目", mediaServerId, keysToRemove.size());
        }
    }

    /**
     * 是否包含指定条目
     */
    public boolean containsKey(String deviceId) {
        return cache.containsKey(deviceId);
    }

    /**
     * 获取所有缓存条目
     */
    public Collection<TalkCacheItem> getAll() {
        return cache.values();
    }

    /**
     * 获取所有 app_stream 映射（用于 getAllAppAndStreamMap 合并）
     */
    public Map<String, TalkCacheItem> getAllAsMap() {
        return new ConcurrentHashMap<>(cache);
    }

    /**
     * 缓存条目数量
     */
    public int size() {
        return cache.size();
    }

    /**
     * 清空所有缓存（仅用于测试或特殊场景）
     */
    public void clear() {
        cache.clear();
        log.info("[TalkCache] 缓存已清空");
    }
}
