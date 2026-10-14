package com.ruoyi.onvif.service.impl;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceReqVo;
import com.ruoyi.onvif.event.DeviceOnvifEvent;
import com.ruoyi.onvif.exception.OnvifException;
import com.ruoyi.onvif.mapper.DeviceOnvifMapper;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;


/**
 * 设备管理Service业务层处理
 *
 * @author ruoyi
 * @date 2026-06-24
 */
@Service
public class DeviceOnvifServiceImpl implements IDeviceOnvifService, ApplicationEventPublisherAware
{
    private static final Logger log = LoggerFactory.getLogger(DeviceOnvifServiceImpl.class);

    @Autowired
    private DeviceOnvifMapper deviceOnvifMapper;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    // 事件发布器
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 查询设备管理
     *
     * @param id 设备管理主键
     * @return 设备管理
     */
    @Override
    public DeviceOnvif selectDeviceOnvifById(Long id)
    {
        return deviceOnvifMapper.selectById(id);
    }

    @Override
    public DeviceOnvif selectDeviceOnvifByDeviceId(String deviceId) {
        return deviceOnvifMapper.selectOne(new QueryWrapper<DeviceOnvif>().eq("device_id", deviceId));
    }

    /**
     * 查询设备管理列表
     *
     * @param deviceOnvif 设备管理
     * @return 设备管理
     */
    @Override
    public List<DeviceOnvif> selectDeviceOnvifList(DeviceOnvif deviceOnvif)
    {
        return deviceOnvifMapper.selectDeviceOnvifList(deviceOnvif);
    }

    /**
     * 新增设备管理
     *
     * @param deviceOnvif 设备管理
     * @return 结果
     */
    @Override
    public int insertDeviceOnvif(DeviceOnvif deviceOnvif)
    {
        deviceOnvif.setCreateTime(DateUtils.getNowDate());
        return deviceOnvifMapper.insert(deviceOnvif);
    }

