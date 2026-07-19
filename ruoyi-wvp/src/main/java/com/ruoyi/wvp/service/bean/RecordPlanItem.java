package com.ruoyi.wvp.service.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 录制计划项
 */
@Data
@TableName("wvp_record_plan_item")
public class RecordPlanItem {

    /**
     * 计划项数据库ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 计划开始时间的序号， 从0点开始，每半个小时增加1
     */
    @TableField("start")
    private Integer start;

    /**
     * 计划结束时间的序号， 从0点开始，每半个小时增加1
     */
    @TableField("stop")
    private Integer stop;

    /**
     * 计划周几执行
     */
    @TableField("week_day")
    private Integer weekDay;

    /**
     * 所属计划ID
     */
    @TableField("plan_id")
    private Long planId;

}
