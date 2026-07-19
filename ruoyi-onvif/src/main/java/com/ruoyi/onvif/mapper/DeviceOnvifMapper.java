package com.ruoyi.onvif.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.vo.DeviceReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备管理Mapper接口
 *
 * @author ruoyi
 * @date 2026-06-24
 */
public interface DeviceOnvifMapper extends BaseMapper<DeviceOnvif>
{
    /**
     * 查询设备管理列表（动态条件查询）
     */
    List<DeviceOnvif> selectDeviceOnvifList(DeviceOnvif deviceOnvif);

    /**
     * 查询设备管理列表（含通道数量子查询）
     */
    List<DeviceOnvif> selectDeviceOnvifListHasChannelCount(DeviceOnvif deviceOnvif);

    /**
     * 查询所有设备列表（含通道数量 + 关键词搜索）
     */
    List<DeviceOnvif> selectAllDeviceList(DeviceReqVo deviceReqVo);

    /**
     * 查询需要定时探测的ONVIF设备
     *
     * @param heartbeatThreshold 在线设备心跳超时阈值（yyyy-MM-dd HH:mm:ss）
     * @param offlineThreshold   离线设备重探阈值（yyyy-MM-dd HH:mm:ss）
     */
    List<DeviceOnvif> selectOnvifDevicesForProbe(@Param("heartbeatThreshold") String heartbeatThreshold,
                                                 @Param("offlineThreshold") String offlineThreshold);

    /**
     * 更新设备探测信息（keepalive + online）
     */
    int updateDeviceOnvifProbe(DeviceOnvif deviceOnvif);
}
