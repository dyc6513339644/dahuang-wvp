package com.ruoyi.onvif.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
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

    @Override
    public DeviceOnvifChannel selectDeviceOnvifChannelById(Long id) {
        return deviceOnvifChannelMapper.selectById(id);
    }

    @Override
    public DeviceOnvifChannel selectDeviceOnvifChannelByDeviceId(String deviceId) {
        return deviceOnvifChannelMapper.selectOne(new QueryWrapper<DeviceOnvifChannel>().eq("device_id", deviceId));
    }

    @Override
    public List<DeviceOnvifChannel> selectDeviceOnvifChannelList(DeviceOnvifChannel deviceOnvifChannel) {
        return deviceOnvifChannelMapper.selectDeviceOnvifChannelList(deviceOnvifChannel);
    }

    @Override
    public int insertDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel) {
        deviceOnvifChannel.setCreateTime(DateUtils.getNowDate());
        return deviceOnvifChannelMapper.insert(deviceOnvifChannel);
    }

    @Override
    public int updateDeviceOnvifChannel(DeviceOnvifChannel deviceOnvifChannel) {
        deviceOnvifChannel.setUpdateTime(DateUtils.getNowDate());
        return deviceOnvifChannelMapper.updateById(deviceOnvifChannel);
    }

    @Override
    public int deleteDeviceOnvifChannelByIds(Long[] ids) {
        return deviceOnvifChannelMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int deleteDeviceOnvifChannelById(Long id) {
        return deviceOnvifChannelMapper.deleteById(id);
    }

    @Override
    public int deleteDeviceOnvifChannelByDataDeviceId(Long dataDeviceId) {
        return deviceOnvifChannelMapper.deleteDeviceOnvifChannelByDataDeviceId(dataDeviceId);
    }

    @Override
    public int updateStatusByDataDeviceId(Long dataDeviceId, String status) {
        return deviceOnvifChannelMapper.updateStatusByDataDeviceId(dataDeviceId, status);
    }

    @Override
    public List<DeviceOnvifChannel> selectDeviceChannelList(DeviceChannelReqVo deviceChannelReqVo) {
        return deviceOnvifChannelMapper.selectDeviceChannelList(deviceChannelReqVo);
    }
}
