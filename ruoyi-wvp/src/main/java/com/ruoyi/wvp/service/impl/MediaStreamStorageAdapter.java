package com.ruoyi.wvp.service.impl;

import com.ruoyi.wvp.gb28181.service.IInviteStreamService;
import com.ruoyi.media.domain.MediaInfo;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.wvp.service.IMediaStreamStorage;
import com.ruoyi.media.zlm.dto.StreamAuthorityInfo;
import com.ruoyi.wvp.storager.IRedisCatchStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流信息存储 SPI 的 wvp 实现 —— 适配 IRedisCatchStorage 与 IInviteStreamService。
 */
@Component
public class MediaStreamStorageAdapter implements IMediaStreamStorage {

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    @Autowired
    private IInviteStreamService inviteStreamService;

    @Override
    public void addStream(MediaServer mediaServerItem, String type, String app, String streamId, MediaInfo mediaInfo) {
        redisCatchStorage.addStream(mediaServerItem, type, app, streamId, mediaInfo);
    }

    @Override
    public MediaInfo getStreamInfo(String app, String streamId, String mediaServerId) {
        return redisCatchStorage.getStreamInfo(app, streamId, mediaServerId);
    }

    @Override
    public void removeStream(String mediaServerId, String type, String app, String streamId) {
        redisCatchStorage.removeStream(mediaServerId, type, app, streamId);
    }

    @Override
    public int getPushStreamCount(String mediaServerId) {
        return redisCatchStorage.getPushStreamCount(mediaServerId);
    }

    @Override
    public int getProxyStreamCount(String mediaServerId) {
        return redisCatchStorage.getProxyStreamCount(mediaServerId);
    }

    @Override
    public int getGbSendCount(String mediaServerId) {
        return redisCatchStorage.getGbSendCount(mediaServerId);
    }

    @Override
    public int getStreamInfoCount(String mediaServerId) {
        return inviteStreamService.getStreamInfoCount(mediaServerId);
    }

    @Override
    public StreamAuthorityInfo getStreamAuthorityInfo(String app, String stream) {
        return redisCatchStorage.getStreamAuthorityInfo(app, stream);
    }
}
