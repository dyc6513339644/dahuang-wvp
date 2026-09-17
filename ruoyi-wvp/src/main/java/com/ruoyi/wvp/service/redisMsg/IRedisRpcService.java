package com.ruoyi.wvp.service.redisMsg;

import com.ruoyi.wvp.common.CommonCallback;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.media.domain.SendRtpInfo;
import com.ruoyi.media.domain.WVPResult;

public interface IRedisRpcService {

    SendRtpInfo getSendRtpItem(String callId);

    WVPResult startSendRtp(String callId, SendRtpInfo sendRtpItem);

    WVPResult stopSendRtp(String callId);

    long waitePushStreamOnline(SendRtpInfo sendRtpItem, CommonCallback<Integer> callback);

    void stopWaitePushStreamOnline(SendRtpInfo sendRtpItem);

    void rtpSendStopped(String callId);

    void removeCallback(long key);

    long onStreamOnlineEvent(String app, String stream, CommonCallback<StreamInfo> callback);
    void unPushStreamOnlineEvent(String app, String stream);
}
