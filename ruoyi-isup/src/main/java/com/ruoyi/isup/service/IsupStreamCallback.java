package com.ruoyi.isup.service;

import com.ruoyi.media.domain.StreamInfo;

/**
 * ISUP 流结果回调：把 ZLM hook 异步回填的流地址，桥接到统一播放的 DeferredResult。
 */
@FunctionalInterface
public interface IsupStreamCallback {

    /**
     * 流结果回调
     * @param code       0=成功，其余为失败
     * @param msg        描述信息
     * @param streamInfo 完整流信息；失败时为空
     */
    void onResult(int code, String msg, StreamInfo streamInfo);
}
