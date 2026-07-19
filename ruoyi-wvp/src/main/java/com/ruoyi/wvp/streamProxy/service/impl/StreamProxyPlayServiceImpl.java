package com.ruoyi.wvp.streamProxy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruoyi.wvp.common.StreamInfo;
import com.ruoyi.wvp.conf.DynamicTask;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.media.bean.MediaInfo;
import com.ruoyi.wvp.media.bean.MediaServer;
import com.ruoyi.wvp.media.event.hook.Hook;
import com.ruoyi.wvp.media.event.hook.HookSubscribe;
import com.ruoyi.wvp.media.event.hook.HookType;
import com.ruoyi.wvp.media.event.media.MediaArrivalEvent;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.mapper.DeviceMapper;
import com.ruoyi.wvp.service.bean.ErrorCallback;
import com.ruoyi.wvp.service.bean.InviteErrorCode;
import com.ruoyi.wvp.streamProxy.bean.StreamProxy;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyPlayService;
import com.ruoyi.common.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import javax.sip.message.Response;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 视频代理播放业务（基于 Device 表）
 */
@Slf4j
@Service
@DS("master")
public class StreamProxyPlayServiceImpl implements IStreamProxyPlayService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private HookSubscribe subscribe;

    @Autowired
    private DynamicTask dynamicTask;

    @Autowired
    private UserSetting userSetting;

    private ConcurrentHashMap<Integer, ErrorCallback<StreamInfo>> callbackMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer, StreamInfo> streamInfoMap = new ConcurrentHashMap<>();

    /**
     * 流到来的处理
     */
    @Async("taskExecutor")
    @Transactional
    @EventListener
    public void onApplicationEvent(MediaArrivalEvent event) {
        if ("rtsp".equals(event.getSchema())) {
            Device device = deviceMapper.getProxyDeviceByDeviceId(event.getApp());
            if (device == null) {
                return;
            }
            StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
            ErrorCallback<StreamInfo> callback = callbackMap.remove(streamProxy.getId());
            StreamInfo streamInfo = streamInfoMap.remove(streamProxy.getId());
            if (callback != null && streamInfo != null) {
                callback.run(InviteErrorCode.SUCCESS.getCode(), InviteErrorCode.SUCCESS.getMsg(), streamInfo);
            }
        }
    }

    @Override
    public void start(int id, ErrorCallback<StreamInfo> callback) {
        Device device = deviceMapper.query(id);
        if (device == null || !"STREAM_PROXY".equals(device.getProtocolType())) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "代理信息未找到");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        StreamInfo streamInfo = startProxy(streamProxy);
        if (streamInfo == null) {
            callback.run(Response.BUSY_HERE, "busy here", null);
            return;
        }
        callbackMap.put(id, callback);
        streamInfoMap.put(id, streamInfo);

        MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
        if (mediaServer != null) {
            MediaInfo mediaInfo = mediaServerService.getMediaInfo(mediaServer, streamProxy.getApp(), streamProxy.getStream());
            if (mediaInfo != null) {
                callbackMap.remove(id);
                streamInfoMap.remove(id);
                callback.run(InviteErrorCode.SUCCESS.getCode(), InviteErrorCode.SUCCESS.getMsg(), streamInfo);
            }
        }
    }

    @Override
    public StreamInfo start(int id, Boolean record, ErrorCallback<StreamInfo> callback) {
        Device device = deviceMapper.query(id);
        if (device == null || !"STREAM_PROXY".equals(device.getProtocolType())) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "代理信息未找到");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);

        log.info("[拉流代理-播放] start调用: deviceId={}, app={}, stream={}, record参数={}, DB中enableMp4={}",
                id, streamProxy.getApp(), streamProxy.getStream(), record, streamProxy.isEnableMp4());

        // 录像计划：流已在线但录制开关不匹配 → 关闭旧流并重新拉取
        if (record != null && !record.equals(streamProxy.isEnableMp4())) {
            log.info("[拉流代理-录像计划] 检测到录制状态不匹配: record={}, enableMp4={}, 准备检查流状态",
                    record, streamProxy.isEnableMp4());
            MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
            if (mediaServer != null) {
                MediaInfo mediaInfo = mediaServerService.getMediaInfo(mediaServer, streamProxy.getApp(), streamProxy.getStream());
                if (mediaInfo != null) {
                    log.info("[拉流代理-录像计划] 流已在线，录制状态变更({}→{})，关闭旧流并重新拉取: app={}, stream={}, mediaServerId={}",
                            streamProxy.isEnableMp4(), record, streamProxy.getApp(), streamProxy.getStream(), mediaServer.getId());
                    // 1. 关闭旧流（未开启录制的流）
                    mediaServerService.closeStreams(mediaServer, streamProxy.getApp(), streamProxy.getStream());
                    log.info("[拉流代理-录像计划] 旧流已关闭");
                    // 2. 更新 DB 中的 enableMp4，确保后续调用不再进入此分支
                    device.setEnableMp4(record);
                    device.setUpdateTime(com.ruoyi.wvp.utils.DateUtil.getNow());
                    deviceMapper.updateProxyDevice(device);
                    log.info("[拉流代理-录像计划] DB enableMp4 已更新为 {}", record);
                    // 3. 同步更新内存中的 streamProxy，供下方 startProxy 使用
                    streamProxy.setEnableMp4(record);
                    // 4. 继续执行下方 startProxy → 以新的 enableMp4 重新拉流
                } else {
                    log.info("[拉流代理-录像计划] 录制状态不匹配但流不在线，将由 startProxy 直接以新状态拉取");
                }
            } else {
                log.info("[拉流代理-录像计划] 录制状态不匹配但 mediaServer 不存在，跳过关闭");
            }
        }

        if (record != null) {
            streamProxy.setEnableMp4(record);
        }

        StreamInfo streamInfo = startProxy(streamProxy);
        log.info("[拉流代理-播放] startProxy 返回: app={}, stream={}, mediaServer={}, enableMp4={}",
                streamProxy.getApp(), streamProxy.getStream(),
                streamInfo != null ? streamInfo.getMediaServer().getId() : "null",
                streamProxy.isEnableMp4());
        if (callback != null) {
            // 检查流是否已经存在，如果存在则直接回调，不等待 on_media_arrival 事件
            MediaServer mediaServer = streamInfo.getMediaServer();
            if (mediaServer != null) {
                MediaInfo mediaInfo = mediaServerService.getMediaInfo(mediaServer, streamProxy.getApp(), streamProxy.getStream());
                if (mediaInfo != null) {
                    callback.run(InviteErrorCode.SUCCESS.getCode(), InviteErrorCode.SUCCESS.getMsg(), streamInfo);
                    return streamInfo;
                }
            }
            // 流不存在，等待 on_media_arrival 事件
            String timeOutTaskKey = UUID.randomUUID().toString();
            Hook rtpHook = Hook.getInstance(HookType.on_media_arrival, streamProxy.getApp(), streamProxy.getStream(), streamInfo.getMediaServer().getId());
            dynamicTask.startDelay(timeOutTaskKey, () -> {
                subscribe.removeSubscribe(rtpHook);
                callback.run(InviteErrorCode.ERROR_FOR_STREAM_TIMEOUT.getCode(), InviteErrorCode.ERROR_FOR_STREAM_TIMEOUT.getMsg(), streamInfo);
            }, userSetting.getPlayTimeout());

            subscribe.addSubscribe(rtpHook, (hookData) -> {
                dynamicTask.stop(timeOutTaskKey);
                callback.run(InviteErrorCode.SUCCESS.getCode(), InviteErrorCode.SUCCESS.getMsg(), streamInfo);
                subscribe.removeSubscribe(rtpHook);
            });
        }
        return streamInfo;
    }

    @Override
    public StreamInfo startProxy(StreamProxy streamProxy) {
        if (!streamProxy.isEnable()) {
            return null;
        }
        MediaServer mediaServer;
        String mediaServerId = streamProxy.getRelatesMediaServerId();
        if (mediaServerId == null) {
            mediaServer = mediaServerService.getMediaServerForMinimumLoad(null);
        } else {
            mediaServer = mediaServerService.getOne(mediaServerId);
        }
        if (mediaServer == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), mediaServerId == null ? "未找到可用的媒体节点" : "未找到节点" + mediaServerId);
        }
        StreamInfo streamInfo = mediaServerService.startProxy(mediaServer, streamProxy);
        // 更新 Device 的 stream_key、media_server_id 和在线状态
        Device device = new Device();
        device.setDeviceId(streamProxy.getApp());
        device.setStreamKey(streamProxy.getStreamKey());
        device.setMediaServerId(mediaServer.getId());
        device.setOnLine(true);
        device.setUpdateTime(com.ruoyi.wvp.utils.DateUtil.getNow());
        deviceMapper.updateProxyStream(device);
        return streamInfo;
    }

    @Override
    public void stop(int id) {
        Device device = deviceMapper.query(id);
        if (device == null || !"STREAM_PROXY".equals(device.getProtocolType())) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "代理信息未找到");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        stopProxy(streamProxy);
    }

    @Override
    public void stopProxy(StreamProxy streamProxy) {
        String mediaServerId = streamProxy.getMediaServerId();
        Assert.notNull(mediaServerId, "代理节点不存在");

        MediaServer mediaServer = mediaServerService.getOne(mediaServerId);
        if (mediaServer == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "媒体节点不存在");
        }
        if (ObjectUtils.isEmpty(streamProxy.getStreamKey())) {
            mediaServerService.closeStreams(mediaServer, streamProxy.getApp(), streamProxy.getStream());
        } else {
            mediaServerService.stopProxy(mediaServer, streamProxy.getStreamKey());
        }
        // 清除 Device 的 stream_key 和 media_server_id
        Device device = new Device();
        device.setDeviceId(streamProxy.getApp());
        deviceMapper.removeProxyStream(device);
    }
}
