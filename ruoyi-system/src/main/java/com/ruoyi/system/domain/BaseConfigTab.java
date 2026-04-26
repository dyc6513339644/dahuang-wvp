package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 基础配置分类对象 sys_base_config_tab
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
public class BaseConfigTab extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置分类id */
    private Long id;

    /** 上级分类id */
    private Long pid;

    /** 配置分类名称 */
    @Excel(name = "配置分类名称")
    private String title;

    /** 配置分类状态 */
    @Excel(name = "配置分类状态")
    private Integer status;

    /** 配置分类是否显示 */
    @Excel(name = "配置分类是否显示")
    private Integer info;

    /** 排序 */
    @Excel(name = "排序")
    private Long sort;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setPid(Long pid) 
    {
        this.pid = pid;
    }

    public Long getPid() 
    {
        return pid;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setInfo(Integer info) 
    {
        this.info = info;
    }

    public Integer getInfo() 
    {
        return info;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("pid", getPid())
            .append("title", getTitle())
            .append("status", getStatus())
            .append("info", getInfo())
            .append("sort", getSort())
            .toString();
    }
}
