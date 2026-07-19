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
 * 设备通道对象 wvp_device_channel
 *
 * @author ruoyi
 * @date 2026-06-24
 */
@TableName("wvp_device_channel")
public class DeviceOnvifChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 通道ID */
    @Excel(name = "通道ID")
    private String deviceId;

    /** 通道名称 */
    @Excel(name = "通道名称")
    private String name;

    /** 设备厂商 */
    @Excel(name = "设备厂商")
    private String manufacturer;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String dbModel;

    /** 设备归属 */
    @Excel(name = "设备归属")
    private String owner;

    /** 行政区域 */
    @Excel(name = "行政区域")
    private String civilCode;

    /** 警区 */
    @Excel(name = "警区")
    private String block;

    /** 安装地址 */
    @Excel(name = "安装地址")
    private String address;

    /** 是否有子设备(必选)1有,0没有 */
    @Excel(name = "是否有子设备(必选)1有,0没有")
    private Long parental;

    /** 父节点ID */
    @Excel(name = "父节点ID")
    private String parentId;

    /** 信令安全模式 */
    @Excel(name = "信令安全模式")
    private Long safetyWay;

    /** 注册方式 */
    @Excel(name = "注册方式")
    private Long registerWay;

    /** 证书序列号 */
    @Excel(name = "证书序列号")
    private String certNum;

    /** 证书有效标识, */
    @Excel(name = "证书有效标识,")
    private Long certifiable;

    /** 无效原因码 */
    @Excel(name = "无效原因码")
    private Long errCode;

    /** 证书终止有效期 */
    @Excel(name = "证书终止有效期")
    private String endTime;

    /** 保密属性 */
    @Excel(name = "保密属性")
    private Long secrecy;

    /** 设备/系统IPv4/IPv6地址 */
    @Excel(name = "设备/系统IPv4/IPv6地址")
    private String ipAddress;

    /** 设备/系统端口 */
    @Excel(name = "设备/系统端口")
    private Long port;

    /** 密码 */
    @Excel(name = "密码")
    private String password;

    /** 设备状态 */
    @Excel(name = "设备状态")
    private String status;

    /** 经度 */
    @Excel(name = "经度")
    private Double longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private Double latitude;

    /** 摄像机类型 */
    @Excel(name = "摄像机类型")
    private Long ptzType;

    /** 摄像机位置类型扩展 */
    @Excel(name = "摄像机位置类型扩展")
    private Long positionType;

    /** 摄像机安装位置室外、室内属性 */
    @Excel(name = "摄像机安装位置室外、室内属性")
    private Long roomType;

    /** 用途属性， 1-治安、2-交通、3-重点 */
    @Excel(name = "用途属性， 1-治安、2-交通、3-重点")
    private Long useType;

    /** 摄像机补光属性 */
    @Excel(name = "摄像机补光属性")
    private Long supplyLightType;

    /** 摄像机监视方位 */
    @Excel(name = "摄像机监视方位")
    private Long directionType;

    /** 摄像机支持的分辨率 */
    @Excel(name = "摄像机支持的分辨率")
    private String resolution;

    /** 虚拟组织所属的业务分组ID */
    @Excel(name = "虚拟组织所属的业务分组ID")
    private String businessGroupId;

    /** 下载倍速(可选),可多值 */
    @Excel(name = "下载倍速(可选),可多值")
    private String downloadSpeed;

    /** 空域编码能力 */
    @Excel(name = "空域编码能力")
    private Long svcSpaceSupportMod;

    /** 时域编码能力 */
    @Excel(name = "时域编码能力")
    private Long svcTimeSupportMode;

    /** 子设备数 */
    @Excel(name = "子设备数")
    private Long subCount;

    /** 流唯一编号 */
    @Excel(name = "流唯一编号")
    private String streamId;

    /** 是否含有音频 */
    @Excel(name = "是否含有音频")
    private Boolean hasAudio;

    /** GPS的更新时间 */
    @Excel(name = "GPS的更新时间")
    private String gpsTime;

    /** 码流标识 */
    @Excel(name = "码流标识")
    private String streamIdentification;

    /** 通道类型 */
    @Excel(name = "通道类型")
    private Long channelType;

    /** 国标-编码 */
    @Excel(name = "国标-编码")
    private String gbDeviceId;

    /** 国标-名称 */
    @Excel(name = "国标-名称")
    private String gbName;

    /** 国标-设备厂商 */
    @Excel(name = "国标-设备厂商")
    private String gbManufacturer;

    /** 国标-设备型号 */
    @Excel(name = "国标-设备型号")
    private String gbModel;

    /** 国标-设备归属 */
    @Excel(name = "国标-设备归属")
    private String gbOwner;

    /** 国标-行政区域 */
    @Excel(name = "国标-行政区域")
    private String gbCivilCode;

    /** 国标-警区 */
    @Excel(name = "国标-警区")
    private String gbBlock;

    /** 国标-安装地址 */
    @Excel(name = "国标-安装地址")
    private String gbAddress;

    /** 国标-是否有子设备 */
    @Excel(name = "国标-是否有子设备")
    private Long gbParental;

    /** 国标-父节点ID */
    @Excel(name = "国标-父节点ID")
    private String gbParentId;

    /** 国标-信令安全模式 */
    @Excel(name = "国标-信令安全模式")
    private Long gbSafetyWay;

    /** 国标-注册方式 */
    @Excel(name = "国标-注册方式")
    private Long gbRegisterWay;

    /** 国标-证书序列号 */
    @Excel(name = "国标-证书序列号")
    private String gbCertNum;

    /** 国标-证书有效标识 */
    @Excel(name = "国标-证书有效标识")
    private Long gbCertifiable;

    /** 国标-无效原因码 */
    @Excel(name = "国标-无效原因码")
    private Long gbErrCode;

    /** 国标-证书终止有效期 */
    @Excel(name = "国标-证书终止有效期")
    private String gbEndTime;

    /** 国标-保密属性 */
    @Excel(name = "国标-保密属性")
    private Long gbSecrecy;

    /** 国标-设备/系统IPv4/IPv6地址 */
    @Excel(name = "国标-设备/系统IPv4/IPv6地址")
    private String gbIpAddress;

    /** 国标-设备/系统端口 */
    @Excel(name = "国标-设备/系统端口")
    private Long gbPort;

    /** 国标-设备口令 */
    @Excel(name = "国标-设备口令")
    private String gbPassword;

    /** 国标-设备状态 */
    @Excel(name = "国标-设备状态")
    private String gbStatus;

    /** 国标-经度  */
    @Excel(name = "国标-经度 ")
    private Double gbLongitude;

    /** 国标-纬度 */
    @Excel(name = "国标-纬度")
    private Double gbLatitude;

    /** 国标-虚拟组织所属的业务分组ID */
    @Excel(name = "国标-虚拟组织所属的业务分组ID")
    private String gbBusinessGroupId;

    /** 国标-摄像机结构类型 */
    @Excel(name = "国标-摄像机结构类型")
    private Long gbPtzType;

    /** 摄像机位置类型扩展 */
    @Excel(name = "摄像机位置类型扩展")
    private Long gbPositionType;

    /** 国标-摄像机安装位置 */
    @Excel(name = "国标-摄像机安装位置")
    private Long gbRoomType;

    /** 国标-用途属性 */
    @Excel(name = "国标-用途属性")
    private Long gbUseType;

    /** 国标-摄像机补光属性 */
    @Excel(name = "国标-摄像机补光属性")
    private Long gbSupplyLightType;

    /** 国标-摄像机监视方位 */
    @Excel(name = "国标-摄像机监视方位")
    private Long gbDirectionType;

    /** 国标-摄像机支持的分辨率 */
    @Excel(name = "国标-摄像机支持的分辨率")
    private String gbResolution;

    /** 国标-下载倍速 */
    @Excel(name = "国标-下载倍速")
    private String gbDownloadSpeed;

    /** 国标-空域编码能力 */
    @Excel(name = "国标-空域编码能力")
    private Long gbSvcSpaceSupportMod;

    /** 国标-时域编码能力 */
    @Excel(name = "国标-时域编码能力")
    private Long gbSvcTimeSupportMode;

    /** 二进制保存的录制计划 */
    @Excel(name = "二进制保存的录制计划")
    private Long recordPlanId;

    /** 关联的数据类型 */
    @Excel(name = "关联的数据类型")
    private Long dataType;

    /** 关联的设备ID */
    @Excel(name = "关联的设备ID")
    private Long dataDeviceId;

    /** 通道号 */
    @Excel(name = "通道号")
    private String channelNo;

    /** 推流地址 */
    @Excel(name = "推流地址")
    private String srcUrl;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCivilCode() {
        return civilCode;
    }

    public void setCivilCode(String civilCode) {
        this.civilCode = civilCode;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getParental() {
        return parental;
    }

    public void setParental(Long parental) {
        this.parental = parental;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long getSafetyWay() {
        return safetyWay;
    }

    public void setSafetyWay(Long safetyWay) {
        this.safetyWay = safetyWay;
    }

    public Long getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(Long registerWay) {
        this.registerWay = registerWay;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public Long getCertifiable() {
        return certifiable;
    }

    public void setCertifiable(Long certifiable) {
        this.certifiable = certifiable;
    }

    public Long getErrCode() {
        return errCode;
    }

    public void setErrCode(Long errCode) {
        this.errCode = errCode;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(Long secrecy) {
        this.secrecy = secrecy;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Long getPtzType() {
        return ptzType;
    }

    public void setPtzType(Long ptzType) {
        this.ptzType = ptzType;
    }

    public Long getPositionType() {
        return positionType;
    }

    public void setPositionType(Long positionType) {
        this.positionType = positionType;
    }

    public Long getRoomType() {
        return roomType;
    }

    public void setRoomType(Long roomType) {
        this.roomType = roomType;
    }

    public Long getUseType() {
        return useType;
    }

    public void setUseType(Long useType) {
        this.useType = useType;
    }

    public Long getSupplyLightType() {
        return supplyLightType;
    }

    public void setSupplyLightType(Long supplyLightType) {
        this.supplyLightType = supplyLightType;
    }

    public Long getDirectionType() {
        return directionType;
    }

    public void setDirectionType(Long directionType) {
        this.directionType = directionType;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getBusinessGroupId() {
        return businessGroupId;
    }

    public void setBusinessGroupId(String businessGroupId) {
        this.businessGroupId = businessGroupId;
    }

    public String getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(String downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public Long getSvcSpaceSupportMod() {
        return svcSpaceSupportMod;
    }

    public void setSvcSpaceSupportMod(Long svcSpaceSupportMod) {
        this.svcSpaceSupportMod = svcSpaceSupportMod;
    }

    public Long getSvcTimeSupportMode() {
        return svcTimeSupportMode;
    }

    public void setSvcTimeSupportMode(Long svcTimeSupportMode) {
        this.svcTimeSupportMode = svcTimeSupportMode;
    }

    public Long getSubCount() {
        return subCount;
    }

    public void setSubCount(Long subCount) {
        this.subCount = subCount;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getStreamIdentification() {
        return streamIdentification;
    }

    public void setStreamIdentification(String streamIdentification) {
        this.streamIdentification = streamIdentification;
    }

    public Long getChannelType() {
        return channelType;
    }

    public void setChannelType(Long channelType) {
        this.channelType = channelType;
    }

    public String getGbDeviceId() {
        return gbDeviceId;
    }

    public void setGbDeviceId(String gbDeviceId) {
        this.gbDeviceId = gbDeviceId;
    }

    public String getGbName() {
        return gbName;
    }

    public void setGbName(String gbName) {
        this.gbName = gbName;
    }

    public String getGbManufacturer() {
        return gbManufacturer;
    }

    public void setGbManufacturer(String gbManufacturer) {
        this.gbManufacturer = gbManufacturer;
    }

    public String getGbModel() {
        return gbModel;
    }

    public void setGbModel(String gbModel) {
        this.gbModel = gbModel;
    }

    public String getGbOwner() {
        return gbOwner;
    }

    public void setGbOwner(String gbOwner) {
        this.gbOwner = gbOwner;
    }

    public String getGbCivilCode() {
        return gbCivilCode;
    }

    public void setGbCivilCode(String gbCivilCode) {
        this.gbCivilCode = gbCivilCode;
    }

    public String getGbBlock() {
        return gbBlock;
    }

    public void setGbBlock(String gbBlock) {
        this.gbBlock = gbBlock;
    }

    public String getGbAddress() {
        return gbAddress;
    }

    public void setGbAddress(String gbAddress) {
        this.gbAddress = gbAddress;
    }

    public Long getGbParental() {
        return gbParental;
    }

    public void setGbParental(Long gbParental) {
        this.gbParental = gbParental;
    }

    public String getGbParentId() {
        return gbParentId;
    }

    public void setGbParentId(String gbParentId) {
        this.gbParentId = gbParentId;
    }

    public Long getGbSafetyWay() {
        return gbSafetyWay;
    }

    public void setGbSafetyWay(Long gbSafetyWay) {
        this.gbSafetyWay = gbSafetyWay;
    }

    public Long getGbRegisterWay() {
        return gbRegisterWay;
    }

    public void setGbRegisterWay(Long gbRegisterWay) {
        this.gbRegisterWay = gbRegisterWay;
    }

    public String getGbCertNum() {
        return gbCertNum;
    }

    public void setGbCertNum(String gbCertNum) {
        this.gbCertNum = gbCertNum;
    }

    public Long getGbCertifiable() {
        return gbCertifiable;
    }

    public void setGbCertifiable(Long gbCertifiable) {
        this.gbCertifiable = gbCertifiable;
    }

    public Long getGbErrCode() {
        return gbErrCode;
    }

    public void setGbErrCode(Long gbErrCode) {
        this.gbErrCode = gbErrCode;
    }

    public String getGbEndTime() {
        return gbEndTime;
    }

    public void setGbEndTime(String gbEndTime) {
        this.gbEndTime = gbEndTime;
    }

    public Long getGbSecrecy() {
        return gbSecrecy;
    }

    public void setGbSecrecy(Long gbSecrecy) {
        this.gbSecrecy = gbSecrecy;
    }

    public String getGbIpAddress() {
        return gbIpAddress;
    }

    public void setGbIpAddress(String gbIpAddress) {
        this.gbIpAddress = gbIpAddress;
    }

    public Long getGbPort() {
        return gbPort;
    }

    public void setGbPort(Long gbPort) {
        this.gbPort = gbPort;
    }

    public String getGbPassword() {
        return gbPassword;
    }

    public void setGbPassword(String gbPassword) {
        this.gbPassword = gbPassword;
    }

    public String getGbStatus() {
        return gbStatus;
    }

    public void setGbStatus(String gbStatus) {
        this.gbStatus = gbStatus;
    }

    public Double getGbLongitude() {
        return gbLongitude;
    }

    public void setGbLongitude(Double gbLongitude) {
        this.gbLongitude = gbLongitude;
    }

    public Double getGbLatitude() {
        return gbLatitude;
    }

    public void setGbLatitude(Double gbLatitude) {
        this.gbLatitude = gbLatitude;
    }

    public String getGbBusinessGroupId() {
        return gbBusinessGroupId;
    }

    public void setGbBusinessGroupId(String gbBusinessGroupId) {
        this.gbBusinessGroupId = gbBusinessGroupId;
    }

    public Long getGbPtzType() {
        return gbPtzType;
    }

    public void setGbPtzType(Long gbPtzType) {
        this.gbPtzType = gbPtzType;
    }

    public Long getGbPositionType() {
        return gbPositionType;
    }

    public void setGbPositionType(Long gbPositionType) {
        this.gbPositionType = gbPositionType;
    }

    public Long getGbRoomType() {
        return gbRoomType;
    }

    public void setGbRoomType(Long gbRoomType) {
        this.gbRoomType = gbRoomType;
    }

    public Long getGbUseType() {
        return gbUseType;
    }

    public void setGbUseType(Long gbUseType) {
        this.gbUseType = gbUseType;
    }

    public Long getGbSupplyLightType() {
        return gbSupplyLightType;
    }

    public void setGbSupplyLightType(Long gbSupplyLightType) {
        this.gbSupplyLightType = gbSupplyLightType;
    }

    public Long getGbDirectionType() {
        return gbDirectionType;
    }

    public void setGbDirectionType(Long gbDirectionType) {
        this.gbDirectionType = gbDirectionType;
    }

    public String getGbResolution() {
        return gbResolution;
    }

    public void setGbResolution(String gbResolution) {
        this.gbResolution = gbResolution;
    }

    public String getGbDownloadSpeed() {
        return gbDownloadSpeed;
    }

    public void setGbDownloadSpeed(String gbDownloadSpeed) {
        this.gbDownloadSpeed = gbDownloadSpeed;
    }

    public Long getGbSvcSpaceSupportMod() {
        return gbSvcSpaceSupportMod;
    }

    public void setGbSvcSpaceSupportMod(Long gbSvcSpaceSupportMod) {
        this.gbSvcSpaceSupportMod = gbSvcSpaceSupportMod;
    }

    public Long getGbSvcTimeSupportMode() {
        return gbSvcTimeSupportMode;
    }

    public void setGbSvcTimeSupportMode(Long gbSvcTimeSupportMode) {
        this.gbSvcTimeSupportMode = gbSvcTimeSupportMode;
    }

    public Long getRecordPlanId() {
        return recordPlanId;
    }

    public void setRecordPlanId(Long recordPlanId) {
        this.recordPlanId = recordPlanId;
    }

    public Long getDataType() {
        return dataType;
    }

    public void setDataType(Long dataType) {
        this.dataType = dataType;
    }

    public Long getDataDeviceId() {
        return dataDeviceId;
    }

    public void setDataDeviceId(Long dataDeviceId) {
        this.dataDeviceId = dataDeviceId;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }
}
