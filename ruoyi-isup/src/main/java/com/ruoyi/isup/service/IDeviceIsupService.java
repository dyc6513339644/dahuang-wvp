package com.ruoyi.isup.service;

import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.isup.domain.*;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 海康 ISUP 设备服务接口
 */
public interface IDeviceIsupService {

    /**
     * 获取设备信息
     */
    HaiKangIsupDeviceInfo getDevInfo(Integer lUserID);

    /**
     * 开始播放
     */
    void startPlay(RtpServerParam rtpServerParam);

    /**
     * 停止播放
     */
    void stopPlay(Long id, Integer channel);

    /**
     * 开始云台控制
     */
    void startPtz(Long deviceId, Integer channelId, int ptzCmd, int speed);

    /**
     * 结束云台控制
     */
    void endPtz(Long deviceId, Integer channelId, int ptzCmd, int speed);

    /**
     * 设置预置点
     */
    void setPreset(Long deviceId, Integer channelId, int presetIndex);

    /**
     * 清除预置点
     */
    void clearPreset(Long deviceId, Integer channelId, int presetIndex);

    /**
     * 调用预置点
     */
    void gotoPreset(Long deviceId, Integer channelId, int presetIndex);

    /**
     * 辅助设备控制
     */
    void cameraAuxControl(Long deviceId, Integer channelId, String operation, boolean isStart);

    /**
     * 巡航控制
     */
    void cruiseControl(Long deviceId, Integer channelId, String operation, Integer param);

    /**
     * 获取预置点列表
     */
    List<HaiKangIsupPresetInfo> getPresetList(Long deviceId, Integer channelId);

    /**
     * 查询录像
     */
    ArrayList<HashMap<String, Object>> queryRecord(Long deviceId, Integer channelId, String startTime, String endTime);

    /**
     * 开始回放
     */
    void startPlayback(RtpServerParam rtpServerParam);

    /**
     * 停止回放
     */
    void stopPlayback(Long id, Integer channel);

    /**
     * 获取设备当前播放地址（预览/回放，由 ZLMediaKit hook 异步生成，需轮询）
     * @param type preview-预览，playback-回放
     */
    String getPlayUrl(Long deviceId, Integer channel, String type);

    /**
     * 重启设备
     */
    void restartDevice(Long deviceId);

    /**
     * 获取设备时间
     */
    String getDevTime(Long deviceId);

    /**
     * 设置设备时间
     */
    void setDevTime(Long deviceId, String time);

    /**
     * 抓图并保存
     */
    Long captureAndSave(Long deviceId, int channelId, String snapshotType) throws IOException;

    /**
     * 报警联动抓图：按平台设备ID（haikang_isup_xxx）与通道号主动抓图并落盘，返回可访问的图片 URL。
     * <p>
     * 通道号为空时取该设备在平台上的第一个通道。设备未注册、不支持抓图或抓图失败均返回 null，
     * 由调用方按「无图片」处理，不抛出异常。
     */
    String captureSnapshotOnAlarm(String protocolDeviceId, Integer channelNo);

    /**
     * 录像下载
     */
    HaikangIsupRecordDownloadResponse downloadRecord(HaikangIsupRecordDownloadRequest request);

    /**
     * 录像下载文件
     */
    java.io.File downloadRecordFile(HaikangIsupRecordDownloadRequest request) throws Exception;

    /**
     * 获取设备基础信息
     */
    HashMap<String, Object> getHaiKangIsupDeviceInfo(Long deviceId);

    /**
     * 获取存储信息
     */
    HashMap<String, Object> getHaiKangIsupStorageInfo(Long deviceId);

    /**
     * 获取SD卡信息
     */
    HashMap<String, Object> getHaiKangIsupSDCardInfo(Long deviceId);

    /**
     * 获取码率信息
     */
    HashMap<String, Object> getHaiKangIsupBitrateInfo(Long deviceId);

    /**
     * 获取网络状态
     */
    HashMap<String, Object> getHaiKangIsupNetworkStatusInfo(Long deviceId);

    /**
     * 获取软件版本
     */
    HashMap<String, Object> getHaiKangIsupSoftwareVersionInfo(Long deviceId);

    /**
     * 获取电源状态
     */
    HashMap<String, Object> getHaiKangIsupPowerStateInfo(Long deviceId);

    /**
     * 获取摄像头属性
     */
    HaiKangIsupCameraInfo getHaiKangIsupCameraInfo(Long deviceId);

    /**
     * 获取系统参数
     */
    HashMap<String, Object> getHaiKangIsupSystemParam(Long deviceId);

    /**
     * 获取视频参数
     */
    HashMap<String, Object> getHaiKangIsupVideoParam(Long deviceId, Integer channelId, String streamType);

    /**
     * 获取系统状态
     */
    HashMap<String, Object> getHaiKangIsupSystemStatus(Long deviceId);

    /**
     * 获取设备XML信息
     */
    HashMap<String, Object> getHaiKangIsupDeviceInfoXml(Long deviceId);

    /**
     * 远程升级
     */
    HashMap<String, Object> upgradeHaiKangIsupDevice(HaiKangIsupUpgradeRequest request);

    /**
     * 获取设备配置
     */
    HashMap<String, Object> getHaiKangIsupDeviceConfig(Long deviceId);

