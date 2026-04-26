package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BaseConfigMapper;
import com.ruoyi.system.domain.BaseConfig;
import com.ruoyi.system.service.IBaseConfigService;

/**
 * 基础配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
@Service
public class BaseConfigServiceImpl implements IBaseConfigService 
{
    @Autowired
    private BaseConfigMapper baseConfigMapper;

    /**
     * 查询基础配置
     * 
     * @param id 基础配置主键
     * @return 基础配置
     */
    @Override
    public BaseConfig selectBaseConfigById(Integer id)
    {
        return baseConfigMapper.selectBaseConfigById(id);
    }

    /**
     * 查询基础配置列表
     * 
     * @param baseConfig 基础配置
     * @return 基础配置
     */
    @Override
    public List<BaseConfig> selectBaseConfigList(BaseConfig baseConfig)
    {
        return baseConfigMapper.selectBaseConfigList(baseConfig);
    }

    /**
     * 新增基础配置
     * 
     * @param baseConfig 基础配置
     * @return 结果
     */
    @Override
    public int insertBaseConfig(BaseConfig baseConfig)
    {
        return baseConfigMapper.insertBaseConfig(baseConfig);
    }

    /**
     * 修改基础配置
     * 
     * @param baseConfig 基础配置
     * @return 结果
     */
    @Override
    public int updateBaseConfig(BaseConfig baseConfig)
    {
        return baseConfigMapper.updateBaseConfig(baseConfig);
    }

    /**
     * 批量修改基础配置
     * 
     * @param baseConfigs 基础配置列表
     * @return 结果
     */
    @Override
    public int batchUpdateBaseConfig(List<BaseConfig> baseConfigs)
    {
        if (baseConfigs == null || baseConfigs.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        for (BaseConfig config : baseConfigs) {
            count += baseConfigMapper.updateBaseConfig(config);
        }
        return count;
    }

    /**
     * 批量删除基础配置
     * 
     * @param ids 需要删除的基础配置主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigByIds(Integer[] ids)
    {
        return baseConfigMapper.deleteBaseConfigByIds(ids);
    }

    /**
     * 删除基础配置信息
     * 
     * @param id 基础配置主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigById(Integer id)
    {
        return baseConfigMapper.deleteBaseConfigById(id);
    }
}
