package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BaseConfig;

/**
 * 基础配置Service接口
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
public interface IBaseConfigService 
{
    /**
     * 查询基础配置
     * 
     * @param id 基础配置主键
     * @return 基础配置
     */
    public BaseConfig selectBaseConfigById(Integer id);

    /**
     * 查询基础配置列表
     * 
     * @param baseConfig 基础配置
     * @return 基础配置集合
     */
    public List<BaseConfig> selectBaseConfigList(BaseConfig baseConfig);

    /**
     * 新增基础配置
     * 
     * @param baseConfig 基础配置
     * @return 结果
     */
    public int insertBaseConfig(BaseConfig baseConfig);

    /**
     * 修改基础配置
     * 
     * @param baseConfig 基础配置
     * @return 结果
     */
    public int updateBaseConfig(BaseConfig baseConfig);

    /**
     * 批量修改基础配置
     * 
     * @param baseConfigs 基础配置列表
     * @return 结果
     */
    public int batchUpdateBaseConfig(List<BaseConfig> baseConfigs);

    /**
     * 批量删除基础配置
     * 
     * @param ids 需要删除的基础配置主键集合
     * @return 结果
     */
    public int deleteBaseConfigByIds(Integer[] ids);

    /**
     * 删除基础配置信息
     * 
     * @param id 基础配置主键
     * @return 结果
     */
    public int deleteBaseConfigById(Integer id);
}
