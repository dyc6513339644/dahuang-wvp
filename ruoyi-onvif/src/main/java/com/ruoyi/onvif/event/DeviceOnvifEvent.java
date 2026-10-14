package com.ruoyi.onvif.event;

import org.springframework.context.ApplicationEvent;

/**
 * ONVIF设备事件
 */
public class DeviceOnvifEvent extends ApplicationEvent {

    public enum EventType {
        ADD,               // 新增设备 → 删旧通道，全量重建
        UPDATE,            // 更新设备 → 通道存在则更新，不存在则插入
        REFRESH_CHANNELS   // 刷新通道 → 存在则更新，不存在则插入（不删旧通道）
    }

    private Long id;
    private EventType type;

    public DeviceOnvifEvent(Object source, Long id, EventType type) {
        super(source);
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public EventType getType() {
        return type;
    }
}
