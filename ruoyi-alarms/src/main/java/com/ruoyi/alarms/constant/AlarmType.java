package com.ruoyi.alarms.constant;

/**
 * 归一化报警类型，各协议原始报警码统一映射到本枚举。
 * <p>
 * 本枚举只保留 {@code code}（入库与协议映射依据）和 {@code defaultLevel}（缺省级别）；
 * 展示名称以字典 {@code alarm_type} 为准（见 {@link AlarmDictType#ALARM_TYPE}），代码不维护名称副本。
 */
public enum AlarmType {

    /** 视频丢失 */
    VIDEO_LOSS("video_loss", AlarmConstants.LEVEL_CRITICAL),
    /** 设备防拆 / 视频遮挡 */
    TAMPER("tamper", AlarmConstants.LEVEL_CRITICAL),
    /** 存储设备磁盘满 */
    DISK_FULL("disk_full", AlarmConstants.LEVEL_MAJOR),
    /** 存储设备故障（磁盘/风扇） */
    DISK_FAULT("disk_fault", AlarmConstants.LEVEL_CRITICAL),
    /** 设备高温 */
    TEMP_HIGH("temp_high", AlarmConstants.LEVEL_MINOR),
    /** 设备低温 */
    TEMP_LOW("temp_low", AlarmConstants.LEVEL_MINOR),
    /** 移动侦测 */
    MOTION("motion", AlarmConstants.LEVEL_MINOR),
    /** 区域入侵 */
    INTRUSION("intrusion", AlarmConstants.LEVEL_MAJOR),
    /** 越界/绊线检测 */
    CROSS_LINE("cross_line", AlarmConstants.LEVEL_MAJOR),
    /** 物品遗留 */
    OBJECT_LEAVE("object_leave", AlarmConstants.LEVEL_MINOR),
    /** 物品移除 */
    OBJECT_REMOVE("object_remove", AlarmConstants.LEVEL_MAJOR),
    /** 逆行检测 */
    RETROGRADE("retrograde", AlarmConstants.LEVEL_MAJOR),
    /** 徘徊检测 */
    LOITER("loiter", AlarmConstants.LEVEL_MINOR),
    /** 流量统计 */
    TRAFFIC("traffic", AlarmConstants.LEVEL_MINOR),
    /** 密度检测 */
    DENSITY("density", AlarmConstants.LEVEL_MINOR),
    /** 视频异常 */
    VIDEO_ABNORMAL("video_abnormal", AlarmConstants.LEVEL_MINOR),
    /** 快速移动 */
    FAST_MOVE("fast_move", AlarmConstants.LEVEL_MINOR),
    /** 人脸检测/识别 */
    FACE("face", AlarmConstants.LEVEL_MAJOR),
    /** 人脸抓拍（海康 ISUP/ISAPI 上报的人脸抓拍图片） */
    FACE_CAPTURE("face_capture", AlarmConstants.LEVEL_MAJOR),
    /** 人体检测 */
    PERSON("person", AlarmConstants.LEVEL_MINOR),
    /** 车辆检测/识别 */
    VEHICLE("vehicle", AlarmConstants.LEVEL_MINOR),
    /** 行为分析 */
    BEHAVIOR("behavior", AlarmConstants.LEVEL_MINOR),
    /** 物品检测 */
    OBJECT("object", AlarmConstants.LEVEL_MINOR),
    /** 人群密度 */
    CROWD("crowd", AlarmConstants.LEVEL_MINOR),
    /** 目标跟踪 */
    TRACK("track", AlarmConstants.LEVEL_MINOR),
    /** 人工视频报警 */
    MANUAL("manual", AlarmConstants.LEVEL_MAJOR),
    /** 门禁报警 */
    ACCESS("access", AlarmConstants.LEVEL_MAJOR),
    /** GPS 报警 */
    GPS("gps", AlarmConstants.LEVEL_MINOR),
    /** 设备故障 */
    DEVICE_FAULT("device_fault", AlarmConstants.LEVEL_CRITICAL),
    /** 录像异常 */
    RECORD_ABNORMAL("record_abnormal", AlarmConstants.LEVEL_MAJOR),
    /** 外来报警（上级平台转发/第三方） */
    EXTERNAL("external", AlarmConstants.LEVEL_MINOR),
    /** 其它/未知 */
    OTHER("other", AlarmConstants.LEVEL_INFO);

    private final String code;
    private final int defaultLevel;

    AlarmType(String code, int defaultLevel) {
        this.code = code;
        this.defaultLevel = defaultLevel;
    }

    public String getCode() {
        return code;
    }

    public int getDefaultLevel() {
        return defaultLevel;
    }

    /** 按归一化编码查找类型，未命中返回 {@link #OTHER} */
    public static AlarmType ofCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return OTHER;
        }
        String target = code.trim();
        for (AlarmType type : values()) {
            if (type.code.equalsIgnoreCase(target)) {
                return type;
            }
        }
        return OTHER;
    }
}
