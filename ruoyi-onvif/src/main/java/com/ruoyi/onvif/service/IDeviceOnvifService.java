package com.ruoyi.onvif.service;

import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.vo.DeviceReqVo;
import com.ruoyi.onvif.utils.OnvifUtils;

import java.util.List;


/**
 * 设备管理Service接口
 *
 * @author ruoyi
 * @date 2026-06-24
 */
public interface IDeviceOnvifService
{
    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    public DeviceOnvif selectDeviceOnvifById(Long id);


   /**
     * 根据deviceId 查询设备
     */
    public DeviceOnvif selectDeviceOnvifByDeviceId(String deviceId);

    /**
     * 查询设备管理列表
     *
     * @param deviceOnvif 设备管理
     * @return 设备管理集合
     */
    public List<DeviceOnvif> selectDeviceOnvifList(DeviceOnvif deviceOnvif);


    /**
     * 新增设备管理
     *
     * @param deviceOnvif 设备管理
     * @return 结果
     */
    public int insertDeviceOnvif(DeviceOnvif deviceOnvif);

    public int quickAdd(DeviceOnvif deviceOnvif);

    /**
     * 修改设备管理
     *
     * @param deviceOnvif 设备管理
     * @return 结果
     */
    public int updateDeviceOnvif(DeviceOnvif deviceOnvif);

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键集合
     * @return 结果
     */
    public int deleteDeviceOnvifByIds(Long[] ids);

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    public int deleteDeviceOnvifById(Long id);

    /**
     * 设备发现接口
     */
    List<OnvifUtils.DiscoveryInfo> wsDiscovery() ;


    void refreshChannels(Long id);

    /**
     * 查询需要定时探测的ONVIF设备
     *
     * @param heartbeatThreshold 在线设备心跳超时阈值（yyyy-MM-dd HH:mm:ss）
     * @param offlineThreshold   离线设备重探阈值（yyyy-MM-dd HH:mm:ss）
     */
    List<DeviceOnvif> selectOnvifDevicesForProbe(String heartbeatThreshold, String offlineThreshold);

    /**
     * 探测设备在线状态（TCP连接检测）
     */
    boolean probeDeviceOnline(DeviceOnvif device);

    /**
     * 查询设备管理列表（含通道数量）
     */
    public List<DeviceOnvif> selectDeviceOnvifListHasChannelCount(DeviceOnvif deviceOnvif);

    /**
     * 更新设备探测信息（keepalive + online）
     */
    int updateDeviceOnvifProbe(DeviceOnvif device);

    // ==================== 云台控制 ====================

    /**
     * 根据通道ID（数据库主键）解析 profileToken
     */
    String getProfileTokenByChannelId(Long channelId);

    /**
     * 获取预置点列表（通过 channelId）
     */
    List<OnvifUtils.PresetInfo> getPresetListByChannelId(Long channelId);

    /**
     * 跳转到预置点（通过 channelId）
     */
    void gotoPresetByChannelId(Long channelId, String presetToken);

    /**
     * 删除预置点（通过 channelId）
     */
    void removePresetByChannelId(Long channelId, String presetToken);

    /**
     * 添加预置点（通过 channelId）
     */
    void addPresetByChannelId(Long channelId, String presetToken);

    /**
     * 云台连续移动（通过 channelId，x=y=z=0 时自动 stop）
     */
    void continuousMoveByChannelId(Long channelId, double x, double y, double z,String command);

    // ==================== 聚焦 / 光圈 ====================

    /**
     * 聚焦连续移动（speed>0 远焦、speed<0 近焦、speed=0 停止聚焦）
     *
     * @param channelId 通道数据库ID
     * @param speed     聚焦速度（-1.0~1.0），0 时调用 focusStop()
     */
    void focusMoveByChannelId(Long channelId, float speed);

    /**
     * 光圈绝对值设置（0.0~1.0）
     *
     * @param channelId  通道数据库ID
     * @param irisValue  光圈值（0.0 = 最小，1.0 = 最大）
     */
    void irisSetByChannelId(Long channelId, float irisValue);


   /**
     * 查询所有设备列表
     */
    public List<DeviceOnvif> selectAllDeviceList(DeviceReqVo deviceReqVo);

    /**
     * 查询所有设备列表（含分配设备过滤）
     * @param deviceReqVo 查询条件
     * @param deviceIds   用户分配的设备ID列表，null或空表示不过滤
     */
    public List<DeviceOnvif> selectAllDeviceListFiltered(DeviceReqVo deviceReqVo, List<String> deviceIds);



}