    @Override
    public int quickAdd(DeviceOnvif deviceOnvif) {
        deviceOnvif.setDeviceId("onvif" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        deviceOnvif.setMediaServerId("auto");
        deviceOnvif.setHeartBeatInterval(30L);  // 探测间隔 30s
        deviceOnvif.setHeartBeatCount(2L);      // 连续 2 次失败判离线
        deviceOnvif.setKeepaliveTime(DateUtils.getTime());
        deviceOnvif.setProtocolType("ONVIF");
        deviceOnvif.setHostAddress(deviceOnvif.getIp()+":"+deviceOnvif.getPort());
        int result =insertDeviceOnvif(deviceOnvif);
        //发布事件，onvif 设备添加成功
        applicationEventPublisher.publishEvent(new DeviceOnvifEvent(this, deviceOnvif.getId(), DeviceOnvifEvent.EventType.ADD));
        return result;
    }

    /**
     * 修改设备管理
     *
     * @param deviceOnvif 设备管理
     * @return 结果
     */
    @Override
    public int updateDeviceOnvif(DeviceOnvif deviceOnvif)
    {
        if(StringUtils.isNotEmpty(deviceOnvif.getName())) {
            deviceOnvif.setCustomName(deviceOnvif.getName());
            deviceOnvif.setName(null);
        }
        deviceOnvif.setUpdateTime(DateUtils.getNowDate());
        return deviceOnvifMapper.updateById(deviceOnvif);
    }

    /**
     * 批量删除设备管理
     *
     * @param ids 需要删除的设备管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOnvifByIds(Long[] ids)
    {
        // 先删除每个设备关联的通道
        for (Long id : ids) {
            deviceOnvifChannelService.deleteDeviceOnvifChannelByDataDeviceId(id);
        }
        return deviceOnvifMapper.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 删除设备管理信息
     *
     * @param id 设备管理主键
     * @return 结果
     */
    @Override
    public int deleteDeviceOnvifById(Long id)
    {
        return deviceOnvifMapper.deleteById(id);
    }

    @Override
    public List<OnvifUtils.DiscoveryInfo> wsDiscovery() {

        try {
           return   OnvifUtils.wsDiscovery(30);
        } catch (Exception e) {
            log.warn("[WSDiscovery] 等待设备发现异常");
        }
        return new ArrayList<>();
    }

    @Override
    public void refreshChannels(Long id) {
        applicationEventPublisher.publishEvent(new DeviceOnvifEvent(this,id, DeviceOnvifEvent.EventType.REFRESH_CHANNELS));
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public List<DeviceOnvif> selectOnvifDevicesForProbe(String heartbeatThreshold, String offlineThreshold) {
        return deviceOnvifMapper.selectOnvifDevicesForProbe(heartbeatThreshold, offlineThreshold);
    }

    @Override
    public boolean probeDeviceOnline(DeviceOnvif device) {
        String host = device.getIp();
        int port = device.getPort() != null ? device.getPort().intValue() : 80;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 3000);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    @Override
    public int updateDeviceOnvifProbe(DeviceOnvif device) {
        return deviceOnvifMapper.updateDeviceOnvifProbe(device);
    }

    @Override
    public List<DeviceOnvif> selectDeviceOnvifListHasChannelCount(DeviceOnvif deviceOnvif) {
        return deviceOnvifMapper.selectDeviceOnvifListHasChannelCount(deviceOnvif);
    }

    // ==================== 云台控制 ====================

    @Override
    public String getProfileTokenByChannelId(Long channelId) {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
        if (channel == null) {
            throw new OnvifException("通道不存在: " + channelId);
        }
        return channel.getChannelNo();
    }


    @Override
    public List<OnvifUtils.PresetInfo> getPresetListByChannelId(Long channelId) {
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
            return  OnvifUtils.getPresets(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo());
        } catch (Exception e) {
            log.error("预置位查询失败", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public void gotoPresetByChannelId(Long channelId, String presetToken) {
        try {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
        DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
        OnvifUtils.gotoPreset(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), presetToken);
        } catch (Exception e) {
            log.error("调用预置位失败", e.getMessage());
        }
    }

    @Override
    public void removePresetByChannelId(Long channelId, String presetToken) {
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
            OnvifUtils.removePreset(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), presetToken);
        } catch (Exception e) {
            log.error("删除预置位失败", e.getMessage());
        }
    }

    @Override
    public void addPresetByChannelId(Long channelId, String presetToken) {
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
            OnvifUtils.setPreset(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), "预置位"+presetToken,presetToken);
        } catch (Exception e) {
            log.error("添加预置位失败", e.getMessage());
        }
    }

    @Override
    public void continuousMoveByChannelId(Long channelId, double x, double y, double z,String command) {
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
            if (x == 0 && y == 0 && z == 0) {
                OnvifUtils.ptzStop(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo());
            }else {
                double speed=x;
                if(y>0){
                    speed=y;
                }
                if(z>0){
                    speed=z;
                }
                OnvifUtils.ptzMove(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), command, speed);
            }
        } catch (Exception e) {
            log.error("云台控制失败", e.getMessage());
        }
    }

    @Override
    public void focusMoveByChannelId(Long channelId, float speed) {
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif=deviceOnvifMapper.selectById(channel.getDataDeviceId());
            if (speed == 0) {
                OnvifUtils.focusStop(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo());
            }else {
                OnvifUtils.focusMove(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), speed>0?1:-1);
            }
        } catch (Exception e) {
            log.error("光圈聚焦失败", e.getMessage());
        }
    }






    @Override
    public void  irisSetByChannelId(Long channelId, float irisValue) {
        try {
            if (irisValue < 0 || irisValue > 1) {
                return;
            }
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelById(channelId);
            DeviceOnvif deviceOnvif = deviceOnvifMapper.selectById(channel.getDataDeviceId());
            OnvifUtils.irisSet(deviceOnvif.getIp(), deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), channel.getChannelNo(), irisValue);
        } catch (Exception e) {
            log.error("光圈调节失败", e.getMessage());
        }
    }

    @Override
    public List<DeviceOnvif> selectAllDeviceList(DeviceReqVo deviceReqVo) {
       return  deviceOnvifMapper.selectAllDeviceList(deviceReqVo);
    }

    @Override
    public List<DeviceOnvif> selectAllDeviceListFiltered(DeviceReqVo deviceReqVo, List<String> deviceIds) {
        return deviceOnvifMapper.selectAllDeviceListFiltered(deviceReqVo, deviceIds);
    }
}
