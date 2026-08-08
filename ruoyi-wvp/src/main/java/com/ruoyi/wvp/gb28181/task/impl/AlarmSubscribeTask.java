package com.ruoyi.wvp.gb28181.task.impl;

import com.ruoyi.wvp.common.CommonCallback;
import com.ruoyi.wvp.conf.DynamicTask;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.task.ISubscribeTask;
import com.ruoyi.wvp.gb28181.transmit.cmd.ISIPCommander;
import lombok.extern.slf4j.Slf4j;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import java.text.ParseException;

/**
 * 报警订阅的定时更新
 * @author lin
 */
@Slf4j
public class AlarmSubscribeTask implements ISubscribeTask {
    private final Device device;
    private final ISIPCommander sipCommander;
    private final DynamicTask dynamicTask;
    private final String taskKey = "alarm-subscribe-timeout";

    public AlarmSubscribeTask(Device device, ISIPCommander sipCommander, DynamicTask dynamicTask) {
        this.device = device;
        this.sipCommander = sipCommander;
        this.dynamicTask = dynamicTask;
    }

    @Override
    public void run() {
        if (dynamicTask.get(taskKey) != null) {
            dynamicTask.stop(taskKey);
        }
        try {
            sipCommander.alarmSubscribe(device,
                    device.getSubscribeCycleForAlarm(),
                    null, null, null, null, null);
            log.info("[报警订阅]成功： {}", device.getDeviceId());
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.error("[命令发送失败] 报警订阅: {} - {}", device.getDeviceId(), e.getMessage());
            dynamicTask.startDelay(taskKey, AlarmSubscribeTask.this, 2000);
        }
    }

    @Override
    public void stop(CommonCallback<Boolean> callback) {
        if (dynamicTask.get(taskKey) != null) {
            dynamicTask.stop(taskKey);
        }
        device.setSubscribeCycleForAlarm(0);
        try {
            sipCommander.alarmSubscribe(device, 0,
                    null, null, null, null, null);
            log.info("[取消报警订阅]成功： {}", device.getDeviceId());
            if (callback != null) {
                callback.run(true);
            }
        } catch (InvalidArgumentException | SipException | ParseException e) {
            log.warn("[取消报警订阅]失败，信令发送失败： {} - {}", device.getDeviceId(), e.getMessage());
            if (callback != null) {
                callback.run(false);
            }
        }
    }
}
