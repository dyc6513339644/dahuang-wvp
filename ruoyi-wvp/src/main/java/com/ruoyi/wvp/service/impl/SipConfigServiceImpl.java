package com.ruoyi.wvp.service.impl;

import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;
import com.ruoyi.wvp.mapper.SipConfigMapper;
import com.ruoyi.wvp.service.ISipConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * SIP配置服务实现类
 *
 * @author ruoyi-wvp
 */
@Slf4j
@Service
public class SipConfigServiceImpl implements ISipConfigService {

    @Autowired
    private SipConfigMapper sipConfigMapper;

    @Autowired
    private SipConfig sipConfig;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public SipConfigEntity getSipConfig() {
        List<SipConfigEntity> sipConfigEntities= sipConfigMapper.selectList(null);
        if(!sipConfigEntities.isEmpty()){
            return sipConfigEntities.get(0);
        }
        return null;
    }

    @Override
    public SipConfigEntity getSipConfigById(Long id) {
        return  sipConfigMapper.selectById(id);
    }

    @Override
    public boolean save(SipConfigEntity sipConfig) {
        String now = LocalDateTime.now().format(FORMATTER);
        sipConfig.setCreateTime(now);
        sipConfig.setUpdateTime(now);
        int result= sipConfigMapper.insert(sipConfig);
        if (result > 0) {
            // 保存成功后重新加载配置
            reloadSipConfig();
            return true;
        }
        return false;
    }

    @Override
    public boolean update(SipConfigEntity sipConfig) {
        String now = LocalDateTime.now().format(FORMATTER);
        sipConfig.setUpdateTime(now);
        int result = sipConfigMapper.updateById(sipConfig);
        if (result > 0) {
            // 更新成功后重新加载配置
            reloadSipConfig();
            return true;
        }
        return false;
    }

    @Override
    public List<SipConfigEntity> getAll() {

        return sipConfigMapper.selectList(null);
    }

    @Override
    public boolean delete(Long id) {
        int result = sipConfigMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public boolean exists() {
        return  sipConfigMapper.selectCount(null) > 0;
    }

    /**
     * 重新加载SIP配置
     */
    private void reloadSipConfig() {
        try {
            sipConfig.reload();
        } catch (Exception e) {
            log.error("[SIP配置] 重新加载配置失败", e);
        }
    }
}
