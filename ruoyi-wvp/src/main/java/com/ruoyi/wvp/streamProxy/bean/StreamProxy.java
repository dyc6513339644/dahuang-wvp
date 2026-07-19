package com.ruoyi.wvp.streamProxy.bean;

import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.ObjectUtils;

/**
 * 拉流代理 DTO（不再对应数据库表，纯数据载体）
 * 用于 ZLM 层交互，从 Device 构建
 *
 * @author lin
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StreamProxy extends CommonGBChannel {

    /**
     * 数据库自增ID（对应 wvp_device.id）
     */
    private int id;

    /**
     * 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流
     */
    private String type;

    /**
     * 应用名（对应 wvp_device.device_id，自动生成）
     */
    private String app;

    /**
     * 流ID（对应 wvp_device_channel.device_id = app + "01"）
     */
    private String stream;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 当前拉流使用的流媒体服务ID
     */
    private String mediaServerId;

    /**
     * 固定选择的流媒体服务ID
     */
    private String relatesMediaServerId;

    /**
     * 拉流地址
     */
    private String srcUrl;

    /**
     * 超时时间:秒
     */
    private int timeout;

    /**
     * ffmpeg模板KEY
     */
    private String ffmpegCmdKey;

    /**
     * rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播
     */
    private String rtspType;

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
     * 拉流代理时zlm返回的key，用于停止拉流代理
     */
    private String streamKey;

    /**
     * 拉流状态
     */
    private Boolean pulling;

    public CommonGBChannel buildCommonGBChannel() {
        if (ObjectUtils.isEmpty(this.getGbDeviceId())) {
            return null;
        }
        if (ObjectUtils.isEmpty(this.getGbName())) {
            this.setGbName(name);
        }
        this.setDataType(ChannelDataType.STREAM_PROXY.value);
        this.setDataDeviceId(this.getId());
        return this;
    }

    /**
     * 从 Device 构建 StreamProxy DTO
     */
    public static StreamProxy buildFromDevice(Device device) {
        StreamProxy proxy = new StreamProxy();
        proxy.setId(device.getId());
        proxy.setApp(device.getDeviceId());
        proxy.setStream(device.getDeviceId() + "01");
        proxy.setName(device.getName());
        proxy.setType(device.getStreamType() != null ? device.getStreamType() : "default");
        proxy.setSrcUrl(device.getSrcUrl());
        proxy.setTimeout(device.getExpires());
        proxy.setFfmpegCmdKey(device.getFfmpegCmdKey());
        proxy.setRtspType(device.getRtspType() != null ? device.getRtspType() : "0");
        proxy.setEnableAudio(device.isEnableAudio());
        proxy.setEnableMp4(device.isEnableMp4());
        proxy.setEnableRemoveNoneReader(device.isEnableRemoveNoneReader());
        proxy.setEnableDisableNoneReader(device.isEnableDisableNoneReader());
        proxy.setStreamKey(device.getStreamKey());
        proxy.setMediaServerId(device.getMediaServerId());
        proxy.setRelatesMediaServerId(device.getMediaServerId());
        proxy.setPulling(device.isOnLine());
        proxy.setEnable(true);
        proxy.setGbDeviceId(proxy.getStream());
        proxy.setGbName(device.getName());
        return proxy;
    }
}
