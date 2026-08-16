package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.system.domain.WvpUserApiKey;
import com.ruoyi.system.mapper.WvpUserApiKeyMapper;
import com.ruoyi.system.service.IWvpUserApiKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/**
 * 用户API密钥Service实现类
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class WvpUserApiKeyServiceImpl implements IWvpUserApiKeyService {

    @Autowired
    private WvpUserApiKeyMapper apiKeyMapper;

    /** AK前缀 */
    private static final String AK_PREFIX = "ak_";

    /** 随机源 */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public List<WvpUserApiKey> listByUserId(Long userId) {
        LambdaQueryWrapper<WvpUserApiKey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WvpUserApiKey::getUserId, userId)
               .orderByDesc(WvpUserApiKey::getCreateTime);
        // select 排除 secret_key 字段
        wrapper.select(WvpUserApiKey.class, field ->
            !"secret_key".equals(field.getProperty()) && !"secretKey".equals(field.getProperty()));
        return apiKeyMapper.selectList(wrapper);
    }

    @Override
    public WvpUserApiKey generate(Long userId, String appName, String remark, String createBy) {
        WvpUserApiKey entity = new WvpUserApiKey();
        entity.setUserId(userId);
        entity.setAccessKey(generateAccessKey());
        entity.setSecretKey(generateSecretKey());
        entity.setAppName(appName);
        entity.setEnable(true);

        Date now = new Date();
        entity.setCreateBy(createBy);
        entity.setCreateTime(now);
        entity.setUpdateBy(createBy);
        entity.setUpdateTime(now);
        entity.setRemark(remark);

        apiKeyMapper.insert(entity);
        log.info("API密钥已创建 - userId: {}, accessKey: {}, appName: {}", userId, entity.getAccessKey(), appName);

        // 返回时保留 secretKey（仅此一次），后续查询不再返回
        return entity;
    }

    @Override
    public boolean toggleEnable(Long id, String updateBy) {
        WvpUserApiKey entity = apiKeyMapper.selectById(id);
        if (entity == null) {
            return false;
        }
        entity.setEnable(!entity.getEnable());
        entity.setUpdateBy(updateBy);
        entity.setUpdateTime(new Date());
        apiKeyMapper.updateById(entity);
        log.info("API密钥状态已切换 - id: {}, enable: {}", id, entity.getEnable());
        return true;
    }

    @Override
    public boolean delete(Long id) {
        int result = apiKeyMapper.deleteById(id);
        if (result > 0) {
            log.info("API密钥已删除 - id: {}", id);
            return true;
        }
        return false;
    }

    @Override
    public WvpUserApiKey getByAccessKey(String accessKey) {
        LambdaQueryWrapper<WvpUserApiKey> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WvpUserApiKey::getAccessKey, accessKey);
        return apiKeyMapper.selectOne(wrapper);
    }

    /**
     * 生成Access Key
     * 格式: ak_ + 24位随机字母数字
     */
    private String generateAccessKey() {
        byte[] randomBytes = new byte[18];
        SECURE_RANDOM.nextBytes(randomBytes);
        String random = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return AK_PREFIX + random;
    }

    /**
     * 生成Secret Key
     * 格式: 64位随机字母数字
     */
    private String generateSecretKey() {
        byte[] randomBytes = new byte[48];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
