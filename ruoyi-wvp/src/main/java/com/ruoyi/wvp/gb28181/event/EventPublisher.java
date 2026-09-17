package com.ruoyi.wvp.gb28181.event;


import com.ruoyi.wvp.gb28181.bean.*;
import com.ruoyi.wvp.gb28181.event.alarm.AlarmEvent;
import com.ruoyi.wvp.gb28181.event.record.RecordEndEvent;
import com.ruoyi.wvp.gb28181.event.subscribe.catalog.CatalogEvent;
import com.ruoyi.wvp.gb28181.event.subscribe.mobilePosition.MobilePositionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:Event事件通知推送器，支持推送报警事件、目录事件、移动位置事件、录像结束事件
 *              注：媒体节点在线/离线事件已改由 ZLMMediaServerStatusManager 直接使用
 *              ApplicationEventPublisher 发布，不再经由本类
 * @author: swwheihei
 * @date:   2020年5月6日 上午11:30:50
 */
@Component
public class EventPublisher {

	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * 设备报警事件
	 * @param deviceAlarm
	 */
	public void deviceAlarmEventPublish(DeviceAlarm deviceAlarm) {
		AlarmEvent alarmEvent = new AlarmEvent(this);
		alarmEvent.setAlarmInfo(deviceAlarm);
		applicationEventPublisher.publishEvent(alarmEvent);
	}


	public void catalogEventPublish(Platform platform, CommonGBChannel deviceChannel, String type) {
		List<CommonGBChannel> deviceChannelList = new ArrayList<>();
		deviceChannelList.add(deviceChannel);
		catalogEventPublish(platform, deviceChannelList, type);
	}

	public void catalogEventPublish(Platform platform, List<CommonGBChannel> deviceChannels, String type) {
		CatalogEvent outEvent = new CatalogEvent(this);
		List<CommonGBChannel> channels = new ArrayList<>();
		if (deviceChannels.size() > 1) {
			// 数据去重
			Set<String> gbIdSet = new HashSet<>();
			for (CommonGBChannel deviceChannel : deviceChannels) {
				if (deviceChannel != null && deviceChannel.getGbDeviceId() != null && !gbIdSet.contains(deviceChannel.getGbDeviceId())) {
					gbIdSet.add(deviceChannel.getGbDeviceId());
					channels.add(deviceChannel);
				}
			}
		}else {
			channels = deviceChannels;
		}
		outEvent.setChannels(channels);
		outEvent.setType(type);
		outEvent.setPlatform(platform);
		applicationEventPublisher.publishEvent(outEvent);
	}

	public void mobilePositionEventPublish(MobilePosition mobilePosition) {
		MobilePositionEvent event = new MobilePositionEvent(this);
		event.setMobilePosition(mobilePosition);
		applicationEventPublisher.publishEvent(event);
	}

	public void recordEndEventPush(RecordInfo recordInfo) {
		RecordEndEvent outEvent = new RecordEndEvent(this);
		outEvent.setRecordInfo(recordInfo);
		applicationEventPublisher.publishEvent(outEvent);
	}
}
