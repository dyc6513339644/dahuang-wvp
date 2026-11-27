package com.ruoyi.alarms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.alarms.constant.AlarmDictType;
import com.ruoyi.alarms.support.AlarmDictUtils;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** 统一报警对象，原始码存 alarm_code，归一化类型存 alarm_type */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wvp_alarms")
public class AlarmRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 接入协议：GB28181/ISUP/ONVIF/PUSH/PULL/OTHER */
    private String protocolType;

    /** 平台设备ID（国标20位编码 或 haikang_isup_序列号） */
    private String deviceId;

    /** 设备名称快照 */
    private String deviceName;

    /** 平台通道ID */
    private String channelId;

    /** 通道名称快照 */
    private String channelName;

    /** 原始报警码（GB AlarmType / ISUP dwAlarmType / ISAPI eventType） */
    private String alarmCode;

    /** 归一化报警类型编码 */
    private String alarmType;

    /** 入库时的类型名称快照（展示走字典，见 {@link #getAlarmTypeName()}） */
    private String alarmTypeName;

    /** 报警级别：1紧急 2重要 3次要 4提示 */
    private Integer alarmLevel;

    /** 国标报警方式（1电话 2设备 3短信 4GPS 5视频 6故障 7其他 8智能分析） */
    private String alarmMethod;

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

    /** 处理状态：0未处理 1已处理 2已忽略 */
    private Integer handleStatus;

    /** 处理人 */
    private String handleBy;

    /** 处理时间 */
    private String handleTime;

    /** 处理备注 */
    private String handleRemark;

    /**
     * 报警类型名称（非表字段）：始终取字典 alarm_type 的最新名称。
     * <p>
     * 表里的 alarm_type_name 只是入库时的快照，仅用于字典项被删除后回溯，不参与展示。
     */
    public String getAlarmTypeName() {
        return AlarmDictUtils.label(AlarmDictType.ALARM_TYPE, alarmType);
    }

    /** 协议名称（非表字段）：取字典 protocol_type */
    public String getProtocolName() {
        return AlarmDictUtils.label(AlarmDictType.PROTOCOL_TYPE, protocolType);
    }

    /** 报警级别名称（非表字段）：取字典 alarm_level */
    public String getAlarmLevelName() {
        return AlarmDictUtils.label(AlarmDictType.ALARM_LEVEL, alarmLevel);
    }

    /** 处理状态名称（非表字段）：取字典 alarm_handle_status */
    public String getHandleStatusName() {
        return AlarmDictUtils.label(AlarmDictType.HANDLE_STATUS, handleStatus);
    }
}
