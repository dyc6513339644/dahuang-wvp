package com.ruoyi.media.service.impl;

import com.ruoyi.media.service.ISSRCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SSRC 分配服务实现。基于 Redis Set 管理各媒体节点的 SSRC 池：初始化、分配、释放、重置。
 */
@Component
public class SSRCServiceImpl implements ISSRCService {

    /**
     * 播流最大并发个数
     */
    private static final Integer MAX_STREAM_COUNT = 10000;

    /**
     * 播流最大并发个数
     */
    private static final String SSRC_INFO_KEY = "VMP_SSRC_INFO_";

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 构造 SSRC 池的 Redis key
     * <p>
     * serverId 用于隔离不同 WVP 实例的 SSRC 池（集群部署必需，详见 ISSRCService 注释）
     */
    private String buildRedisKey(String mediaServerId, String serverId) {
        return SSRC_INFO_KEY + serverId + "_" + mediaServerId;
    }

    @Override
    public void initMediaServerSSRC(String mediaServerId, Set<String> usedSet, String ssrcPrefix, String serverId) {
        String redisKey = buildRedisKey(mediaServerId, serverId);
        List<String> ssrcList = new ArrayList<>();
        for (int i = 1; i < MAX_STREAM_COUNT; i++) {
            String ssrc = String.format("%s%04d", ssrcPrefix, i);

            if (null == usedSet || !usedSet.contains(ssrc)) {
                ssrcList.add(ssrc);

            }
        }
        if (redisTemplate.opsForSet().size(redisKey) != null) {
            redisTemplate.delete(redisKey);
        }
        redisTemplate.opsForSet().add(redisKey, ssrcList.toArray(new String[0]));
    }


    /**
     * 获取视频预览的SSRC值,第一位固定为0
     *
     * @return ssrc
     */
    @Override
    public String getPlaySsrc(String mediaServerId, String serverId) {
        return "0" + getSN(mediaServerId, serverId);
    }

    /**
     * 获取录像回放的SSRC值,第一位固定为1
     */
    @Override
    public String getPlayBackSsrc(String mediaServerId, String serverId) {
        return "1" + getSN(mediaServerId, serverId);
    }

    /**
     * 释放ssrc，主要用完的ssrc一定要释放，否则会耗尽
     *
     * @param ssrc 需要重置的ssrc
     */
    @Override
    public void releaseSsrc(String mediaServerId, String ssrc, String serverId) {
        if (ssrc == null) {
            return;
        }
        String sn = ssrc.substring(1);
        redisTemplate.opsForSet().add(buildRedisKey(mediaServerId, serverId), sn);
    }

    /**
     * 获取后四位数SN,随机数
     */
    private String getSN(String mediaServerId, String serverId) {
        String redisKey = buildRedisKey(mediaServerId, serverId);
        Long size = redisTemplate.opsForSet().size(redisKey);
        if (size == null || size == 0) {
            throw new RuntimeException("ssrc已经用完");
        } else {
            // 在集合中移除并返回一个随机成员。
            return redisTemplate.opsForSet().pop(redisKey);
        }
    }

    /**
     * 重置一个流媒体服务的所有ssrc
     *
     * @param mediaServerId 流媒体服务ID
     * @param ssrcPrefix    SSRC 前缀（由调用方传入）
     * @param serverId      服务ID（由调用方传入）
     */
    @Override
    public void reset(String mediaServerId, String ssrcPrefix, String serverId) {
        this.initMediaServerSSRC(mediaServerId, null, ssrcPrefix, serverId);
    }

    /**
     * 是否已经存在了某个MediaServer的SSRC信息
     *
     * @param mediaServerId 流媒体服务ID
     * @param serverId      服务ID（由调用方传入）
     */
    @Override
    public boolean hasMediaServerSSRC(String mediaServerId, String serverId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildRedisKey(mediaServerId, serverId)));
    }

}
