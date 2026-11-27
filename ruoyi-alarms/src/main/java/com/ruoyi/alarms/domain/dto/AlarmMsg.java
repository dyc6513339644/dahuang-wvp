package com.ruoyi.alarms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 统一报警接入报文，各协议适配器构造后交给 IAlarmIngestService 落库 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMsg {

    /** 接入协议：GB28181/ISUP/ONVIF/PUSH/PULL/OTHER */
    private String protocolType;

    /** 设备ID（国标为20位编码，ISUP 为序列号，入库前由本模块补前缀） */
    private String deviceId;

    /** 设备名称（可空，落库时按需从设备表补齐） */
    private String deviceName;

    /** 通道ID */
    private String channelId;

    /** 通道名称（可空） */
    private String channelName;

    /** 原始报警类型码 */
    private String alarmCode;

    /** 已归一化的报警类型编码，可空（空则由 AlarmTypeResolver 推导） */
    private String alarmType;

    /** 国标报警方式 */
    private String alarmMethod;

    /** ISUP/ISAPI 事件类型字符串 */
    private String eventType;

    /** 指定报警级别，可空（空则取报警类型的默认级别） */
    private Integer alarmLevel;

    /** 报警发生时间 */
    private String alarmTime;

    /** 报警描述 */
    private String alarmDesc;

    /** 经度 */
    private Double longitude;

    /** 纬度 */
    private Double latitude;

    /** 报警图片地址 */
    private String imageUrl;

    /** 原始报文：只写入 wvp_alarms_raw 中间表留档，不进 wvp_alarms */
    private String rawPayload;
}
