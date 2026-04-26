package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 基础配置对象 sys_base_config
 * 
 * @author ruoyi
 * @date 2026-04-26
 */
public class BaseConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置id */
    private Integer id;

    /** 字段名称 */
    @Excel(name = "字段名称")
    private String menuName;

    /** 类型(文本框,单选按钮...) */
    @Excel(name = "类型(文本框,单选按钮...)")
    private String configType;

    /** 表单类型 */
    @Excel(name = "表单类型")
    private String inputType;

    /** 配置分类id */
    @Excel(name = "配置分类id")
    private Integer configTabId;

    /** 规则 单选框和多选框 */
    @Excel(name = "规则 单选框和多选框")
    private String parameter;

    /** 上传文件格式1单图2多图3文件 */
    @Excel(name = "上传文件格式1单图2多图3文件")
    private Integer uploadType;

    /** 规则 */
    @Excel(name = "规则")
    private String required;

    /** 多行文本框的宽度 */
    @Excel(name = "多行文本框的宽度")
    private Integer width;

    /** 多行文框的高度 */
    @Excel(name = "多行文框的高度")
    private Integer high;

    /** 默认值 */
    @Excel(name = "默认值")
    private String defaultValue;

    /** 配置名称 */
    @Excel(name = "配置名称")
    private String configInfo;

    /** 配置简介 */
    @Excel(name = "配置简介")
    private String configDesc;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sort;

    /** 是否隐藏 */
    @Excel(name = "是否隐藏")
    private Integer configStatus;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }

    public void setMenuName(String menuName) 
    {
        this.menuName = menuName;
    }

    public String getMenuName() 
    {
        return menuName;
    }

    public void setConfigType(String configType) 
    {
        this.configType = configType;
    }

    public String getConfigType() 
    {
        return configType;
    }

    public void setInputType(String inputType) 
    {
        this.inputType = inputType;
    }

    public String getInputType() 
    {
        return inputType;
    }

    public void setConfigTabId(Integer configTabId) 
    {
        this.configTabId = configTabId;
    }

    public Integer getConfigTabId() 
    {
        return configTabId;
    }

    public void setParameter(String parameter) 
    {
        this.parameter = parameter;
    }

    public String getParameter() 
    {
        return parameter;
    }

    public void setUploadType(Integer uploadType) 
    {
        this.uploadType = uploadType;
    }

    public Integer getUploadType() 
    {
        return uploadType;
    }

    public void setRequired(String required) 
    {
        this.required = required;
    }

    public String getRequired() 
    {
        return required;
    }

    public void setWidth(Integer width) 
    {
        this.width = width;
    }

    public Integer getWidth() 
    {
        return width;
    }

    public void setHigh(Integer high) 
    {
        this.high = high;
    }

    public Integer getHigh() 
    {
        return high;
    }

    public void setDefaultValue(String defaultValue) 
    {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() 
    {
        return defaultValue;
    }

    public void setConfigInfo(String configInfo) 
    {
        this.configInfo = configInfo;
    }

    public String getConfigInfo() 
    {
        return configInfo;
    }

    public void setConfigDesc(String configDesc) 
    {
        this.configDesc = configDesc;
    }

    public String getConfigDesc() 
    {
        return configDesc;
    }

    public void setSort(Integer sort) 
    {
        this.sort = sort;
    }

    public Integer getSort() 
    {
        return sort;
    }

    public void setConfigStatus(Integer configStatus) 
    {
        this.configStatus = configStatus;
    }

    public Integer getConfigStatus() 
    {
        return configStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("menuName", getMenuName())
            .append("configType", getConfigType())
            .append("inputType", getInputType())
            .append("configTabId", getConfigTabId())
            .append("parameter", getParameter())
            .append("uploadType", getUploadType())
            .append("required", getRequired())
            .append("width", getWidth())
            .append("high", getHigh())
            .append("defaultValue", getDefaultValue())
            .append("configInfo", getConfigInfo())
            .append("configDesc", getConfigDesc())
            .append("sort", getSort())
            .append("configStatus", getConfigStatus())
            .toString();
    }
}
