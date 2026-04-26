package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BaseConfigTab;

/**
 * 基础配置分类Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
public interface BaseConfigTabMapper 
{
    /**
     * 查询基础配置分类
     * 
     * @param id 基础配置分类主键
     * @return 基础配置分类
     */
    public BaseConfigTab selectBaseConfigTabById(Long id);

    /**
     * 查询基础配置分类列表
     * 
     * @param baseConfigTab 基础配置分类
     * @return 基础配置分类集合
     */
    public List<BaseConfigTab> selectBaseConfigTabList(BaseConfigTab baseConfigTab);

    /**
     * 新增基础配置分类
     * 
     * @param baseConfigTab 基础配置分类
     * @return 结果
     */
    public int insertBaseConfigTab(BaseConfigTab baseConfigTab);

    /**
     * 修改基础配置分类
     * 
     * @param baseConfigTab 基础配置分类
     * @return 结果
     */
    public int updateBaseConfigTab(BaseConfigTab baseConfigTab);

    /**
     * 删除基础配置分类
     * 
     * @param id 基础配置分类主键
     * @return 结果
     */
    public int deleteBaseConfigTabById(Long id);

    /**
     * 批量删除基础配置分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBaseConfigTabByIds(Long[] ids);
}
