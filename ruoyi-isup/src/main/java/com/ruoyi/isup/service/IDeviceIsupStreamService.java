package com.ruoyi.isup.service;

import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.onvif.domain.DeviceOnvif;

import java.io.File;

/**
 * ISUP 流媒体服务：预览 / 回放 / 录像下载。
 * 预览、回放通过 CMS 指令让设备把 PS 流推送到本模块 SMS 监听端口，
 * 由 PreviewStreamHandler / PlaybackStreamHandler 转发为 RTP 到 ZLMediaKit 收流端口。
 */
public interface IDeviceIsupStreamService {

    /**
     * 开始预览（实时视频）
     */
    void startPlay(RtpServerParam rtpServerParam);

    /**
     * 开始预览（实时视频），结果通过 callback 异步返回（用于统一播放，替代轮询 getPlayUrl）
     */
    void startPlay(RtpServerParam rtpServerParam, IsupStreamCallback callback);

    /**
     * 停止预览
     */
    void stopPlay(Long deviceId, Integer channel);

    /**
     * 开始回放
     */
    void startPlayback(RtpServerParam rtpServerParam);

    /**
     * 开始回放，结果通过 callback 异步返回（用于统一播放，替代轮询 getPlayUrl）
     */
    void startPlayback(RtpServerParam rtpServerParam, IsupStreamCallback callback);

    /**
     * 停止回放
     */
    void stopPlayback(Long deviceId, Integer channel);

    /**
     * 按时间下载录像到本地文件
     */
    File downloadRecordByTime(Integer lUserID, DeviceOnvif device, Integer channelId,
                              String startTime, String endTime, String filePath);
}
