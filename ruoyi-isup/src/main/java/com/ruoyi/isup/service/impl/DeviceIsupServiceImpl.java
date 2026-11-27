package com.ruoyi.isup.service.impl;

import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.isup.domain.*;
import com.ruoyi.isup.service.IDeviceIsupService;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海康 ISUP 设备服务实现（开源版：已移除原生 SDK 与业务逻辑）。
 * <p>
 * 开源版不随包分发海康 ISUP / Ehome 原生库，也不监听 CMS/SMS/报警端口，因此本实现只保留
 * {@link IDeviceIsupService} 的方法契约，全部返回空值，用于保证上层（统一播放控制器、
 * ISUP 设备管理接口）能够正常编译与启动。
 * <p>
 * 预留扩展点：如需在开源版恢复 ISUP 能力，把 PRO 版的 {@code sdk}、{@code handler}、
 * {@code manager}、{@code config} 包与原生库目录一并引入，并用完整实现替换本类即可，
 * 上层代码无需改动。
 */
@Slf4j
@Service
public class DeviceIsupServiceImpl implements IDeviceIsupService {

    private static final String DISABLED_MSG = "开源版未启用海康 ISUP 协议，操作被忽略";

    // ==================== 设备信息 / 播放 ====================

    @Override
    public HaiKangIsupDeviceInfo getDevInfo(Integer lUserID) {
        return null;
    }

    @Override
    public void startPlay(RtpServerParam rtpServerParam) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void stopPlay(Long id, Integer channel) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void startPlayback(RtpServerParam rtpServerParam) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void stopPlayback(Long id, Integer channel) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public String getPlayUrl(Long deviceId, Integer channel, String type) {
        return null;
    }

    // ==================== 云台 / 预置点 / 巡航 / 辅助控制 ====================

    @Override
    public void startPtz(Long deviceId, Integer channelId, int ptzCmd, int speed) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void endPtz(Long deviceId, Integer channelId, int ptzCmd, int speed) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void setPreset(Long deviceId, Integer channelId, int presetIndex) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void clearPreset(Long deviceId, Integer channelId, int presetIndex) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void gotoPreset(Long deviceId, Integer channelId, int presetIndex) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void cameraAuxControl(Long deviceId, Integer channelId, String operation, boolean isStart) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void cruiseControl(Long deviceId, Integer channelId, String operation, Integer param) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public List<HaiKangIsupPresetInfo> getPresetList(Long deviceId, Integer channelId) {
        return new ArrayList<>();
    }

    // ==================== 录像 / 抓图 ====================

    @Override
    public ArrayList<HashMap<String, Object>> queryRecord(Long deviceId, Integer channelId, String startTime, String endTime) {
        return new ArrayList<>();
    }

    @Override
    public Long captureAndSave(Long deviceId, int channelId, String snapshotType) throws IOException {
        return null;
    }

    @Override
    public String captureSnapshotOnAlarm(String protocolDeviceId, Integer channelNo) {
        return null;
    }

    @Override
    public HaikangIsupRecordDownloadResponse downloadRecord(HaikangIsupRecordDownloadRequest request) {
        return null;
    }

    @Override
    public File downloadRecordFile(HaikangIsupRecordDownloadRequest request) throws Exception {
        return null;
    }

    // ==================== 设备运维 ====================

    @Override
    public void restartDevice(Long deviceId) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public String getDevTime(Long deviceId) {
        return null;
    }

    @Override
    public void setDevTime(Long deviceId, String time) {
        log.debug(DISABLED_MSG);
    }

    // ==================== 设备信息查询（ISAPI 透传） ====================

    @Override
    public HashMap<String, Object> getHaiKangIsupDeviceInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupStorageInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupSDCardInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupBitrateInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupNetworkStatusInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupSoftwareVersionInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupPowerStateInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HaiKangIsupCameraInfo getHaiKangIsupCameraInfo(Long deviceId) {
        return null;
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupSystemParam(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupVideoParam(Long deviceId, Integer channelId, String streamType) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupSystemStatus(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupDeviceInfoXml(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> upgradeHaiKangIsupDevice(HaiKangIsupUpgradeRequest request) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupDeviceConfig(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> setHaiKangIsupDeviceConfig(Long deviceId, HashMap<String, Object> config) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupDeviceDetail(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupVersionInfo(Long deviceId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> getHaiKangIsupMotionArea(Long deviceId, Integer channelId) {
        return new HashMap<>();
    }

    @Override
    public HashMap<String, Object> setHaiKangIsupMotionArea(Long deviceId, Integer channelId, HashMap<String, Object> motionAreaConfig) {
        return new HashMap<>();
    }

    @Override
    public Integer getUserIdByDeviceId(Long deviceId) {
        return null;
    }

    // ==================== 通道同步 / 上下线（SDK 回调驱动，开源版不触发） ====================

    @Override
    public void syncChannels(DeviceOnvif device) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void syncChannelsOnRegister(String rawDeviceId, int lUserID) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void upsertDeviceOnRegister(String rawDeviceId, String ip) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void offlineDevice(String fullDeviceId) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public List<DeviceOnvif> selectOnlineIsupDevices() {
        return new ArrayList<>();
    }

    @Override
    public int touchKeepalive(String fullDeviceId) {
        return 0;
    }

    @Override
    public boolean isDeviceReachable(String fullDeviceId) {
        return false;
    }

    // ==================== 数据访问 ====================

    @Override
    public List<DeviceOnvif> selectIsupDeviceList(DeviceOnvif query) {
        return new ArrayList<>();
    }

    @Override
    public DeviceOnvif selectIsupDeviceById(Long id) {
        return null;
    }

    @Override
    public int insertIsupDevice(DeviceOnvif device) {
        log.debug(DISABLED_MSG);
        return 0;
    }

    @Override
    public boolean syncDeviceInfoFromDevice(Long deviceId) {
        return false;
    }

    @Override
    public int updateIsupDevice(DeviceOnvif device) {
        log.debug(DISABLED_MSG);
        return 0;
    }

    @Override
    public int deleteIsupDeviceByIds(Long[] ids) {
        log.debug(DISABLED_MSG);
        return 0;
    }

    @Override
    public List<DeviceOnvifChannel> selectIsupChannelList(Long deviceId) {
        return new ArrayList<>();
    }

    // ==================== 语音对讲 ====================

    @Override
    public void startVoiceTalk(Long deviceId, Integer channelId) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public void stopVoiceTalk(Long deviceId, Integer channelId) {
        log.debug(DISABLED_MSG);
    }

    @Override
    public boolean sendVoiceTalkAudio(Long deviceId, Integer channelId, byte[] pcm) {
        return false;
    }

    @Override
    public Map<String, Object> getVoiceTalkStatus(Long deviceId, Integer channelId) {
        return new HashMap<>();
    }
}
