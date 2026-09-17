package com.ruoyi.wvp.streamPush.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.service.IGbChannelService;
import com.ruoyi.wvp.mapper.CommonGBChannelMapper;
import com.ruoyi.wvp.mapper.DeviceMapper;
import com.ruoyi.wvp.mapper.StreamPushMapper;
import com.ruoyi.media.domain.MediaInfo;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.event.media.MediaArrivalEvent;
import com.ruoyi.media.event.media.MediaDepartureEvent;
import com.ruoyi.media.event.mediaServer.MediaServerOfflineEvent;
import com.ruoyi.media.event.mediaServer.MediaServerOnlineEvent;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.media.zlm.dto.StreamAuthorityInfo;
import com.ruoyi.media.zlm.dto.hook.OriginType;
import com.ruoyi.wvp.service.ISendRtpServerService;
import com.ruoyi.wvp.service.bean.GPSMsgInfo;
import com.ruoyi.wvp.service.bean.StreamPushItemFromRedis;
import com.ruoyi.wvp.storager.IRedisCatchStorage;
import com.ruoyi.wvp.streamPush.bean.StreamPush;
import com.ruoyi.wvp.streamPush.cache.TalkCacheItem;
import com.ruoyi.wvp.streamPush.cache.TalkCacheManager;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import com.ruoyi.wvp.utils.DateUtil;
import com.ruoyi.wvp.vmanager.bean.ResourceBaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
@DS("master")
public class StreamPushServiceImpl implements IStreamPushService {

    @Autowired
    private StreamPushMapper streamPushMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private CommonGBChannelMapper commonGBChannelMapper;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private ISendRtpServerService sendRtpServerService;

    @Autowired
    private TalkCacheManager talkCacheManager;

    @Autowired
    private IGbChannelService gbChannelService;

    /**
     * 流到来的处理
     * deviceId = app（即 wvp_device.device_id）
     * channelDeviceId = stream = app_01（即 wvp_device_channel.device_id）
     */
    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void onApplicationEvent(MediaArrivalEvent event) {
        MediaInfo mediaInfo = event.getMediaInfo();
        if (mediaInfo == null) {
            return;
        }
        if (mediaInfo.getOriginType() != OriginType.RTMP_PUSH.ordinal()
                && mediaInfo.getOriginType() != OriginType.RTSP_PUSH.ordinal()
                && mediaInfo.getOriginType() != OriginType.RTC_PUSH.ordinal()) {
            return;
        }

        StreamAuthorityInfo streamAuthorityInfo = redisCatchStorage.getStreamAuthorityInfo(event.getApp(), event.getStream());
        if (streamAuthorityInfo == null) {
            streamAuthorityInfo = StreamAuthorityInfo.getInstanceByHook(event);
        } else {
            streamAuthorityInfo.setOriginType(mediaInfo.getOriginType());
        }
        redisCatchStorage.updateStreamAuthorityInfo(event.getApp(), event.getStream(), streamAuthorityInfo);

        // deviceId = app_stream（语音/广播对讲等 app 固定的流类型，用 app+stream 保证 device_id 唯一）
        String deviceId = event.getApp() + "_" + event.getStream();

        // 语音/广播对讲流走纯内存缓存，不落DB（临时流无需持久化）
        boolean isBroadcastOrTalk = "broadcast".equals(event.getApp()) || "talk".equals(event.getApp());
        if (isBroadcastOrTalk) {
            talkCacheManager.put(event.getApp(), event.getStream(), event.getApp(),
                    event.getMediaServer().getId(), DateUtil.getNow());
            log.info("[语音流上线-缓存] app: {}, stream: {}, deviceId: {}", event.getApp(), event.getStream(), deviceId);
        } else {
            handlePushArrival(event, deviceId);
        }

        // 冗余数据，自己系统中自用
        if (!"broadcast".equals(event.getApp()) && !"talk".equals(event.getApp())) {
            redisCatchStorage.addPushListItem(event.getApp(), event.getStream(), event.getMediaInfo());
        }

        // 发送流变化redis消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverId", userSetting.getServerId());
        jsonObject.put("app", event.getApp());
        jsonObject.put("stream", event.getStream());
        jsonObject.put("register", true);
        jsonObject.put("mediaServerId", event.getMediaServer().getId());
        redisCatchStorage.sendStreamChangeMsg(OriginType.values()[event.getMediaInfo().getOriginType()].getType(), jsonObject);
    }

