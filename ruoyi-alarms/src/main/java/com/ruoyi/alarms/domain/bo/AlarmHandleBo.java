package com.ruoyi.alarms.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/** 报警处理请求 */
@Data
public class AlarmHandleBo {

    /** 报警记录ID集合 */
    @NotEmpty(message = "请选择要处理的报警")
    private List<Long> ids;

    /** 处理状态：1已处理 2已忽略 */
    @NotNull(message = "处理状态不能为空")
    private Integer handleStatus;

    /** 处理备注 */
    private String handleRemark;
}