    /**
     * 设置设备配置
     */
    HashMap<String, Object> setHaiKangIsupDeviceConfig(Long deviceId, HashMap<String, Object> config);

    /**
     * 获取设备详情
     */
    HashMap<String, Object> getHaiKangIsupDeviceDetail(Long deviceId);

    /**
     * 获取版本信息
     */
    HashMap<String, Object> getHaiKangIsupVersionInfo(Long deviceId);

    /**
     * 获取移动侦测区域
     */
    HashMap<String, Object> getHaiKangIsupMotionArea(Long deviceId, Integer channelId);

    /**
     * 设置移动侦测区域
     */
    HashMap<String, Object> setHaiKangIsupMotionArea(Long deviceId, Integer channelId, HashMap<String, Object> motionAreaConfig);

    /**
     * 根据数据库 deviceId 获取本地登录句柄
     */
    Integer getUserIdByDeviceId(Long deviceId);

    /**
     * 同步设备通道到 wvp_device_channel（手动触发，需设备在线）
     */
    void syncChannels(DeviceOnvif device);

    /**
     * 设备上线时自动同步通道：直接用注册回调的 lUserID，不依赖 lUserIDMap 反查。
     * @param rawDeviceId 海康设备原始 DeviceID（不含 haikang_isup_ 前缀）
     * @param lUserID 注册回调分配的登录句柄
     */
    void syncChannelsOnRegister(String rawDeviceId, int lUserID);

    /**
     * 设备上线时 upsert 到 wvp_device（protocol_type='ISUP'，上线置 on_line=1）
     * @param rawDeviceId 海康设备原始 DeviceID（不含 haikang_isup_ 前缀）
     * @param ip 设备 IP
     */
    void upsertDeviceOnRegister(String rawDeviceId, String ip);

    /**
     * 设备下线时标记 wvp_device 离线（on_line=0），并联动通道置离线
     * @param fullDeviceId 完整 deviceId（含 haikang_isup_ 前缀）
     */
    void offlineDevice(String fullDeviceId);

    // ==================== 在离线巡检（供 IsupDeviceOnlineTask 使用） ====================

    /**
     * 查询数据库中标记为在线的 ISUP 设备（只取 id/deviceId/keepaliveTime，供心跳超时巡检使用）
     */
    List<DeviceOnvif> selectOnlineIsupDevices();

    /**
     * 刷新设备心跳时间（只更新 keepalive_time 与 on_line）
     */
    int touchKeepalive(String fullDeviceId);

    /**
     * 探活：按设备当前 lUserID 查询一次设备信息，用于心跳超时后确认设备是否真的离线
     */
    boolean isDeviceReachable(String fullDeviceId);

    // ==================== 设备/通道数据访问（Controller 不直接持有 Mapper） ====================

    /**
     * 查询 ISUP 设备列表（分页由调用方 startPage 控制）
     */
    List<DeviceOnvif> selectIsupDeviceList(DeviceOnvif query);

    /**
     * 按主键查询 ISUP 设备
     */
    DeviceOnvif selectIsupDeviceById(Long id);

    /**
     * 新增 ISUP 设备（强制 protocol_type='ISUP'、离线状态）
     */
    int insertIsupDevice(DeviceOnvif device);

    /**
     * 立即从设备拉取信息（ISAPI /System/deviceInfo）并回填名称/型号/固件。
     * 新增设备后会自动异步调用一次，此方法供手动刷新使用。
     *
     * @return true 同步成功（或设备已不存在），false 设备未注册/查询失败
     */
    boolean syncDeviceInfoFromDevice(Long deviceId);

    /**
     * 修改 ISUP 设备（强制 protocol_type='ISUP'）
     */
    int updateIsupDevice(DeviceOnvif device);

    /**
     * 批量删除 ISUP 设备，同时删除其关联通道
     */
    int deleteIsupDeviceByIds(Long[] ids);

    /**
     * 查询某 ISUP 设备的通道列表（分页由调用方 startPage 控制）
     */
    List<DeviceOnvifChannel> selectIsupChannelList(Long deviceId);

    // ==================== 语音对讲（StmServer 模式） ====================

    /**
     * 开始语音对讲。
     * <p>
     * 走 {@code NET_ECMS_StartVoiceWithStmServer}，设备对讲音频回推到平台流服务的
     * {@code haikang-isup.ports.voice} 端口；上行音频由 {@link #sendVoiceTalkAudio} 下发。
     *
     * @param deviceId  设备库主键
     * @param channelId ISUP 通道号
     */
    void startVoiceTalk(Long deviceId, Integer channelId);

    /**
     * 停止语音对讲，释放设备音频通道与对讲链路（幂等）
     */
    void stopVoiceTalk(Long deviceId, Integer channelId);

    /**
     * 发送平台侧语音（PCM 16bit 小端 / 8kHz / 单声道），内部转 G.711A 后下发设备
     *
     * @return 对讲链路已就绪且发送成功返回 true；设备尚未回连时返回 false（该帧丢弃）
     */
    boolean sendVoiceTalkAudio(Long deviceId, Integer channelId, byte[] pcm);

    /**
     * 对讲链路状态，供前端显示“等待设备回连 / 已连接”以及上下行统计
     */
    Map<String, Object> getVoiceTalkStatus(Long deviceId, Integer channelId);
}