    /**
     * 普通推流上线（写DB）
     */
    private void handlePushArrival(MediaArrivalEvent event, String deviceId) {
        Device deviceInDb = deviceMapper.getPushDeviceByDeviceId(deviceId);

        if (deviceInDb == null) {
            // 自动创建推流设备
            Device device = new Device();
            device.setDeviceId(deviceId);
            device.setName(event.getApp());
            device.setMediaServerId(event.getMediaServer().getId());
            device.setOnLine(true);
            device.setStartOfflinePush(true);
            device.setCreateTime(DateUtil.getNow());
            device.setUpdateTime(DateUtil.getNow());
            device.setPushTime(DateUtil.getNow());
            deviceMapper.addPushDevice(device);

            // 创建通道，channel deviceId = stream
            String channelDeviceId = event.getStream();
            CommonGBChannel gbChannel = new CommonGBChannel();
            gbChannel.setDeviceId(channelDeviceId);
            gbChannel.setName(device.getName());
            gbChannel.setParentId(deviceId);
            gbChannel.setDataType(ChannelDataType.STREAM_PUSH.value);
            gbChannel.setDataDeviceId(device.getId());
            gbChannel.setStatus("ON");
            gbChannel.setCreateTime(DateUtil.getNow());
            gbChannel.setUpdateTime(DateUtil.getNow());
            gbChannelService.add(gbChannel);
            log.info("[推流上线-新增] device: {}, channel: {}", deviceId, channelDeviceId);
        } else {
            // 更新推流状态
            deviceInDb.setMediaServerId(event.getMediaServer().getId());
            deviceInDb.setOnLine(true);
            deviceInDb.setPushTime(DateUtil.getNow());
            deviceInDb.setUpdateTime(DateUtil.getNow());
            deviceMapper.updatePushDevice(deviceInDb);

            // 同步通道状态，channel deviceId = stream
            String channelDeviceId = event.getStream();
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            if (channel != null && !"ON".equals(channel.getGbStatus())) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), "ON");
            }
            log.info("[推流上线-更新] device: {}", deviceId);
        }
    }

    /**
     * 流离开的处理
     */
    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void onApplicationEvent(MediaDepartureEvent event) {
        // 兼容流注销时类型从redis记录获取
        MediaInfo mediaInfo = redisCatchStorage.getStreamInfo(
                event.getApp(), event.getStream(), event.getMediaServer().getId());
        if (mediaInfo != null) {
            String type = OriginType.values()[mediaInfo.getOriginType()].getType();
            redisCatchStorage.removeStream(event.getMediaServer().getId(), type, event.getApp(), event.getStream());
            if ("PUSH".equalsIgnoreCase(type)) {
                redisCatchStorage.removePushListItem(event.getApp(), event.getStream(), event.getMediaServer().getId());
            }
            if (type != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("serverId", userSetting.getServerId());
                jsonObject.put("app", event.getApp());
                jsonObject.put("stream", event.getStream());
                jsonObject.put("register", false);
                jsonObject.put("mediaServerId", event.getMediaServer().getId());
                redisCatchStorage.sendStreamChangeMsg(type, jsonObject);
            }
        }

        // deviceId = app_stream（与上线时一致，保证唯一性）
        String deviceId = event.getApp() + "_" + event.getStream();

        // 语音/广播对讲流从缓存移除，不查DB
        boolean isBroadcastOrTalk = "broadcast".equals(event.getApp()) || "talk".equals(event.getApp());
        if (isBroadcastOrTalk) {
            talkCacheManager.remove(deviceId);
            log.info("[语音流离线-缓存] deviceId: {}, stream: {}", deviceId, event.getStream());
            return;
        }

        // 检查是否有国标编码，有国标编码的不删除，只标记离线
        String channelDeviceId = event.getStream();
        Device device = deviceMapper.getPushDeviceByDeviceId(deviceId);
        if (device == null) {
            return;
        }
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
        if (channel != null && !ObjectUtils.isEmpty(channel.getGbDeviceId())) {
            // 有国标编码，只标记离线
            device.setOnLine(false);
            device.setUpdateTime(DateUtil.getNow());
            deviceMapper.updatePushDeviceStatus(device);
            if ("ON".equals(channel.getGbStatus())) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
            }
        } else {
            // 没有国标编码，删除
            if (channel != null) {
                gbChannelService.delete(channel.getGbId());
            }
            deviceMapper.deletePushDevice(device.getId());
            log.info("[推流离线-删除] device: {}", deviceId);
        }
    }

    /**
     * 流媒体节点上线
     */
    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void onApplicationEvent(MediaServerOnlineEvent event) {
        zlmServerOnline(event.getMediaServer());
    }

    /**
     * 流媒体节点离线
     */
    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void onApplicationEvent(MediaServerOfflineEvent event) {
        zlmServerOffline(event.getMediaServer());
    }

    @Override
    public List<StreamPush> getPushList(Integer pageNum, Integer pageSize, String query, Boolean pushing, String mediaServerId) {
        if (query != null) {
            query = query.replaceAll("/", "//")
                    .replaceAll("%", "/%")
                    .replaceAll("_", "/_");
        }
        return streamPushMapper.selectAll(query, pushing, mediaServerId);
    }

    @Override
    public List<StreamPush> getPushList(String mediaServerId) {
        List<Device> devices = deviceMapper.getPushDeviceListByMediaServerId(mediaServerId);
        List<StreamPush> result = new ArrayList<>();
        for (Device device : devices) {
            // channel deviceId = app + "01"
            String channelDeviceId = device.getDeviceId() + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            result.add(StreamPush.buildFromDevice(device, channel));
        }
        return result;
    }

    @Override
    public StreamPush getPush(String app, String stream) {
        // 广播/对讲流优先从缓存查
        String cacheKey = app + "_" + stream;
        TalkCacheItem cacheItem = talkCacheManager.get(cacheKey);
        if (cacheItem != null) {
            return cacheItem.toStreamPush();
        }
        // deviceId = app（普通推流）
        Device device = deviceMapper.getPushDeviceByDeviceId(app);
        if (device == null) {
            return null;
        }
        // channel deviceId = stream
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(stream);
        return StreamPush.buildFromDevice(device, channel);
    }

    @Override
    @Transactional
    public boolean add(StreamPush stream) {
        String app = stream.getApp();
        String streamVal = stream.getStream();
        log.info("[添加推流] name: {}, app: {}, stream: {}", stream.getName(), app, streamVal);

        // deviceId = app
        Device deviceInDb = deviceMapper.getPushDeviceByDeviceId(app);
        if (deviceInDb != null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "推流设备已存在");
        }

        Device device = new Device();
        device.setDeviceId(app);
        device.setName(stream.getName());
        device.setOnLine(false);
        device.setStartOfflinePush(stream.isStartOfflinePush());
        device.setCreateTime(DateUtil.getNow());
        device.setUpdateTime(DateUtil.getNow());
        deviceMapper.addPushDevice(device);

        // 创建通道，channel deviceId = stream = app_01
        CommonGBChannel channel = new CommonGBChannel();
        channel.setDeviceId(streamVal);
        channel.setName(stream.getName());
        channel.setParentId(app);
        channel.setDataType(ChannelDataType.STREAM_PUSH.value);
        channel.setDataDeviceId(device.getId());
        channel.setGbDeviceId(stream.getGbDeviceId());
        channel.setGbName(stream.getGbName());
        channel.setStatus("OFF");
        channel.setCreateTime(DateUtil.getNow());
        channel.setUpdateTime(DateUtil.getNow());
        int addChannelResult = gbChannelService.add(channel);

        return addChannelResult > 0;
    }

    @Override
    @Transactional
    public boolean update(StreamPush streamPush) {
        log.info("[更新推流] id: {}, name: {}", streamPush.getId(), streamPush.getName());
        Device device = deviceMapper.query(streamPush.getId());
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "推流设备不存在");
        }

        // 更新设备名称
        if (!ObjectUtils.isEmpty(streamPush.getName())) {
            device.setName(streamPush.getName());
            device.setUpdateTime(DateUtil.getNow());
            // 直接更新设备名称
            deviceMapper.update(device);
        }

        if (streamPush.isStartOfflinePush() != device.isStartOfflinePush()) {
            device.setStartOfflinePush(streamPush.isStartOfflinePush());
            device.setUpdateTime(DateUtil.getNow());
            deviceMapper.updatePushDeviceStatus(device);
        }

        // 更新通道名称（与设备名称保持一致）
        String channelDeviceId = device.getDeviceId() + "01";
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
        if (channel != null) {
            if (!ObjectUtils.isEmpty(streamPush.getName())) {
                channel.setName(streamPush.getName());
            }
            if (!ObjectUtils.isEmpty(streamPush.getGbDeviceId())) {
                channel.setGbDeviceId(streamPush.getGbDeviceId());
                channel.setGbName(streamPush.getGbName());
            }
            gbChannelService.update(channel);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean stop(StreamPush streamPush) {
        log.info("[主动停止推流] id: {}, app: {}, stream: {}", streamPush.getId(), streamPush.getApp(), streamPush.getStream());
        // deviceId = app
        Device device = deviceMapper.getPushDeviceByDeviceId(streamPush.getApp());
        if (device == null) {
            return false;
        }

        MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
        if (mediaServer == null) {
            mediaServer = mediaServerService.getMediaServerByAppAndStream(streamPush.getApp(), streamPush.getStream());
        }
        if (mediaServer != null) {
            mediaServerService.closeStreams(mediaServer, streamPush.getApp(), streamPush.getStream());
        }

        device.setOnLine(false);
        device.setUpdateTime(DateUtil.getNow());
        deviceMapper.updatePushDeviceStatus(device);

        String channelDeviceId = streamPush.getStream();
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
        if (channel != null) {
            if (userSetting.getUsePushingAsStatus()) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
            }
        }

        sendRtpServerService.deleteByStream(streamPush.getStream());
        mediaServerService.stopSendRtp(mediaServer, streamPush.getApp(), streamPush.getStream(), null);
        return true;
    }

    @Override
    @Transactional
    public boolean stopByAppAndStream(String app, String stream) {
        log.info("[主动停止推流] app: {}, stream: {}", app, stream);
        StreamPush streamPush = getPush(app, stream);
        if (streamPush != null) {
            stop(streamPush);
        }
        return true;
    }

    @Override
    @Transactional
    public void zlmServerOnline(MediaServer mediaServer) {
        if (mediaServer == null) {
            return;
        }
        // 获取数据库中的推流设备
        List<Device> devices = deviceMapper.getPushDeviceListByMediaServerId(mediaServer.getId());
        Map<String, Device> deviceMapForDb = new HashMap<>();
        for (Device device : devices) {
            // deviceId = app
            deviceMapForDb.put(device.getDeviceId(), device);
        }

        // 获取Redis中的推流信息
        List<MediaInfo> mediaInfoList = redisCatchStorage.getStreams(mediaServer.getId(), "PUSH");
        Map<String, MediaInfo> streamInfoPushItemMap = new HashMap<>();
        if (!mediaInfoList.isEmpty()) {
            for (MediaInfo mediaInfo : mediaInfoList) {
                // key = app（deviceId）
                streamInfoPushItemMap.put(mediaInfo.getApp(), mediaInfo);
            }
        }

        // 获取所有推流鉴权信息
        List<StreamAuthorityInfo> allStreamAuthorityInfo = redisCatchStorage.getAllStreamAuthorityInfo();
        Map<String, StreamAuthorityInfo> streamAuthorityInfoInfoMap = new HashMap<>();
        for (StreamAuthorityInfo streamAuthorityInfo : allStreamAuthorityInfo) {
            streamAuthorityInfoInfoMap.put(streamAuthorityInfo.getApp(), streamAuthorityInfo);
        }

        List<StreamInfo> mediaList = mediaServerService.getMediaList(mediaServer, null, null, null);
        if (mediaList == null) {
            return;
        }

        for (StreamInfo streamInfo : mediaList) {
            if (streamInfo.getOriginType() == OriginType.RTSP_PUSH.ordinal()
                    || streamInfo.getOriginType() == OriginType.RTMP_PUSH.ordinal()
                    || streamInfo.getOriginType() == OriginType.RTC_PUSH.ordinal()) {
                // key = app（直接匹配 deviceId）
                deviceMapForDb.remove(streamInfo.getApp());
                streamInfoPushItemMap.remove(streamInfo.getApp());
                streamAuthorityInfoInfoMap.remove(streamInfo.getApp());
            }
        }

        // 处理在DB中但不在ZLM中的设备（标记离线或停止）
        if (!deviceMapForDb.isEmpty()) {
            for (Device device : deviceMapForDb.values()) {
                device.setOnLine(false);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updatePushDeviceStatus(device);

                String channelDeviceId = device.getDeviceId() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null && "ON".equals(channel.getGbStatus())) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
                }
            }
        }

        // 清理Redis中的过期数据
        Collection<MediaInfo> mediaInfos = streamInfoPushItemMap.values();
        if (!mediaInfos.isEmpty()) {
            String type = "PUSH";
            for (MediaInfo mediaInfo : mediaInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("serverId", userSetting.getServerId());
                jsonObject.put("app", mediaInfo.getApp());
                jsonObject.put("stream", mediaInfo.getStream());
                jsonObject.put("register", false);
                jsonObject.put("mediaServerId", mediaServer.getId());
                redisCatchStorage.sendStreamChangeMsg(type, jsonObject);
                redisCatchStorage.removeStream(mediaServer.getId(), "PUSH", mediaInfo.getApp(), mediaInfo.getStream());
                redisCatchStorage.removePushListItem(mediaInfo.getApp(), mediaInfo.getStream(), mediaServer.getId());
            }
        }

        Collection<StreamAuthorityInfo> streamAuthorityInfos = streamAuthorityInfoInfoMap.values();
        if (!streamAuthorityInfos.isEmpty()) {
            for (StreamAuthorityInfo streamAuthorityInfo : streamAuthorityInfos) {
                redisCatchStorage.removeStreamAuthorityInfo(streamAuthorityInfo.getApp(), streamAuthorityInfo.getStream());
            }
        }
    }

    @Override
    @Transactional
    public void zlmServerOffline(MediaServer mediaServer) {
        List<Device> devices = deviceMapper.getPushDeviceListByMediaServerId(mediaServer.getId());
        if (!devices.isEmpty()) {
            for (Device device : devices) {
                device.setOnLine(false);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updatePushDeviceStatus(device);

                String channelDeviceId = device.getDeviceId() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null && "ON".equals(channel.getGbStatus())) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
                }
            }
        }

        // 清理该 ZLM 节点上的语音/广播对讲缓存
        talkCacheManager.removeByMediaServerId(mediaServer.getId());

        String type = "PUSH";
        List<MediaInfo> mediaInfoList = redisCatchStorage.getStreams(mediaServer.getId(), type);
        if (!mediaInfoList.isEmpty()) {
            for (MediaInfo mediaInfo : mediaInfoList) {
                redisCatchStorage.removeStream(mediaServer.getId(), type, mediaInfo.getApp(), mediaInfo.getStream());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("serverId", userSetting.getServerId());
                jsonObject.put("app", mediaInfo.getApp());
                jsonObject.put("stream", mediaInfo.getStream());
                jsonObject.put("register", false);
                jsonObject.put("mediaServerId", mediaServer.getId());
                redisCatchStorage.sendStreamChangeMsg(type, jsonObject);
                redisCatchStorage.removePushListItem(mediaInfo.getApp(), mediaInfo.getStream(), mediaServer.getId());
            }
        }
    }

    @Override
    @Transactional
    public void batchAdd(List<StreamPush> streamPushItems) {
        for (StreamPush streamPush : streamPushItems) {
            add(streamPush);
        }
    }

    @Override
    public void allOffline() {
        List<Device> devices = deviceMapper.getPushDeviceList();
        if (devices.isEmpty()) {
            return;
        }
        for (Device device : devices) {
            String channelDeviceId = device.getDeviceId() + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            if (channel != null) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
            }
        }
    }

    @Override
    public void offline(List<StreamPushItemFromRedis> offlineStreams) {
        for (StreamPushItemFromRedis item : offlineStreams) {
            // deviceId = app
            Device device = deviceMapper.getPushDeviceByDeviceId(item.getApp());
            if (device != null) {
                device.setOnLine(false);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updatePushDeviceStatus(device);

                String channelDeviceId = item.getApp() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
                }
            }
        }
    }

    @Override
    public void online(List<StreamPushItemFromRedis> onlineStreams) {
        for (StreamPushItemFromRedis item : onlineStreams) {
            // deviceId = app
            Device device = deviceMapper.getPushDeviceByDeviceId(item.getApp());
            if (device != null) {
                device.setOnLine(true);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updatePushDeviceStatus(device);

                String channelDeviceId = item.getApp() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "ON");
                }
            }
        }
    }

    @Override
    public List<String> getAllAppAndStream() {
        List<Device> devices = deviceMapper.getPushDeviceList();
        List<String> result = new ArrayList<>();
        for (Device device : devices) {
            result.add(device.getDeviceId());
        }
        return result;
    }

    @Override
    public ResourceBaseInfo getOverview() {
        int total = streamPushMapper.getAllCount();
        int online = streamPushMapper.getAllPushing();
        return new ResourceBaseInfo(total, online);
    }

    @Override
    public Map<String, StreamPush> getAllAppAndStreamMap() {
        List<Device> devices = deviceMapper.getPushDeviceList();
        Map<String, StreamPush> result = new HashMap<>();
        for (Device device : devices) {
            String channelDeviceId = device.getDeviceId() + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            StreamPush push = StreamPush.buildFromDevice(device, channel);
            result.put(device.getDeviceId(), push);
        }
        // 合并广播/对讲缓存条目
        for (TalkCacheItem cacheItem : talkCacheManager.getAll()) {
            StreamPush push = cacheItem.toStreamPush();
            result.put(cacheItem.getDeviceId(), push);
        }
        return result;
    }

    @Override
    public Map<String, StreamPush> getAllGBId() {
        List<Device> devices = deviceMapper.getPushDeviceList();
        Map<String, StreamPush> result = new HashMap<>();
        for (Device device : devices) {
            String channelDeviceId = device.getDeviceId() + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            if (channel != null && !ObjectUtils.isEmpty(channel.getGbDeviceId())) {
                StreamPush push = StreamPush.buildFromDevice(device, channel);
                result.put(channel.getGbDeviceId(), push);
            }
        }
        return result;
    }

    @Override
    public void updateStatus(StreamPush push) {
    }

    @Override
    @Transactional
    public void updatePushStatus(StreamPush streamPush, boolean pushIng) {
        // deviceId = app
        Device device = deviceMapper.getPushDeviceByDeviceId(streamPush.getApp());
        if (device == null) {
            return;
        }
        device.setOnLine(pushIng);
        device.setPushTime(DateUtil.getNow());
        device.setUpdateTime(DateUtil.getNow());
        deviceMapper.updatePushDevice(device);

        String channelDeviceId = streamPush.getStream();
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
        if (channel != null) {
            String status = pushIng ? "ON" : "OFF";
            commonGBChannelMapper.updateStatusById(channel.getGbId(), status);
        }
    }

    @Override
    @Transactional
    public void deleteByAppAndStream(String app, String stream) {
        log.info("[删除推流] app: {}, stream: {}", app, stream);
        // deviceId = app
        Device device = deviceMapper.getPushDeviceByDeviceId(app);
        if (device == null) {
            return;
        }
        if (device.isOnLine()) {
            MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
            if (mediaServer != null) {
                mediaServerService.closeStreams(mediaServer, app, stream);
            }
        }
        // channel deviceId = stream
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(stream);
        if (channel != null) {
            gbChannelService.delete(channel.getGbId());
        }
        deviceMapper.deletePushDevice(device.getId());
    }

    @Override
    @Transactional
    public int delete(int id) {
        Device device = deviceMapper.query(id);
        if (device == null) {
            return 0;
        }
        // deviceId = app
        String app = device.getDeviceId();
        String stream = app + "01";
        if (device.isOnLine()) {
            MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
            if (mediaServer != null) {
                mediaServerService.closeStreams(mediaServer, app, stream);
            }
        }
        // channel deviceId = stream
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(stream);
        if (channel != null) {
            gbChannelService.delete(channel.getGbId());
        }
        return deviceMapper.deletePushDevice(device.getId());
    }

    @Override
    @Transactional
    public void batchRemove(Set<Integer> ids) {
        for (Integer id : ids) {
            delete(id);
        }
    }

    @Override
    public void batchUpdate(List<StreamPush> streamPushItemForUpdate) {
        for (StreamPush push : streamPushItemForUpdate) {
            update(push);
        }
    }

    @Override
    public void updateGPSFromGPSMsgInfo(List<GPSMsgInfo> gpsMsgInfoList) {
        List<CommonGBChannel> channels = new ArrayList<>();
        for (GPSMsgInfo gpsMsgInfo : gpsMsgInfoList) {
            CommonGBChannel channel = new CommonGBChannel();
            channel.setGbDeviceId(gpsMsgInfo.getId());
            channel.setGbLongitude(gpsMsgInfo.getLng());
            channel.setGbLatitude(gpsMsgInfo.getLat());
            channels.add(channel);
        }
        gbChannelService.updateGpsByDeviceIdForStreamPush(channels);
    }
}
