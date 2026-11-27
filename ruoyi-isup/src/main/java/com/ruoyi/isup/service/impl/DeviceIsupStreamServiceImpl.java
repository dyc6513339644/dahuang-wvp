package com.ruoyi.isup.service.impl;

import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.isup.service.IDeviceIsupStreamService;
import com.ruoyi.isup.service.IsupStreamCallback;
import com.ruoyi.onvif.domain.DeviceOnvif;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * ISUP 流媒体服务实现（开源版：已移除流转发与 SDK 调用）。
 * <p>
 * 开源版不启动 SMS 监听端口，也不做 PS → RTP 转发，因此预览/回放一律回调失败，
 * 让统一播放接口能立即返回错误而不是让前端一直等待。
 */
@Slf4j
@Service
public class DeviceIsupStreamServiceImpl implements IDeviceIsupStreamService {

    /** 与统一播放约定的失败码：0=成功，其余为失败 */
    private static final int CODE_DISABLED = -1;

    private static final String MSG_DISABLED = "开源版未启用海康 ISUP 协议，无法取流";

    @Override
    public void startPlay(RtpServerParam rtpServerParam) {
        log.debug(MSG_DISABLED);
    }

    @Override
    public void startPlay(RtpServerParam rtpServerParam, IsupStreamCallback callback) {
        log.debug(MSG_DISABLED);
        // 必须回调，否则统一播放的 DeferredResult 会一直挂起
        if (callback != null) {
            callback.onResult(CODE_DISABLED, MSG_DISABLED, null);
        }
    }

    @Override
    public void stopPlay(Long deviceId, Integer channel) {
        log.debug(MSG_DISABLED);
    }

    @Override
    public void startPlayback(RtpServerParam rtpServerParam) {
        log.debug(MSG_DISABLED);
    }

    @Override
    public void startPlayback(RtpServerParam rtpServerParam, IsupStreamCallback callback) {
        log.debug(MSG_DISABLED);
        if (callback != null) {
            callback.onResult(CODE_DISABLED, MSG_DISABLED, null);
        }
    }

    @Override
    public void stopPlayback(Long deviceId, Integer channel) {
        log.debug(MSG_DISABLED);
    }

    @Override
    public File downloadRecordByTime(Integer lUserID, DeviceOnvif device, Integer channelId,
                                     String startTime, String endTime, String filePath) {
        return null;
    }
}
