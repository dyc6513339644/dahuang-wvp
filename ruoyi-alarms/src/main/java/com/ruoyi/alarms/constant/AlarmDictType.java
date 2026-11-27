package com.ruoyi.alarms.constant;

/**
 * 统一报警模块使用的字典类型（对应 sys_dict_type.dict_type）。
 */
public class AlarmDictType {

    private AlarmDictType() {
    }

    /** 归一化报警类型：dict_value = 类型编码（video_loss 等），dict_label = 展示名称 */
    public static final String ALARM_TYPE = "alarm_type";

    /** 报警级别：1紧急 2重要 3次要 4提示 */
    public static final String ALARM_LEVEL = "alarm_level";

    /** 处理状态：0未处理 1已处理 2已忽略 */
    public static final String HANDLE_STATUS = "alarm_handle_status";

    /**
     * 协议类型：GB28181/ISUP/ONVIF/PUSH/PULL/OTHER。
     * <p>
     * 设备、通道、报警共用，故 dictType 不带 alarms 前缀，其他模块可直接引用。
     */
    public static final String PROTOCOL_TYPE = "protocol_type";
}
