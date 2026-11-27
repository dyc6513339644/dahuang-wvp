package com.ruoyi.alarms.support;

import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.constant.AlarmType;
import org.apache.commons.lang3.StringUtils;

/** 各协议原始报警码归一化，国标看 AlarmMethod+AlarmType，ISUP 看 dwAlarmType */
public class AlarmTypeResolver {

    private AlarmTypeResolver() {
    }

    /** 归一化报警类型 */
    public static AlarmType resolve(String protocolType, String alarmCode, String alarmMethod,
                                    String eventType, String alarmTypeCode) {
        if (StringUtils.isNotBlank(alarmTypeCode)) {
            return AlarmType.ofCode(alarmTypeCode);
        }
        if (AlarmConstants.PROTOCOL_GB28181.equals(protocolType)) {
            return resolveGb(alarmMethod, alarmCode);
        }
        if (AlarmConstants.PROTOCOL_ISUP.equals(protocolType)) {
            return resolveIsup(eventType, alarmCode);
        }
        return AlarmType.OTHER;
    }

    /** 国标报警类型归一化：报警方式为 2/5/6/8 时报警类型才有独立含义 */
    public static AlarmType resolveGb(String alarmMethod, String alarmType) {
        if (StringUtils.isBlank(alarmMethod) || StringUtils.isBlank(alarmType)) {
            return AlarmType.OTHER;
        }
        String method = alarmMethod.trim();
        String type = alarmType.trim();

        // 2：设备报警
        if (method.contains("2")) {
            switch (type) {
                case "1":
                    return AlarmType.VIDEO_LOSS;
                case "2":
                    return AlarmType.TAMPER;
                case "3":
                    return AlarmType.DISK_FULL;
                case "4":
                    return AlarmType.TEMP_HIGH;
                case "5":
                    return AlarmType.TEMP_LOW;
                default:
                    break;
            }
        }
        // 5：视频报警
        if (method.contains("5")) {
            switch (type) {
                case "1":
                    return AlarmType.MANUAL;
                case "2":
                    return AlarmType.MOTION;
                case "3":
                    return AlarmType.OBJECT_LEAVE;
                case "4":
                    return AlarmType.OBJECT_REMOVE;
                case "5":
                    return AlarmType.CROSS_LINE;
                case "6":
                    return AlarmType.INTRUSION;
                case "7":
                    return AlarmType.RETROGRADE;
                case "8":
                    return AlarmType.LOITER;
                case "9":
                    return AlarmType.TRAFFIC;
                case "10":
                    return AlarmType.DENSITY;
                case "11":
                    return AlarmType.VIDEO_ABNORMAL;
                case "12":
                    return AlarmType.FAST_MOVE;
                default:
                    break;
            }
        }
        // 6：设备故障报警
        if (method.contains("6")) {
            return AlarmType.DISK_FAULT;
        }
        // 8：智能分析报警
        if (method.contains("8")) {
            switch (type) {
                case "1":
                case "2":
                    return AlarmType.FACE;
                case "3":
                case "4":
                    return AlarmType.VEHICLE;
                case "5":
                    return AlarmType.PERSON;
                case "6":
                    return AlarmType.BEHAVIOR;
                case "7":
                    return AlarmType.OBJECT;
                case "8":
                    return AlarmType.CROWD;
                case "9":
                    return AlarmType.TRACK;
                default:
                    break;
            }
        }
        // 4：GPS 报警
        if (method.contains("4")) {
            return AlarmType.GPS;
        }
        return AlarmType.OTHER;
    }

    /** 海康报警类型归一化：优先按 ISAPI 事件类型，其次按 SDK 基础报警码 */
    public static AlarmType resolveIsup(String eventType, String alarmCode) {
        if (StringUtils.isNotBlank(eventType)) {
            switch (eventType.trim().toLowerCase()) {
                case "vmd":
                case "motion_detection":
                    return AlarmType.MOTION;
                case "videolost":
                case "video_loss":
                    return AlarmType.VIDEO_LOSS;
                case "shelteralarm":
                case "video_cover":
                    return AlarmType.TAMPER;
                case "linedetection":
                case "cross_line_detection":
                    return AlarmType.CROSS_LINE;
                case "fielddetection":
                case "area_intrusion":
                case "enter_area":
                    return AlarmType.INTRUSION;
                case "leave_area":
                    return AlarmType.INTRUSION;
                case "object_removal":
                case "removedetection":
                    return AlarmType.OBJECT_REMOVE;
                case "object_leave":
                case "parkingdetection":
                    return AlarmType.OBJECT_LEAVE;
                case "facedetection":
                case "face_detection":
                    return AlarmType.FACE;
                // 人脸抓拍：ISAPI 事件名有 faceCapture / faceSnap 等多种写法
                case "facecapture":
                case "face_capture":
                case "facesnap":
                case "face_snap":
                case "facesnapinfo":
                    return AlarmType.FACE_CAPTURE;
                case "accesscontrol":
                case "acs":
                    return AlarmType.ACCESS;
                default:
                    break;
            }
        }
        if (StringUtils.isNotBlank(alarmCode) && alarmCode.trim().matches("\\d+")) {
            switch (Integer.parseInt(alarmCode.trim())) {
                // EHOME_ALARM_FACESNAP_REPORT - 人脸抓拍
                case 3:
                    return AlarmType.FACE_CAPTURE;
                // EHOME_ALARM_FACETEMP - 人脸测温
                case 21:
                    return AlarmType.FACE;
                // EHOME_ALARM_ACS
                case 11:
                    return AlarmType.ACCESS;
                // ALARM_TYPE_RECORD_ABNORMAL
                case 706:
                    return AlarmType.RECORD_ABNORMAL;
                // 设备/通道/硬盘状态变化、定时状态
                case 700:
                case 701:
                case 702:
                case 703:
                case 704:
                case 705:
                    return AlarmType.DEVICE_FAULT;
                default:
                    return AlarmType.OTHER;
            }
        }
        return AlarmType.OTHER;
    }
}
