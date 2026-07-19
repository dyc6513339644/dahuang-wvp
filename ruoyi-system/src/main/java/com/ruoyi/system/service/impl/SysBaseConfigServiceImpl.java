package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysBaseConfig;
import com.ruoyi.system.mapper.SysBaseConfigMapper;
import com.ruoyi.system.service.ISysBaseConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 基础配置Service实现类
 *
 * @author ruoyi
 */
@Slf4j
@Service
public class SysBaseConfigServiceImpl implements ISysBaseConfigService {

    @Autowired
    private SysBaseConfigMapper baseConfigMapper;

    @Override
    public SysBaseConfig getBaseConfig() {

        List<SysBaseConfig> sysBaseConfigList= baseConfigMapper.selectList(null);
        if(sysBaseConfigList.size()>0){
            return sysBaseConfigList.get(0);
        }
        return null;
    }

    @Override
    public boolean save(SysBaseConfig config) {
        Date now = new Date();
        config.setCreateTime(now);
        config.setUpdateTime(now);
        baseConfigMapper.insert(config);
        return true;
    }

    @Override
    public boolean update(SysBaseConfig config) {
        config.setUpdateTime(new Date());
        baseConfigMapper.updateById(config);
        return true;
    }

    @Override
    public boolean exists() {
        return baseConfigMapper.selectCount(null)> 0;
    }
}
