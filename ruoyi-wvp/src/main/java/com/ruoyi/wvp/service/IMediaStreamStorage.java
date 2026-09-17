package com.ruoyi.wvp.service;

import com.ruoyi.media.domain.MediaInfo;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.zlm.dto.StreamAuthorityInfo;

/**
 * 流信息存储服务 —— 聚合 IRedisCatchStorage（Redis 流缓存）与
 * IInviteStreamService（国标点播流），为媒体节点服务提供流信息读写与负载统计。
 * <p>
 * 实现见 {@link com.ruoyi.wvp.service.impl.MediaStreamStorageAdapter}。
 */
public interface IMediaStreamStorage {

    /**
     * 写入流信息到缓存
     *
     * @param type 流类型（push/pull/proxy 等）
     */
    void addStream(MediaServer mediaServerItem, String type, String app, String streamId, MediaInfo mediaInfo);

    /**
     * 根据 app/stream/mediaServerId 查询流信息
     */
    MediaInfo getStreamInfo(String app, String streamId, String mediaServerId);

    /**
     * 移除流信息
     */
    void removeStream(String mediaServerId, String type, String app, String streamId);

    /**
     * 获取推流数量
     */
    int getPushStreamCount(String mediaServerId);

    /**
     * 获取拉流代理数量
     */
    int getProxyStreamCount(String mediaServerId);

    /**
     * 获取国标发流数量
     */
    int getGbSendCount(String mediaServerId);

    /**
     * 获取国标收流数量（负载统计）
     */
    int getStreamInfoCount(String mediaServerId);

    /**
     * 获取流的鉴权信息
     */
    StreamAuthorityInfo getStreamAuthorityInfo(String app, String stream);
}
