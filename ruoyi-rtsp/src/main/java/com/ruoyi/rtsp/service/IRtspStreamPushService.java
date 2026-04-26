package com.ruoyi.rtsp.service;

/**
 * @Author: 陈江灿
 * @CreateTime: 2025-06-10
 */
public interface IRtspStreamPushService {

    public boolean startPush(String streamId, String rtspUrl, String pushUrl);

    public boolean stopPush(String streamId);
}
