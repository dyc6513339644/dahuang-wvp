package com.ruoyi.onvif.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceChannelReqVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备通道Mapper接口
 * @author ruoyi
 * @date 2026-06-24
 */
public interface DeviceOnvifChannelMapper extends BaseMapper<DeviceOnvifChannel>
{
    /**
     * 查询设备通道列表（动态条件查询）
     */
    List<DeviceOnvifChannel> selectDeviceOnvifChannelList(DeviceOnvifChannel deviceOnvifChannel);

    /**
     * 根据关联设备ID删除通道
     */
    int deleteDeviceOnvifChannelByDataDeviceId(Long dataDeviceId);

    /**
     * 按 dataDeviceId 批量更新通道状态
     */
    int updateStatusByDataDeviceId(@Param("dataDeviceId") Long dataDeviceId,
                                   @Param("status") String status);

    /**
     * 查询设备通道列表（含设备信息）
     */
    List<DeviceOnvifChannel> selectDeviceChannelList(DeviceChannelReqVo deviceChannelReqVo);
}
