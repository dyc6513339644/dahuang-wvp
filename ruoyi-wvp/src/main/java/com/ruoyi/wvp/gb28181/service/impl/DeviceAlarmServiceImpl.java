package com.ruoyi.wvp.gb28181.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.wvp.gb28181.bean.DeviceAlarm;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.service.IDeviceChannelService;
import com.ruoyi.wvp.mapper.DeviceAlarmMapper;
import com.ruoyi.wvp.gb28181.service.IDeviceAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@DS("master")
public class DeviceAlarmServiceImpl implements IDeviceAlarmService {

    @Autowired
    private DeviceAlarmMapper deviceAlarmMapper;

    @Autowired
    private IDeviceChannelService deviceChannelService;

    @Override
    public PageInfo<DeviceAlarm> getAllAlarm(int page, int count, String deviceId, String channelId, String alarmPriority, String alarmMethod, String alarmType, String startTime, String endTime) {
        PageHelper.startPage(page, count);
        List<DeviceAlarm> all = deviceAlarmMapper.query(deviceId, channelId, alarmPriority, alarmMethod, alarmType, startTime, endTime);
        return new PageInfo<>(all);
    }

    @Override
    public void add(DeviceAlarm deviceAlarm) {
        deviceAlarmMapper.add(deviceAlarm);
    }

    /**
     * 异步回填报警通道名称：报警报文本身不含通道名，落库后异步查通道表补齐。
     * getOne(deviceId, channelId) 内部按 coalesce(gb_device_id, device_id) 匹配通道编码，
     * 返回的 name 已是 coalesce(gb_name, name) 通道名。
     */
    @Async("taskExecutor")
    @Override
    public void asyncFillChannelName(DeviceAlarm deviceAlarm) {
        if (deviceAlarm == null || deviceAlarm.getId() == null) {
            return;
        }
        try {
            DeviceChannel deviceChannel = deviceChannelService.getOne(deviceAlarm.getDeviceId(), deviceAlarm.getChannelId());
            if (deviceChannel != null && deviceChannel.getName() != null && !deviceChannel.getName().isEmpty()) {
                deviceAlarmMapper.updateChannelName(deviceAlarm.getId(), deviceChannel.getName());
            }
        } catch (Exception e) {
            log.warn("[报警通道名称回填] 失败, alarmId={}, deviceId={}, channelId={}",
                    deviceAlarm.getId(), deviceAlarm.getDeviceId(), deviceAlarm.getChannelId(), e);
        }
    }

    @Override
    public int clearAlarmBeforeTime(Integer id, List<String> deviceIdList, String time) {
        return deviceAlarmMapper.clearAlarmBeforeTime(id, deviceIdList, time);
    }
}
