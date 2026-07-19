package com.ruoyi.wvp.streamProxy.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruoyi.wvp.common.StreamInfo;
import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.service.IGbChannelService;
import com.ruoyi.wvp.mapper.CommonGBChannelMapper;
import com.ruoyi.wvp.mapper.DeviceChannelMapper;
import com.ruoyi.wvp.mapper.DeviceMapper;
import com.ruoyi.wvp.mapper.StreamProxyMapper;
import com.ruoyi.wvp.media.bean.MediaServer;
import com.ruoyi.wvp.media.event.media.MediaArrivalEvent;
import com.ruoyi.wvp.media.event.media.MediaDepartureEvent;
import com.ruoyi.wvp.media.event.media.MediaNotFoundEvent;
import com.ruoyi.wvp.media.event.mediaServer.MediaServerOfflineEvent;
import com.ruoyi.wvp.media.event.mediaServer.MediaServerOnlineEvent;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.media.zlm.dto.hook.OriginType;
import com.ruoyi.wvp.storager.IRedisCatchStorage;
import com.ruoyi.wvp.streamProxy.bean.StreamProxy;
import com.ruoyi.wvp.streamProxy.bean.StreamProxyParam;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyPlayService;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.utils.DateUtil;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.wvp.vmanager.bean.ResourceBaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 视频代理业务（基于 Device + DeviceChannel 表）
 */
@Slf4j
@Service
@DS("master")
public class StreamProxyServiceImpl implements IStreamProxyService {

    @Autowired
    private StreamProxyMapper streamProxyMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceChannelMapper deviceChannelMapper;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private IStreamProxyPlayService playService;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private IGbChannelService gbChannelService;

    @Autowired
    private CommonGBChannelMapper commonGBChannelMapper;

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Autowired
    TransactionDefinition transactionDefinition;

    /**
     * 流到来的处理
     */
    @Async("taskExecutor")
    @Transactional
    @EventListener
    public void onApplicationEvent(MediaArrivalEvent event) {
        if ("rtsp".equals(event.getSchema())) {
            streamChangeHandler(event.getApp(), event.getStream(), event.getMediaServer().getId(), true);
        }
    }

    /**
     * 流离开的处理
     */
    @Async("taskExecutor")
    @EventListener
    @Transactional
    public void onApplicationEvent(MediaDepartureEvent event) {
        if ("rtsp".equals(event.getSchema())) {
            streamChangeHandler(event.getApp(), event.getStream(), event.getMediaServer().getId(), false);
        }
    }

    /**
     * 流未找到的处理
     */
    @Async("taskExecutor")
    @EventListener
    public void onApplicationEvent(MediaNotFoundEvent event) {
        if ("rtp".equals(event.getApp())) {
            return;
        }
        StreamProxy streamProxy = getStreamProxyByAppAndStream(event.getApp(), event.getStream());
        if (streamProxy != null && streamProxy.isEnableDisableNoneReader()) {
            startByAppAndStream(event.getApp(), event.getStream());
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
    @Transactional
    public StreamInfo save(StreamProxyParam param) {
        if (param.getMediaServerId() != null && param.getMediaServerId().equals("auto")) {
            param.setMediaServerId(null);
        }

        Device device = param.buildDevice();
        device.setCreateTime(DateUtil.getNow());
        device.setUpdateTime(DateUtil.getNow());
        device.setMediaServerId(param.getMediaServerId());
        device.setOnLine(false);

        // 先尝试查找已存在的同 deviceId 设备
        Device existingDevice = deviceMapper.getProxyDeviceByDeviceId(device.getDeviceId());
        if (existingDevice != null) {
            throw new ControllerException(ErrorCode.ERROR400.getCode(), "设备已存在");
        }
        deviceMapper.addProxyDevice(device);

        // 创建 DeviceChannel 关联
        String channelDeviceId = device.getDeviceId() + "01";

        // 直接构造 CommonGBChannel 插入通道表
        CommonGBChannel gbChannel = new CommonGBChannel();
        gbChannel.setDeviceId(channelDeviceId);
        gbChannel.setName(device.getName());
        gbChannel.setParentId(device.getDeviceId());
        gbChannel.setDataType(ChannelDataType.STREAM_PROXY.value);
        gbChannel.setDataDeviceId(device.getId());
        gbChannel.setStatus(device.isOnLine() ? "ON" : "OFF");
        gbChannel.setCreateTime(com.ruoyi.wvp.utils.DateUtil.getNow());
        gbChannel.setUpdateTime(com.ruoyi.wvp.utils.DateUtil.getNow());

        try {
            gbChannelService.add(gbChannel);
        } catch (ControllerException e) {
            log.warn("[添加拉流代理通道] 通道已存在或添加失败: {}", e.getMessage());
        }

//        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
//        if (param.isEnable()) {
//            return playService.startProxy(streamProxy);
//        } else {
//            return null;
//        }
         return null;
    }

    @Override
    @Transactional
    public void add(StreamProxy streamProxy) {
        Device device = new Device();
        device.setDeviceId(streamProxy.getApp());
        device.setName(streamProxy.getName());
        device.setProtocolType("STREAM_PROXY");
        device.setSrcUrl(streamProxy.getSrcUrl());
        device.setStreamType(streamProxy.getType() != null ? streamProxy.getType() : "default");
        device.setExpires(streamProxy.getTimeout());
        device.setFfmpegCmdKey(streamProxy.getFfmpegCmdKey());
        device.setRtspType(streamProxy.getRtspType());
        device.setEnableAudio(streamProxy.isEnableAudio());
        device.setEnableMp4(streamProxy.isEnableMp4());
        device.setEnableRemoveNoneReader(streamProxy.isEnableRemoveNoneReader());
        device.setEnableDisableNoneReader(streamProxy.isEnableDisableNoneReader());
        device.setMediaServerId(streamProxy.getRelatesMediaServerId());
        device.setCreateTime(DateUtil.getNow());
        device.setUpdateTime(DateUtil.getNow());
        device.setOnLine(false);
        device.setChannelCount(1);

        Device existing = deviceMapper.getProxyDeviceByDeviceId(device.getDeviceId());
        if (existing != null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "此设备ID已存在");
        }
        deviceMapper.addProxyDevice(device);
    }

    @Override
    public void delete(int id) {
        StreamProxy streamProxy = getStreamProxy(id);
        if (streamProxy == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "代理不存在");
        }
        delete(streamProxy);
    }

