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
     * 查询所有设备列表（含通道数量 + 关键词搜索 + 分配设备过滤）
     * @param deviceReqVo 查询条件
     * @param deviceIds   用户分配的设备ID列表（sys_user_channel.device_id），null或空表示不过滤
     */
    List<DeviceOnvif> selectAllDeviceListFiltered(@Param("reqVo") DeviceReqVo deviceReqVo,
                                                   @Param("deviceIds") List<String> deviceIds);

    /**
     * 更新设备探测信息（keepalive + online）
     */
    int updateDeviceOnvifProbe(DeviceOnvif deviceOnvif);
}
