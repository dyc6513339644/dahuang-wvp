package com.ruoyi.alarms.domain.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/** 报警清理请求（按时间批量删除） */
@Data
public class AlarmCleanBo {

    /** 截止时间 yyyy-MM-dd HH:mm:ss，早于该时间的报警会被删除 */
    @NotBlank(message = "请选择截止时间")
    private String beforeTime;

    /** 限定协议，空表示全部 */
    private String protocolType;

    /** 限定处理状态，空表示全部 */
    private Integer handleStatus;

    /** 限定报警类型，空表示全部 */
    private String alarmType;
}
