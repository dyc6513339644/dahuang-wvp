package com.ruoyi.wvp.streamProxy.bean;

import com.ruoyi.wvp.gb28181.bean.Device;
import lombok.Data;

import java.util.UUID;

/**
 * 拉流代理参数
 * @author lin
 */
@Data
public class StreamProxyParam {

    /**
     * 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流
     */
    private String type;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 流媒体服务ID
     */
    private String mediaServerId;

    /**
     * 拉流地址
     */
    private String url;

    /**
     * 超时时间:秒
     */
    private int timeoutMs;

    /**
     * ffmpeg模板KEY
     */
    private String ffmpegCmdKey;

    /**
     * rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播
     */
    private String rtpType;

    /**
     * 是否启用
     */
    private boolean enable;

    /**
     * 是否启用音频
     */
    private boolean enableAudio;

    /**
     * 是否启用MP4
     */
    private boolean enableMp4;

    /**
     * 是否 无人观看时删除
     */
    private boolean enableRemoveNoneReader;

    /**
     * 是否 无人观看时自动停用
     */
    private boolean enableDisableNoneReader;

    /**
     * 生成唯一的设备ID
     */
    public static String generateDeviceId() {
        return "proxy" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    /**
     * 构建 Device 实体
     */
    public Device buildDevice() {
        Device device = new Device();
        device.setDeviceId(generateDeviceId());
        device.setName(name);
        device.setProtocolType("STREAM_PROXY");
        device.setSrcUrl(url);
        device.setStreamType(type != null ? type : "default");
        device.setExpires(timeoutMs > 0 ? timeoutMs : 15);
        device.setChannelCount(1);
        device.setFfmpegCmdKey(ffmpegCmdKey);
        device.setRtspType(rtpType != null ? rtpType : "0");
        device.setEnableAudio(enableAudio);
        device.setEnableMp4(enableMp4);
        device.setEnableRemoveNoneReader(enableRemoveNoneReader);
        device.setEnableDisableNoneReader(enableDisableNoneReader);
        return device;
    }

    /**
     * 构建 StreamProxy DTO（兼容旧接口）
     */
    public StreamProxy buildStreamProxy(String channelId) {
        Device device = buildDevice();
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        streamProxy.setRelatesMediaServerId(mediaServerId);
        streamProxy.setEnable(enable);
        return streamProxy;
    }
}
