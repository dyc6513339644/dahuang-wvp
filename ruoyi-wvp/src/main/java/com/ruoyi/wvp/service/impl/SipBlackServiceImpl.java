package com.ruoyi.wvp.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.wvp.gb28181.bean.SipBlackEntity;
import com.ruoyi.wvp.mapper.SipBlackMapper;
import com.ruoyi.wvp.service.ISipBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * SIP 黑名单服务实现类
 * <p>
 * 使用 Redis String 缓存完整黑名单列表：key=vmp_sip_black:, value=JSON array
 *
 * @author ruoyi-wvp
 */
@Slf4j
@Service
public class SipBlackServiceImpl implements ISipBlackService {

    @Autowired
    private SipBlackMapper sipBlackMapper;

    @Autowired
    private RedisCache redisCache;

    private static final String REDIS_KEY = CacheConstants.SIP_BLACK_KEY;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        refreshCache();
    }

    /**
     * 全量刷新缓存：DB → JSON 数组 → Redis String
     */
    private void refreshCache() {
        List<SipBlackEntity> all = sipBlackMapper.selectList(null);
        redisCache.setCacheObject(REDIS_KEY, JSON.toJSONString(all));
    }

    /**
     * 从缓存读取黑名单列表
     */
    private List<SipBlackEntity> getCachedList() {
        String json = redisCache.getCacheObject(REDIS_KEY);
        if (json == null) {
            return Collections.emptyList();
        }
        return JSON.parseArray(json, SipBlackEntity.class);
    }

    @Override
    public List<SipBlackEntity> list() {
        return sipBlackMapper.selectList(
                new LambdaQueryWrapper<SipBlackEntity>().orderByDesc(SipBlackEntity::getCreateTime));
    }

    @Override
    public SipBlackEntity getById(Long id) {
        return sipBlackMapper.selectById(id);
    }

    @Override
    public SipBlackEntity getByDeviceId(String deviceId) {
        LambdaQueryWrapper<SipBlackEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SipBlackEntity::getDeviceId, deviceId);
        return sipBlackMapper.selectOne(wrapper);
    }

    @Override
    public boolean add(SipBlackEntity entity) {
        if (getByDeviceId(entity.getDeviceId()) != null) {
            log.warn("黑名单添加失败，设备ID已存在: {}", entity.getDeviceId());
            return false;
        }
        String now = LocalDateTime.now().format(FORMATTER);
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        int result = sipBlackMapper.insert(entity);
        if (result > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean update(SipBlackEntity entity) {
        String now = LocalDateTime.now().format(FORMATTER);
        entity.setUpdateTime(now);
        int result = sipBlackMapper.updateById(entity);
        if (result > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        int result = sipBlackMapper.deleteById(id);
        if (result > 0) {
            refreshCache();
            return true;
        }
        return false;
    }

    @Override
    public boolean isBlacklisted(String deviceId, String ip, Integer port) {
        List<SipBlackEntity> list = getCachedList();
        for (SipBlackEntity entry : list) {
            if (!entry.getDeviceId().equals(deviceId)) {
                continue;
            }
            boolean ipMatch = entry.getIp() == null || entry.getIp().isEmpty() || entry.getIp().equals(ip);
            boolean portMatch = entry.getPort() == null || entry.getPort().equals(port);
            if (ipMatch && portMatch) {
                return true;
            }
        }
        return false;
    }
}
