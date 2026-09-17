package com.ruoyi.media.service;

import java.util.Set;

/**
 * SSRC 分配服务
 */
public interface ISSRCService {

    /**
     * 初始化某个媒体服务器的 SSRC 池
     *
     * @param mediaServerId 媒体服务器 ID
     * @param usedSet       已占用的 SSRC 集合（可为 null）
     * @param ssrcPrefix    SSRC 前缀（由调用方传入）
     * @param serverId      服务 ID，用于隔离不同 WVP 实例的 SSRC 池（由调用方传入）
     */
    void initMediaServerSSRC(String mediaServerId, Set<String> usedSet, String ssrcPrefix, String serverId);

    /**
     * 获取一个点播用的 SSRC
     *
     * @param serverId 服务 ID（由调用方传入）
     */
    String getPlaySsrc(String mediaServerId, String serverId);

    /**
     * 获取一个回放用的 SSRC
     *
     * @param serverId 服务 ID（由调用方传入）
     */
    String getPlayBackSsrc(String mediaServerId, String serverId);

    /**
     * 释放 SSRC
     *
     * @param serverId 服务 ID（由调用方传入）
     */
    void releaseSsrc(String mediaServerId, String ssrc, String serverId);

    /**
     * 重置某个媒体服务器的 SSRC 池
     *
     * @param mediaServerId 媒体服务器 ID
     * @param ssrcPrefix    SSRC 前缀（由调用方传入）
     * @param serverId      服务 ID（由调用方传入）
     */
    void reset(String mediaServerId, String ssrcPrefix, String serverId);

    /**
     * 判断某个媒体服务器是否已初始化 SSRC 池
     *
     * @param serverId 服务 ID（由调用方传入）
     */
    boolean hasMediaServerSSRC(String mediaServerId, String serverId);
}
