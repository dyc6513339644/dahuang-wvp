package com.ruoyi.wvp.gb28181.event.alarm;

import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.service.IAlarmIngestService;
import com.ruoyi.wvp.gb28181.bean.DeviceAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 国标报警 → 统一报警模块（wvp_alarms）入库桥接
 * <p>
 * 国标报警已统一落库到 wvp_alarms，旧表 wvp_device_alarm 不再写入；
 * {@link AlarmEvent} 仅保留给 SSE 实时推送使用，不再承担入库职责。
 * <p>
 * 调用点：AlarmNotifyMessageHandler（Message 方式上报）、NotifyRequestProcessor（Notify 方式上报），
 * 两者都受 sipConfig.isAlarm() 控制，与原有落库开关保持一致。
 *
 * @author ruoyi
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GbAlarmIngestBridge {

    private final IAlarmIngestService alarmIngestService;

    /** 转换并投递统一报警，异常一律吞掉，不影响国标报警主流程 */
    public void ingest(DeviceAlarm alarm) {
        if (alarm == null || alarm.getDeviceId() == null) {
            return;
        }
        try {
            AlarmMsg msg = AlarmMsg.builder()
                    .protocolType(AlarmConstants.PROTOCOL_GB28181)
                    .deviceId(alarm.getDeviceId())
                    .channelId(alarm.getChannelId())
                    .channelName(alarm.getChannelName())
                    .alarmCode(alarm.getAlarmType())
                    .alarmMethod(alarm.getAlarmMethod())
                    .alarmLevel(parseLevel(alarm.getAlarmPriority()))
                    .alarmTime(alarm.getAlarmTime())
                    .alarmDesc(alarm.getAlarmDescription())
                    .longitude(alarm.getLongitude())
                    .latitude(alarm.getLatitude())
                    .build();
            alarmIngestService.ingest(msg);
        } catch (Exception e) {
            // 统一报警入库不得影响国标报警主流程
            log.error("[统一报警] 国标报警入库异常 deviceId={}", alarm.getDeviceId(), e);
        }
    }

    /**
     * 国标报警级别 1-4 直接作为统一级别（1 最高）
     */
    private Integer parseLevel(String priority) {
        if (priority == null) {
            return null;
        }
        try {
            int level = Integer.parseInt(priority.trim());
            return level >= AlarmConstants.LEVEL_CRITICAL && level <= AlarmConstants.LEVEL_INFO ? level : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
