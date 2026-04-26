package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BaseConfigTabMapper;
import com.ruoyi.system.domain.BaseConfigTab;
import com.ruoyi.system.service.IBaseConfigTabService;

/**
 * 基础配置分类Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
@Service
public class BaseConfigTabServiceImpl implements IBaseConfigTabService 
{
    @Autowired
    private BaseConfigTabMapper baseConfigTabMapper;

    /**
     * 查询基础配置分类
     * 
     * @param id 基础配置分类主键
     * @return 基础配置分类
     */
    @Override
    public BaseConfigTab selectBaseConfigTabById(Long id)
    {
        return baseConfigTabMapper.selectBaseConfigTabById(id);
    }

    /**
     * 查询基础配置分类列表
     * 
     * @param baseConfigTab 基础配置分类
     * @return 基础配置分类
     */
    @Override
    public List<BaseConfigTab> selectBaseConfigTabList(BaseConfigTab baseConfigTab)
    {
        return baseConfigTabMapper.selectBaseConfigTabList(baseConfigTab);
    }

    /**
     * 新增基础配置分类
     * 
     * @param baseConfigTab 基础配置分类
     * @return 结果
     */
    @Override
    public int insertBaseConfigTab(BaseConfigTab baseConfigTab)
    {
        return baseConfigTabMapper.insertBaseConfigTab(baseConfigTab);
    }

    /**
     * 修改基础配置分类
     * 
     * @param baseConfigTab 基础配置分类
     * @return 结果
     */
    @Override
    public int updateBaseConfigTab(BaseConfigTab baseConfigTab)
    {
        return baseConfigTabMapper.updateBaseConfigTab(baseConfigTab);
    }

    /**
     * 批量删除基础配置分类
     * 
     * @param ids 需要删除的基础配置分类主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigTabByIds(Long[] ids)
    {
        return baseConfigTabMapper.deleteBaseConfigTabByIds(ids);
    }

    /**
     * 删除基础配置分类信息
     * 
     * @param id 基础配置分类主键
     * @return 结果
     */
    @Override
    public int deleteBaseConfigTabById(Long id)
    {
        return baseConfigTabMapper.deleteBaseConfigTabById(id);
    }
}
