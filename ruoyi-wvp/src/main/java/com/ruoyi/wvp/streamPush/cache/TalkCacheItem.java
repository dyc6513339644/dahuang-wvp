package com.ruoyi.wvp.streamPush.cache;

import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.streamPush.bean.StreamPush;

/**
 * 语音/广播对讲缓存条目（内存持有，不落DB）
 * deviceId = app + "_" + stream，保证唯一
 */
public class TalkCacheItem {

    /** 缓存key = app + "_" + stream（如 "talk_340200..._340200..."） */
    private String deviceId;

    /** 应用名（"broadcast" 或 "talk"） */
    private String app;

    /** 流名（如 "340200..._340200..."） */
    private String stream;

    /** 名称（"broadcast" 或 "talk"） */
    private String name;

    /** 所在 ZLM 节点 ID */
    private String mediaServerId;

    /** 创建时间 */
    private String createTime;

    public TalkCacheItem() {
    }

    public static TalkCacheItem of(String app, String stream, String name, String mediaServerId, String createTime) {
        TalkCacheItem item = new TalkCacheItem();
        item.deviceId = app + "_" + stream;
        item.app = app;
        item.stream = stream;
        item.name = name;
        item.mediaServerId = mediaServerId;
        item.createTime = createTime;
        return item;
    }

    /**
     * 从缓存构建 Device 对象（仅用于 buildFromDevice 等读操作，无 DB id）
     */
    public Device toDevice() {
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setName(name);
        device.setMediaServerId(mediaServerId);
        device.setOnLine(true);
        device.setCreateTime(createTime);
        device.setPushTime(createTime);
        return device;
    }

    /**
     * 从缓存构建 Channel 对象
     */
    public CommonGBChannel toChannel() {
        CommonGBChannel channel = new CommonGBChannel();
        channel.setDeviceId(stream);
        channel.setName(name);
        channel.setParentId(deviceId);
        return channel;
    }

    /**
     * 从缓存构建 StreamPush DTO
     */
    public StreamPush toStreamPush() {
        return StreamPush.buildFromDevice(toDevice(), toChannel());
    }

    // ========== getters/setters ==========

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMediaServerId() {
        return mediaServerId;
    }

    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
