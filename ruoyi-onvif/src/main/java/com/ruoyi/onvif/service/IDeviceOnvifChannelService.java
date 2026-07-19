package com.ruoyi.onvif.service;

import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceChannelReqVo;

import java.util.List;


/**
 * 设备通道Service接口
 *
 * @author ruoyi
 * @date 2026-06-24
 */
public interface IDeviceOnvifChannelService
{
    /**
     * 查询设备通道
     *
     * @param id 设备通道主键
     * @return 设备通道
     */
    public DeviceOnvifChannel selectDeviceOnvifChannelById(Long id);


    /**
     * 查询设备通道
     *
     * @param deviceId 设备编码
     * @return 设备通道
     */
    public DeviceOnvifChannel selectDeviceOnvifChannelByDeviceId(String deviceId);

    /**
     * 查询设备通道列表
     *
     * @param deviceOnvifChannel 设备通道
     * @return 设备通道集合
     */
    public List<DeviceOnvifChannel> selectDeviceOnvifChannelList(DeviceOnvifChannel deviceOnvifChannel);

    /**
     * 新增设备通道
     *
     * @param deviceOnvifChannel 设备通道
     * @return 结果
     */
    public int insertDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel);

    /**
     * 修改设备通道
     *
     * @param deviceOnvifChannel 设备通道
     * @return 结果
     */
    public int updateDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel);

    /**
     * 批量删除设备通道
     *
     * @param ids 需要删除的设备通道主键集合
     * @return 结果
     */
    public int deleteDeviceOnvifChannelByIds(Long[] ids);

    /**
     * 删除设备通道信息
     *
     * @param id 设备通道主键
     * @return 结果
     */
    public int deleteDeviceOnvifChannelById(Long id);

    /**
     * 根据关联设备ID删除通道
     *
     * @param dataDeviceId 关联设备ID
     * @return 结果
     */
    public int deleteDeviceOnvifChannelByDataDeviceId(Long dataDeviceId);

    /**
     * 按 dataDeviceId 批量更新通道状态（用于探测任务联动）
     */
    public int updateStatusByDataDeviceId(Long dataDeviceId, String status);


    /**
     *查询设备通道
     * @param deviceChannelReqVo
     * @return
     */
    public List<DeviceOnvifChannel> selectDeviceChannelList(DeviceChannelReqVo deviceChannelReqVo);
}
