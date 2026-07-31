package com.ruoyi.onvif.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceChannelReqVo;
import com.ruoyi.onvif.mapper.DeviceOnvifChannelMapper;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 设备通道Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-24
 */
@Service
public class DeviceOnvifChannelServiceImpl implements IDeviceOnvifChannelService
{
    @Autowired
    private DeviceOnvifChannelMapper deviceOnvifChannelMapper;

    /**
     * 查询设备通道
     *
     * @param id 设备通道主键
     * @return 设备通道
     */
    @Override
    public DeviceOnvifChannel selectDeviceOnvifChannelById(Long id)
    {
        return deviceOnvifChannelMapper.selectById(id);
    }

    @Override
    public DeviceOnvifChannel selectDeviceOnvifChannelByDeviceId(String deviceId) {
        return deviceOnvifChannelMapper.selectOne(new QueryWrapper<DeviceOnvifChannel>().eq("device_id", deviceId));
    }

    /**
     * 查询设备通道列表
     *
     * @param deviceOnvifChannel 设备通道
     * @return 设备通道
     */
    @Override
    public List<DeviceOnvifChannel> selectDeviceOnvifChannelList(DeviceOnvifChannel deviceOnvifChannel)
    {
        return deviceOnvifChannelMapper.selectDeviceOnvifChannelList(deviceOnvifChannel);
    }

    /**
     * 新增设备通道
     *
     * @param deviceOnvifChannel 设备通道
     * @return 结果
     */
    @Override
    public int insertDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel)
    {
        deviceOnvifChannel.setCreateTime(DateUtils.getNowDate());
        return deviceOnvifChannelMapper.insert(deviceOnvifChannel);
    }

    /**
     * 修改设备通道
     *
     * @param deviceOnvifChannel 设备通道
     * @return 结果
     */
    @Override
    public int updateDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel)
    {
        deviceOnvifChannel.setUpdateTime(DateUtils.getNowDate());
        return deviceOnvifChannelMapper.updateById(deviceOnvifChannel);
    }

    /**
     * 批量删除设备通道
     *
     * @param ids 需要删除的设备通道主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOnvifChannelByIds(Long[] ids)
    {
        return deviceOnvifChannelMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除设备通道信息
     *
     * @param id 设备通道主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOnvifChannelById(Long id)
    {
        return deviceOnvifChannelMapper.deleteById(id);
    }

    /**
     * 根据关联设备ID删除通道
     *
     * @param dataDeviceId 关联设备ID
     * @return 结果
     */
    @Override
    public int deleteDeviceOnvifChannelByDataDeviceId(Long dataDeviceId)
    {
        return deviceOnvifChannelMapper.deleteDeviceOnvifChannelByDataDeviceId(dataDeviceId);
    }

    @Override
    public int updateStatusByDataDeviceId(Long dataDeviceId, String status)
    {
        return deviceOnvifChannelMapper.updateStatusByDataDeviceId(dataDeviceId, status);
    }

    @Override
    public List<DeviceOnvifChannel> selectDeviceChannelList(DeviceChannelReqVo deviceChannelReqVo) {
        return deviceOnvifChannelMapper.selectDeviceChannelList(deviceChannelReqVo);
    }

    @Override
    public List<DeviceOnvifChannel> selectDeviceChannelListFiltered(DeviceChannelReqVo deviceChannelReqVo, List<String> deviceIds) {
        return deviceOnvifChannelMapper.selectDeviceChannelListFiltered(deviceChannelReqVo, deviceIds);
    }
}
