package com.ruoyi.onvif.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 设备管理对象 wvp_device
 *
 * @author ruoyi
 * @date 2026-06-24
 */
@TableName("wvp_device")
public class DeviceOnvif extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 设备编号 */
    @Excel(name = "设备编号")
    private String deviceId;

    /** 设备名 */
    @Excel(name = "设备名")
    private String name;

    /** 生产厂商 */
    @Excel(name = "生产厂商")
    private String manufacturer;

    /** model */
    @Excel(name = "model")
    private String dbModel;

    /** 固件版本 */
    @Excel(name = "固件版本")
    private String firmware;

    /** 传输协议(UDP/TCP) */
    @Excel(name = "传输协议(UDP/TCP)")
    private String transport;

    /** 数据流传输模式 */
    @Excel(name = "数据流传输模式")
    private String streamMode;

    /** 在线状态 */
    @Excel(name = "在线状态")
    private Boolean onLine;

    /** 注册时间 */
    @Excel(name = "注册时间")
    private String registerTime;

    /** 心跳时间 */
    @Excel(name = "心跳时间")
    private String keepaliveTime;

    /** IP */
    @Excel(name = "IP")
    private String ip;

    /** 端口 */
    @Excel(name = "端口")
    private Long port;

    /** 注册有效期 */
    @Excel(name = "注册有效期")
    private Long expires;

    /** 目录订阅周期，0为不订阅 */
    @Excel(name = "目录订阅周期，0为不订阅")
    private Boolean subscribeCycleForCatalog;

    /** 移动设备位置订阅周期，0为不订阅 */
    @Excel(name = "移动设备位置订阅周期，0为不订阅")
    private Boolean  subscribeCycleForMobilePosition;

    /** 移动设备位置信息上报时间间隔,单位:秒,默认值5 */
    @Excel(name = "移动设备位置信息上报时间间隔,单位:秒,默认值5")
    private Integer  mobilePositionSubmissionInterval;

    /** 报警订阅周期，0为不订阅 */
    @Excel(name = "报警订阅周期，0为不订阅")
    private Boolean  subscribeCycleForAlarm;

    /** HOST地址 */
    @Excel(name = "HOST地址")
    private String hostAddress;

    /** 字符集 */
    @Excel(name = "字符集")
    private String charset;

    /** 是否开启ssrc校验，默认关闭 */
    @Excel(name = "是否开启ssrc校验，默认关闭")
    private Boolean  ssrcCheck;

    /** 地理坐标系， 目前支持 WGS84,GCJ02, 此字段保留，暂无用 */
    @Excel(name = "地理坐标系， 目前支持 WGS84,GCJ02, 此字段保留，暂无用")
    private String geoCoordSys;

    /** 设备使用的媒体id */
    @Excel(name = "设备使用的媒体id")
    private String mediaServerId;

    /** 自定义名称 */
    @Excel(name = "自定义名称")
    private String customName;

    /** 收流IP */
    @Excel(name = "收流IP")
    private String sdpIp;

    /** SIP交互IP（设备访问平台的IP） */
    @Excel(name = "SIP交互IP", readConverterExp = "设=备访问平台的IP")
    private String localIp;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 是否作为消息通道 */
    @Excel(name = "是否作为消息通道")
    private Boolean  asMessageChannel;

    /** 心跳间隔 */
    @Excel(name = "心跳间隔")
    private Long heartBeatInterval;

    /** 心跳超时次数 */
    @Excel(name = "心跳超时次数")
    private Long heartBeatCount;

    /** 定位功能支持情况 */
    @Excel(name = "定位功能支持情况")
    private Long positionCapability;

    /** 控制语音对讲流程，释放收到ACK后发流 */
    @Excel(name = "控制语音对讲流程，释放收到ACK后发流")
    private Boolean  broadcastPushAfterAck;

    /** 纬度 */
    @Excel(name = "纬度")
    private String lat;

    /** 经度 */
    @Excel(name = "经度")
    private String lng;

    /** 地图定位地址 */
    @Excel(name = "地图定位地址")
    private String addressMap;

    /** 设备接入协议类型 */
    @Excel(name = "设备接入协议类型")
    private String protocolType;

    /** 拉流源地址 */
    @Excel(name = "拉流源地址")
    private String srcUrl;

    /** 代理类型 */
    @Excel(name = "代理类型")
    private String streamType;

    /** ffmpeg模板KEY */
    @Excel(name = "ffmpeg模板KEY")
    private String ffmpegCmdKey;

    /** RTSP拉流方式 */
    @Excel(name = "RTSP拉流方式")
    private String rtspType;

    /** 是否启用音频 */
    @Excel(name = "是否启用音频")
    private Boolean enableAudio;

    /** 是否启用录像 */
    @Excel(name = "是否启用录像")
    private Boolean enableMp4;

    /** 无人观看时自动移除 */
    @Excel(name = "无人观看时自动移除")
    private Boolean enableRemoveNoneReader;

    /** 无人观看时自动停用 */
    @Excel(name = "无人观看时自动停用")
    private Boolean enableDisableNoneReader;

    /** ZLM返回的streamKey */
    @Excel(name = "ZLM返回的streamKey")
    private String streamKey;

    /** 是否拉起离线推流 */
    @Excel(name = "是否拉起离线推流")
    private Boolean startOfflinePush;

    /** 推流状态 */
    @Excel(name = "推流状态")
    private Boolean pushing;

    /** 推流时间 */
    @Excel(name = "推流时间")
    private String pushTime;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** onvif 设备鉴权方式 */
    @Excel(name = "onvif 设备鉴权方式")
    private String onvifAuthType;

    /** 通道数量（非表字段，仅查询时使用） */
    @TableField(exist = false)
    private Integer channelCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDbModel() {
        return dbModel;
    }

    public void setDbModel(String dbModel) {
        this.dbModel = dbModel;
    }

    public String getFirmware() {
        return firmware;
    }

    public void setFirmware(String firmware) {
        this.firmware = firmware;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getStreamMode() {
        return streamMode;
    }

    public void setStreamMode(String streamMode) {
        this.streamMode = streamMode;
    }

    public Boolean getOnLine() {
        return onLine;
    }

    public void setOnLine(Boolean onLine) {
        this.onLine = onLine;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getKeepaliveTime() {
        return keepaliveTime;
    }

    public void setKeepaliveTime(String keepaliveTime) {
        this.keepaliveTime = keepaliveTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public Boolean getSubscribeCycleForCatalog() {
        return subscribeCycleForCatalog;
    }

    public void setSubscribeCycleForCatalog(Boolean subscribeCycleForCatalog) {
        this.subscribeCycleForCatalog = subscribeCycleForCatalog;
    }

    public Boolean getSubscribeCycleForMobilePosition() {
        return subscribeCycleForMobilePosition;
    }

    public void setSubscribeCycleForMobilePosition(Boolean subscribeCycleForMobilePosition) {
        this.subscribeCycleForMobilePosition = subscribeCycleForMobilePosition;
    }

    public Integer getMobilePositionSubmissionInterval() {
        return mobilePositionSubmissionInterval;
    }

    public void setMobilePositionSubmissionInterval(Integer mobilePositionSubmissionInterval) {
        this.mobilePositionSubmissionInterval = mobilePositionSubmissionInterval;
    }

    public Boolean getSubscribeCycleForAlarm() {
        return subscribeCycleForAlarm;
    }

    public void setSubscribeCycleForAlarm(Boolean subscribeCycleForAlarm) {
        this.subscribeCycleForAlarm = subscribeCycleForAlarm;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getSsrcCheck() {
        return ssrcCheck;
    }

    public void setSsrcCheck(Boolean ssrcCheck) {
        this.ssrcCheck = ssrcCheck;
    }

    public String getGeoCoordSys() {
        return geoCoordSys;
    }

    public void setGeoCoordSys(String geoCoordSys) {
        this.geoCoordSys = geoCoordSys;
    }

    public String getMediaServerId() {
        return mediaServerId;
    }

    public void setMediaServerId(String mediaServerId) {
        this.mediaServerId = mediaServerId;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getSdpIp() {
        return sdpIp;
    }

    public void setSdpIp(String sdpIp) {
        this.sdpIp = sdpIp;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAsMessageChannel() {
        return asMessageChannel;
    }

    public void setAsMessageChannel(Boolean asMessageChannel) {
        this.asMessageChannel = asMessageChannel;
    }

    public Long getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(Long heartBeatInterval) {
        this.heartBeatInterval = heartBeatInterval;
    }

    public Long getHeartBeatCount() {
        return heartBeatCount;
    }

    public void setHeartBeatCount(Long heartBeatCount) {
        this.heartBeatCount = heartBeatCount;
    }

    public Long getPositionCapability() {
        return positionCapability;
    }

    public void setPositionCapability(Long positionCapability) {
        this.positionCapability = positionCapability;
    }

    public Boolean getBroadcastPushAfterAck() {
        return broadcastPushAfterAck;
    }

    public void setBroadcastPushAfterAck(Boolean broadcastPushAfterAck) {
        this.broadcastPushAfterAck = broadcastPushAfterAck;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(String addressMap) {
        this.addressMap = addressMap;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getStreamType() {
        return streamType;
    }

    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

    public String getFfmpegCmdKey() {
        return ffmpegCmdKey;
    }

    public void setFfmpegCmdKey(String ffmpegCmdKey) {
        this.ffmpegCmdKey = ffmpegCmdKey;
    }

    public String getRtspType() {
        return rtspType;
    }

    public void setRtspType(String rtspType) {
        this.rtspType = rtspType;
    }

    public Boolean getEnableAudio() {
        return enableAudio;
    }

    public void setEnableAudio(Boolean enableAudio) {
        this.enableAudio = enableAudio;
    }

    public Boolean getEnableMp4() {
        return enableMp4;
    }

    public void setEnableMp4(Boolean enableMp4) {
        this.enableMp4 = enableMp4;
    }

    public Boolean getEnableRemoveNoneReader() {
        return enableRemoveNoneReader;
    }

    public void setEnableRemoveNoneReader(Boolean enableRemoveNoneReader) {
        this.enableRemoveNoneReader = enableRemoveNoneReader;
    }

    public Boolean getEnableDisableNoneReader() {
        return enableDisableNoneReader;
    }

    public void setEnableDisableNoneReader(Boolean enableDisableNoneReader) {
        this.enableDisableNoneReader = enableDisableNoneReader;
    }

    public String getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }

    public Boolean getStartOfflinePush() {
        return startOfflinePush;
    }

    public void setStartOfflinePush(Boolean startOfflinePush) {
        this.startOfflinePush = startOfflinePush;
    }

    public Boolean getPushing() {
        return pushing;
    }

    public void setPushing(Boolean pushing) {
        this.pushing = pushing;
    }

    public String getPushTime() {
        return pushTime;
    }

    public void setPushTime(String pushTime) {
        this.pushTime = pushTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOnvifAuthType() {
        return onvifAuthType;
    }

    public void setOnvifAuthType(String onvifAuthType) {
        this.onvifAuthType = onvifAuthType;
    }

    public Integer getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(Integer channelCount) {
        this.channelCount = channelCount;
    }
}
