package com.ruoyi.alarms.domain.vo;

import lombok.Data;

/** 报警查询条件 */
@Data
public class AlarmQueryVo {

    /** 页码 */
    private Integer pageNum = 1;

    /** 每页条数 */
    private Integer pageSize = 15;

    /** 接入协议 */
    private String protocolType;

    /** 设备ID（精确） */
    private String deviceId;

    /** 通道ID（精确） */
    private String channelId;

    /** 归一化报警类型 */
    private String alarmType;

    /** 报警级别 */
    private Integer alarmLevel;

    /** 处理状态 */
    private Integer handleStatus;

    /** 关键字：设备编号/设备名称/通道名称 */
    private String keyword;

    /** 报警开始时间 yyyy-MM-dd HH:mm:ss */
    private String startTime;

    /** 报警结束时间 yyyy-MM-dd HH:mm:ss */
    private String endTime;

    /** 排序字段：alarm_time / create_time / alarm_level，默认 alarm_time */
    private String orderBy;
}
