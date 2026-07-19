package com.ruoyi.onvif.service.impl;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceReqVo;
import com.ruoyi.onvif.mapper.DeviceOnvifMapper;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 设备管理Service业务层处理（开源版：ONVIF协议操作已移除）
 *
 * @author ruoyi
 * @date 2026-06-24
 */
@Service
public class DeviceOnvifServiceImpl implements IDeviceOnvifService
{
    private static final Logger log = LoggerFactory.getLogger(DeviceOnvifServiceImpl.class);
    private static final String NOT_SUPPORTED_MSG = "ONVIF核心功能未开源，请联系开发者获取完整版本";

    @Autowired
    private DeviceOnvifMapper deviceOnvifMapper;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    // ==================== 基础数据库 CRUD（保留） ====================

    @Override
    public DeviceOnvif selectDeviceOnvifById(Long id) {
        return deviceOnvifMapper.selectById(id);
    }

    @Override
    public DeviceOnvif selectDeviceOnvifByDeviceId(String deviceId) {
        return deviceOnvifMapper.selectOne(new QueryWrapper<DeviceOnvif>().eq("device_id", deviceId));
    }

    @Override
    public List<DeviceOnvif> selectDeviceOnvifList(DeviceOnvif deviceOnvif) {
        return deviceOnvifMapper.selectDeviceOnvifList(deviceOnvif);
    }

    @Override
    public int insertDeviceOnvif(DeviceOnvif deviceOnvif) {
        deviceOnvif.setCreateTime(DateUtils.getNowDate());
        return deviceOnvifMapper.insert(deviceOnvif);
    }

    @Override
    public int updateDeviceOnvif(DeviceOnvif deviceOnvif) {
        if (StringUtils.isNotEmpty(deviceOnvif.getName())) {
            deviceOnvif.setCustomName(deviceOnvif.getName());
            deviceOnvif.setName(null);
        }
        deviceOnvif.setUpdateTime(DateUtils.getNowDate());
        return deviceOnvifMapper.updateById(deviceOnvif);
    }

    @Override
    public int deleteDeviceOnvifByIds(Long[] ids) {
        for (Long id : ids) {
            deviceOnvifChannelService.deleteDeviceOnvifChannelByDataDeviceId(id);
        }
        return deviceOnvifMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public int deleteDeviceOnvifById(Long id) {
        return deviceOnvifMapper.deleteById(id);
    }

    @Override
    public List<DeviceOnvif> selectDeviceOnvifListHasChannelCount(DeviceOnvif deviceOnvif) {
        return deviceOnvifMapper.selectDeviceOnvifListHasChannelCount(deviceOnvif);
    }

    @Override
    public List<DeviceOnvif> selectAllDeviceList(DeviceReqVo deviceReqVo) {
        return deviceOnvifMapper.selectAllDeviceList(deviceReqVo);
    }

    @Override
    public List<DeviceOnvif> selectOnvifDevicesForProbe(String heartbeatThreshold, String offlineThreshold) {
        return Collections.emptyList();
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

    // ==================== ONVIF 协议操作（核心代码已移除，返回默认值） ====================

    @Override
    public int quickAdd(DeviceOnvif deviceOnvif) {
        log.warn(NOT_SUPPORTED_MSG);
        return 0;
    }

    @Override
    public List<OnvifUtils.DiscoveryInfo> wsDiscovery() {
        log.warn(NOT_SUPPORTED_MSG);
        return new ArrayList<>();
    }

    @Override
    public void refreshChannels(Long id) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public String getProfileTokenByChannelId(Long channelId) {
        log.warn(NOT_SUPPORTED_MSG);
        return null;
    }

    @Override
    public List<OnvifUtils.PresetInfo> getPresetListByChannelId(Long channelId) {
        log.warn(NOT_SUPPORTED_MSG);
        return new ArrayList<>();
    }

    @Override
    public void gotoPresetByChannelId(Long channelId, String presetToken) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public void removePresetByChannelId(Long channelId, String presetToken) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public void addPresetByChannelId(Long channelId, String presetToken) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public void continuousMoveByChannelId(Long channelId, double x, double y, double z, String command) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public void focusMoveByChannelId(Long channelId, float speed) {
        log.warn(NOT_SUPPORTED_MSG);
    }

    @Override
    public void irisSetByChannelId(Long channelId, float irisValue) {
        log.warn(NOT_SUPPORTED_MSG);
    }
}