    private void delete(StreamProxy streamProxy) {
        Assert.notNull(streamProxy, "代理不可为NULL");
        if (streamProxy.getPulling() != null && streamProxy.getPulling()) {
            playService.stopProxy(streamProxy);
        }
        // 删除关联的国标通道
        if (streamProxy.getGbId() > 0) {
            gbChannelService.delete(streamProxy.getGbId());
        }
        // 删除 DeviceChannel
        deviceChannelMapper.del(streamProxy.getGbId());
        // 删除 Device
        deviceMapper.deleteProxyDevice(streamProxy.getApp());
    }

    @Override
    @Transactional
    public void delteByAppAndStream(String app, String stream) {
        Device device = deviceMapper.getProxyDeviceByDeviceId(app);
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "代理不存在");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        delete(streamProxy);
    }

    @Override
    public boolean update(StreamProxy streamProxy) {
        Device existing = deviceMapper.getProxyDeviceByDeviceId(streamProxy.getApp());
        if (existing == null) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "代理不存在");
        }
        existing.setName(streamProxy.getName());
        existing.setSrcUrl(streamProxy.getSrcUrl());
        existing.setStreamType(streamProxy.getType() != null ? streamProxy.getType() : "default");
        existing.setExpires(streamProxy.getTimeout());
        existing.setFfmpegCmdKey(streamProxy.getFfmpegCmdKey());
        existing.setRtspType(streamProxy.getRtspType());
        existing.setEnableAudio(streamProxy.isEnableAudio());
        existing.setEnableMp4(streamProxy.isEnableMp4());
        existing.setEnableRemoveNoneReader(streamProxy.isEnableRemoveNoneReader());
        existing.setEnableDisableNoneReader(streamProxy.isEnableDisableNoneReader());
        existing.setUpdateTime(DateUtil.getNow());
        deviceMapper.updateProxyDevice(existing);
        return true;
    }

    @Override
    public List<StreamProxy> getAll(Integer page, Integer count, String query, Boolean pulling, String mediaServerId) {
        if (query != null) {
            query = query.replaceAll("/", "//")
                    .replaceAll("%", "/%")
                    .replaceAll("_", "/_");
        }
        List<Device> devices = streamProxyMapper.selectAll(query, pulling, mediaServerId);
        return devices.stream()
                .map(StreamProxy::buildFromDevice)
                .collect(Collectors.toList());
    }

    @Override
    public boolean startByAppAndStream(String app, String stream) {
        Device device = deviceMapper.getProxyDeviceByDeviceId(app);
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "代理信息未找到");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        StreamInfo streamInfo = playService.startProxy(streamProxy);
        return streamInfo != null;
    }

    @Override
    public void stopByAppAndStream(String app, String stream) {
        Device device = deviceMapper.getProxyDeviceByDeviceId(app);
        if (device == null) {
            throw new ControllerException(ErrorCode.ERROR404.getCode(), "代理信息未找到");
        }
        StreamProxy streamProxy = StreamProxy.buildFromDevice(device);
        playService.stopProxy(streamProxy);
    }

    @Override
    public Map<String, String> getFFmpegCMDs(MediaServer mediaServer) {
        return mediaServerService.getFFmpegCMDs(mediaServer);
    }

    @Override
    public StreamProxy getStreamProxyByAppAndStream(String app, String stream) {
        Device device = streamProxyMapper.selectOneByAppAndStream(app, stream);
        if (device == null) {
            return null;
        }
        return StreamProxy.buildFromDevice(device);
    }

    @Override
    @Transactional
    public void zlmServerOnline(MediaServer mediaServer) {
        if (mediaServer == null) {
            return;
        }
        redisCatchStorage.removeStream(mediaServer.getId(), "PULL");

        List<Device> devices = streamProxyMapper.selectForPushingInMediaServer(mediaServer.getId(), true);
        if (devices.isEmpty()) {
            return;
        }
        Map<String, Device> deviceMapForDb = new HashMap<>();
        for (Device device : devices) {
            deviceMapForDb.put(device.getDeviceId(), device);
        }

        List<StreamInfo> streamInfoList = mediaServerService.getMediaList(mediaServer, null, null, null);

        List<CommonGBChannel> channelListForOnline = new ArrayList<>();
        for (StreamInfo streamInfo : streamInfoList) {
            String key = streamInfo.getApp();
            Device device = deviceMapForDb.get(key);
            if (device == null) {
                continue;
            }
            if (streamInfo.getOriginType() == OriginType.PULL.ordinal()
                    || streamInfo.getOriginType() == OriginType.FFMPEG_PULL.ordinal()) {
                redisCatchStorage.addStream(mediaServer, "pull", streamInfo.getApp(), streamInfo.getStream(), streamInfo.getMediaInfo());

                // 更新设备在线状态
                device.setMediaServerId(mediaServer.getId());
                device.setOnLine(true);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updateProxyStream(device);

                // 同步通道状态
                String channelDeviceId = device.getDeviceId() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null && !"ON".equals(channel.getGbStatus())) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "ON");
                    channelListForOnline.add(channel);
                }
                deviceMapForDb.remove(key);
            }
        }

        if (!channelListForOnline.isEmpty()) {
            gbChannelService.online(channelListForOnline);
        }

        // 处理ZLM中不存在的流，标记为离线
        List<CommonGBChannel> channelListForOffline = new ArrayList<>();
        if (!deviceMapForDb.isEmpty()) {
            for (Device device : deviceMapForDb.values()) {
                String channelDeviceId = device.getDeviceId() + "01";
                CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
                if (channel != null && "ON".equals(channel.getGbStatus())) {
                    commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
                    channelListForOffline.add(channel);
                }
                device.setOnLine(false);
                device.setUpdateTime(DateUtil.getNow());
                deviceMapper.updateProxyStream(device);
            }
        }
        if (!channelListForOffline.isEmpty()) {
            gbChannelService.offline(channelListForOffline);
        }
    }

    @Override
    public void zlmServerOffline(MediaServer mediaServer) {
        List<Device> devices = streamProxyMapper.selectForPushingInMediaServer(mediaServer.getId(), true);

        redisCatchStorage.removeStream(mediaServer.getId(), "PULL");

        if (devices.isEmpty()) {
            return;
        }
        List<CommonGBChannel> channelListForOffline = new ArrayList<>();

        for (Device device : devices) {
            // 更新设备离线状态
            device.setOnLine(false);
            device.setUpdateTime(DateUtil.getNow());
            deviceMapper.updateProxyStream(device);

            // 同步通道状态
            String channelDeviceId = device.getDeviceId() + "01";
            CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
            if (channel != null && "ON".equals(channel.getGbStatus())) {
                commonGBChannelMapper.updateStatusById(channel.getGbId(), "OFF");
                channelListForOffline.add(channel);
            }
        }
        if (!channelListForOffline.isEmpty()) {
            gbChannelService.offline(channelListForOffline);
        }
    }

    @Transactional
    public void streamChangeHandler(String app, String stream, String mediaServerId, boolean status) {
        Device device = deviceMapper.getProxyDeviceByDeviceId(app);
        if (device == null) {
            return;
        }
        device.setMediaServerId(mediaServerId);
        device.setUpdateTime(DateUtil.getNow());
        device.setOnLine(status);
        deviceMapper.updateProxyStream(device);

        // 同步更新通道状态
        String channelDeviceId = app + "01";
        CommonGBChannel channel = commonGBChannelMapper.queryByDeviceId(channelDeviceId);
        if (channel != null) {
            String channelStatus = status ? "ON" : "OFF";
            commonGBChannelMapper.updateStatusById(channel.getGbId(), channelStatus);
            log.info("[拉流代理状态变更] device: {}, channel: {}, status: {}", app, channelDeviceId, channelStatus);
        } else {
            log.warn("[拉流代理状态变更] 未找到通道: {}", channelDeviceId);
        }
    }

    @Override
    public ResourceBaseInfo getOverview() {
        int total = streamProxyMapper.getAllCount();
        int online = streamProxyMapper.getOnline();
        return new ResourceBaseInfo(total, online);
    }

    @Override
    public StreamProxy getStreamProxy(int id) {
        Device device = streamProxyMapper.select(id);
        if (device == null) {
            return null;
        }
        return StreamProxy.buildFromDevice(device);
    }
}
