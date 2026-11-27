package com.ruoyi.alarms.domain.ref;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/** 报警过滤规则，直接复用字典表 sys_dict_data（dict_type = alarm_filter） */
@Data
@TableName("sys_dict_data")
public class AlarmFilterItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @TableId(value = "dict_code", type = IdType.AUTO)
    private Long dictCode;

    /** 排序 */
    private Long dictSort;

    /** 规则说明（字典标签） */
    private String dictLabel;

    /** 过滤的原始码（字典键值） */
    private String dictValue;

    /** 字典类型，固定 alarm_filter */
    private String dictType;

    /** 状态：0 正常 1 停用 */
    private String status;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private Date createTime;

    /** 更新时间 */
    private Date updateTime;
}
