package com.ruoyi.wvp.streamPush.bean;

import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import lombok.Data;

/**
 * 推流信息 DTO（数据来自 wvp_device + wvp_device_channel）
 */
@Data
public class StreamPush {

    /**
     * 设备数据库ID（wvp_device.id）
     */
    private Integer id;

    /**
     * 通道数据库ID（wvp_device_channel.id）
     */
    private Integer channelId;

    /**
     * 应用名（设备ID）
     */
    private String app;

    /**
     * 流ID（通道编码）
     */
    private String stream;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 通道数
     */
    private Integer channelCount;

    /**
     * 推流名称（用户输入，添加/编辑时使用）
     */
    private String name;

    /**
     * 国标编码
     */
    private String gbDeviceId;

    /**
     * 国标名称
     */
    private String gbName;

    /**
     * 经度
     */
    private Double gbLongitude;

    /**
     * 纬度
     */
    private Double gbLatitude;

    /**
     * 使用的流媒体ID
     */
    private String mediaServerId;

    /**
     * 推流时间
     */
    private String pushTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否正在推流
     */
    private boolean pushing;

    /**
     * 拉起离线推流
     */
    private boolean startOfflinePush;

    /**
     * 从 Device 和 CommonGBChannel 构建 StreamPush DTO
     */
    public static StreamPush buildFromDevice(Device device, CommonGBChannel channel) {
        StreamPush push = new StreamPush();
        push.setId(device.getId());
        push.setApp(device.getDeviceId());
        push.setDeviceName(device.getName());
        push.setName(device.getName());
        push.setMediaServerId(device.getMediaServerId());
        push.setPushing(device.isOnLine());
        push.setStartOfflinePush(device.isStartOfflinePush());
        push.setCreateTime(device.getCreateTime());
        push.setUpdateTime(device.getUpdateTime());
        push.setPushTime(device.getPushTime());

        if (channel != null) {
            push.setChannelId(channel.getGbId());
            push.setStream(channel.getDeviceId());
            push.setGbDeviceId(channel.getGbDeviceId());
            push.setGbName(channel.getGbName());
            push.setGbLongitude(channel.getGbLongitude());
            push.setGbLatitude(channel.getGbLatitude());
        }
        return push;
    }
}