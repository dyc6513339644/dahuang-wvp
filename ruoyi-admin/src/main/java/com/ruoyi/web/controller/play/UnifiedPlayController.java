package com.ruoyi.web.controller.play;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.isup.domain.HaiKangIsupPresetInfo;
import com.ruoyi.isup.service.IDeviceIsupService;
import com.ruoyi.isup.service.IDeviceIsupStreamService;
import com.ruoyi.isup.service.IsupStreamCallback;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import com.ruoyi.wvp.common.InviteSessionType;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.wvp.common.StreamUrlHelper;
import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.service.*;
import com.ruoyi.wvp.gb28181.transmit.callback.DeferredResultHolder;
import com.ruoyi.wvp.gb28181.transmit.callback.RequestMessage;
import com.ruoyi.wvp.gb28181.transmit.cmd.impl.SIPCommander;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.service.bean.InviteErrorCode;
import com.ruoyi.wvp.streamProxy.bean.StreamProxy;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyPlayService;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.streamPush.service.IStreamPushPlayService;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import com.ruoyi.media.domain.StreamContent;
import com.ruoyi.media.domain.WVPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 统一播放控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/unified")
public class UnifiedPlayController extends BaseController {

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceChannelService deviceChannelService;

    @Autowired
    private IGbChannelService gbChannelService;

    @Autowired
    private IPlayService playService;

    @Autowired
    private IPTZService ptzService;

    @Autowired
    private SIPCommander cmder;

    @Autowired
    private IDeviceOnvifService deviceOnvifService;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private IStreamProxyPlayService streamProxyPlayService;

    @Autowired
    private IStreamPushPlayService streamPushPlayService;

    @Autowired
    private IStreamPushService streamPushService;

    @Autowired
    private IStreamProxyService streamProxyService;

    @Autowired
    private IDeviceIsupStreamService deviceIsupStreamService;

    @Autowired
    private IDeviceIsupService deviceIsupService;

    /**
     * ISUP 云台"停止"需要沿用上一次的方向指令（SDK 的 byAction=1 结束指定指令），
     * 这里记录 deviceId:channelId → 上一次下发的 byPTZCmd，stop 时取出并移除。
     */
    private static final Map<String, Integer> ISUP_LAST_PTZ_CMD = new ConcurrentHashMap<>();

    @Autowired
    private IInviteStreamService inviteStreamService;

    @Autowired
    private DeferredResultHolder resultHolder;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private StreamUrlHelper streamUrlHelper;

    // ==================== 协议识别辅助 ====================

    /**
     * 根据 String channelId（GB通道编码 = CommonGBChannel.deviceId）获取 ONVIF 通道的数据库主键 (Long)
     * 映射路径：CommonGBChannel.deviceId → DeviceOnvifChannel.gbDeviceId → DeviceOnvifChannel.id
     */
    private Long resolveOnvifChannelId(String channelId) {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
        Assert.notNull(channel, "统一通道不存在: " + channelId);
        Long onvifChannelId = channel.getId();
        return onvifChannelId;
    }

    /**
     * 检查设备是否支持云台/预置位/聚焦控制（拉流代理不支持）
     */
    private void assertPtzSupported(Device device) {
        if ("STREAM_PROXY".equals(device.getProtocolType())) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "拉流代理设备不支持云台控制");
        }
    }

    // ==================== ISUP 辅助 ====================

    /**
     * 解析 ISUP 通道（DeviceOnvifChannel），ISUP 云台/预置位需要通道主键与通道号
     */
    private DeviceOnvifChannel resolveIsupChannel(String channelId) {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
        Assert.notNull(channel, "ISUP通道不存在: " + channelId);
        return channel;
    }

    /** ISUP 设备库ID：优先取通道上的 dataDeviceId，兜底设备主键 */
    private Long isupDeviceDbId(Device device, DeviceOnvifChannel channel) {
        return channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();
    }

    /** ISUP 通道号（字符串 → 数字） */
    private Integer isupChannelNo(DeviceOnvifChannel channel) {
        String channelNo = channel.getChannelNo();
        try {
            return Integer.parseInt(channelNo);
        } catch (Exception e) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "ISUP通道号非法: " + channelNo);
        }
    }

    /**
     * 统一 command → ISUP byPTZCmd
     * 与前端 ISUP 云台面板保持一致：0上 1下 2左 3右 4左上 5左下 6右上 7右下 9放大 10缩小
     */
    private int isupPtzCmd(String command) {
        switch (command) {
            case "up":        return 0;
            case "down":      return 1;
            case "left":      return 2;
            case "right":     return 3;
            case "upleft":    return 4;
            case "downleft":  return 5;
            case "upright":   return 6;
            case "downright": return 7;
            case "zoomin":    return 9;
            case "zoomout":   return 10;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效指令: " + command);
        }
    }

    /** 0-255 速度 → ISUP 档位 1-7 */
    private int isupSpeed(int speed255) {
        if (speed255 <= 0) {
            return 1;
        }
        int speed = (int) Math.round(speed255 / 255.0 * 7);
        return Math.min(7, Math.max(1, speed));
    }

    /** ISUP 预置位编号校验（1-255） */
    private int isupPresetId(String presetId) {
        try {
            int id = Integer.parseInt(presetId);
            if (id < 1 || id > 255) {
                throw new NumberFormatException();
            }
            return id;
        } catch (NumberFormatException e) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "海康ISUP 预置位编号必须为1-255之间的数字");
        }
    }

    // ==================== 播放 ====================

    /**
     * 统一播放
     * @param deviceId  设备编号
     * @param channelId 通道编号
     * @return 流信息 (DeferredResult)
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @GetMapping("/play/start/{deviceId}/{channelId}")
    public DeferredResult<WVPResult<StreamContent>> play(HttpServletRequest request,
                                                          @PathVariable String deviceId,
                                                          @PathVariable String channelId) {
        Assert.notNull(deviceId, "设备国标编号不可为NULL");
        Assert.notNull(channelId, "通道国标编号不可为NULL");
        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");

        String protocolType = device.getProtocolType();

        // ---- 拉流代理 ----
        if ("STREAM_PROXY".equals(protocolType)) {
            return playProxy(request, device, channelId);
        }

        // ---- 推流 ----
        if ("STREAM_PUSH".equals(protocolType)) {
            return playPush(request, device, channelId);
        }

        // ---- ONVIF ----
        if ("ONVIF".equals(protocolType)) {
            return playOnvif(request, device, channelId);
        }

        // ---- ISUP（海康 EHOME/ISUP 私有协议）----
        if ("ISUP".equals(protocolType)) {
            return playIsup(request, device, channelId);
        }

        // ---- GB28181 ----
        return playGb(request, device, channelId);
    }

    /**
     * 统一停止播放 —— 兼容 GB28181 / ONVIF / STREAM_PROXY / STREAM_PUSH
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:stop')")
    @GetMapping("/play/stop/{deviceId}/{channelId}")
    public AjaxResult playStop(@PathVariable String deviceId, @PathVariable String channelId) {
        log.info("[统一停止播放] deviceId: {}, channelId: {}", deviceId, channelId);
        if (deviceId == null || channelId == null) {
            throw new ControllerException(ErrorCode.ERROR400);
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        String protocolType = device.getProtocolType();

        if ("STREAM_PROXY".equals(protocolType)) {
            streamProxyPlayService.stop(device.getId());
            return success();
        }

        if ("STREAM_PUSH".equals(protocolType)) {
            streamPushService.stopByAppAndStream(device.getDeviceId(), device.getDeviceId() + "_01");
            return success();
        }

        if ("ONVIF".equals(protocolType)) {
            MediaServer mediaServer = mediaServerService.getOne(device.getMediaServerId());
            if (mediaServer != null) {
                mediaServerService.closeStreams(mediaServer, device.getDeviceId(), channelId);
            }
            return success();
        }

        // ---- ISUP（海康私有协议）----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
            Assert.notNull(channel, "通道不存在");
            Integer channelNo = Integer.parseInt(channel.getChannelNo());
            Long deviceDbId = channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();
            deviceIsupStreamService.stopPlay(deviceDbId, channelNo);
            return success();
        }

        // GB28181
        DeviceChannel channel = deviceChannelService.getOneForSource(deviceId, channelId);
        Assert.notNull(channel, "通道不存在");
        String streamId = String.format("%s_%s", device.getDeviceId(), channel.getDeviceId());
        playService.stop(InviteSessionType.PLAY, device, channel, streamId);
        return success();
    }

    // ---- 播放子方法 ----

    /** GB28181 SIP 点播 */
    private DeferredResult<WVPResult<StreamContent>> playGb(HttpServletRequest request, Device device, String channelId) {
        DeviceChannel channel = deviceChannelService.getOne(device.getDeviceId(), channelId);
        Assert.notNull(channel, "通道不存在");

        MediaServer newMediaServerItem = playService.getNewMediaServerItem(device);

        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channelId;
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());

        result.onTimeout(() -> {
            log.info("[统一播放-GB] 点播超时 deviceId: {}, channelId: {}", device.getDeviceId(), channelId);
            WVPResult<StreamInfo> wvpResult = new WVPResult<>();
            wvpResult.setCode(ErrorCode.ERROR100.getCode());
            wvpResult.setMsg("点播超时");
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
            inviteStreamService.removeInviteInfoByDeviceAndChannel(InviteSessionType.PLAY, channel.getId());
            deviceChannelService.stopPlay(channel.getId());
        });

        resultHolder.put(key, uuid, result);

        playService.play(newMediaServerItem, device.getDeviceId(), channelId, null, (code, msg, streamInfo) -> {
            WVPResult<StreamContent> wvpResult = buildStreamWvpResult(request, code, msg, streamInfo, newMediaServerItem);
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        });
        return result;
    }

    /** 拉流代理播放 */
    private DeferredResult<WVPResult<StreamContent>> playProxy(HttpServletRequest request, Device device, String channelId) {
        DeviceChannel channel = deviceChannelService.getOne(device.getDeviceId(), channelId);
        Assert.notNull(channel, "通道不存在");

        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channelId;
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());

        resultHolder.put(key, uuid, result);

        streamProxyPlayService.start(device.getId(), null, (code, msg, streamInfo) -> {
            WVPResult<StreamContent> wvpResult = buildStreamWvpResult(request, code, msg, streamInfo, null);
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        });
        return result;
    }

    /** 推流播放 */
    private DeferredResult<WVPResult<StreamContent>> playPush(HttpServletRequest request, Device device, String channelId) {
        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channelId;
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());

        resultHolder.put(key, uuid, result);

        streamPushPlayService.start(device.getId(), (code, msg, streamInfo) -> {
            WVPResult<StreamContent> wvpResult = buildStreamWvpResult(request, code, msg, streamInfo, null);
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        }, null, null);
        return result;
    }

    /** ONVIF 拉流播放（从通道 srcUrl 读取 RTSP 地址 → ZLM 代理拉流） */
    private DeferredResult<WVPResult<StreamContent>> playOnvif(HttpServletRequest request, Device device, String channelId) {
        log.info("[统一播放-ONVIF] deviceId: {}, channelId: {}", device.getDeviceId(), channelId);

        CommonGBChannel channel = gbChannelService.queryByDeviceId(channelId);
        Assert.notNull(channel, "ONVIF通道不存在: " + channelId);
        String rtspUrl = channel.getSrcUrl();
        Assert.hasText(rtspUrl, "通道未存储取流地址");

        MediaServer mediaServer = mediaServerService.getMediaServerForMinimumLoad(null);
        Assert.notNull(mediaServer, "未找到可用流媒体节点");

        StreamProxy streamProxy = new StreamProxy();
        streamProxy.setApp(device.getDeviceId());
        streamProxy.setStream(channelId);
        streamProxy.setSrcUrl(rtspUrl);
        streamProxy.setType("default");
        streamProxy.setRtspType("0");
        streamProxy.setEnableAudio(channel.isHasAudio());
        streamProxy.setEnableMp4(false);
        streamProxy.setEnableRemoveNoneReader(true);
        streamProxy.setEnableDisableNoneReader(false);
        streamProxy.setEnable(true);

        StreamInfo streamInfo = mediaServerService.startProxy(mediaServer, streamProxy);
        streamInfo.setMediaServer(mediaServer);
        streamInfo.setStream(streamProxy.getStream());

        // ONVIF 拉流是同步操作，直接构建 DeferredResult 并立即返回
        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channelId;
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());

        result.onTimeout(() -> {
            log.info("[统一播放-ONVIF] 超时 deviceId: {}, channelId: {}", device.getDeviceId(), channelId);
            WVPResult<WVPResult> failResult = new WVPResult<>();
            failResult.setCode(ErrorCode.ERROR100.getCode());
            failResult.setMsg("点播超时");
            requestMessage.setData(failResult);
            resultHolder.invokeAllResult(requestMessage);
        });

        resultHolder.put(key, uuid, result);

        WVPResult<StreamContent> wvpResult = buildStreamWvpResultSync(request, streamInfo);
        requestMessage.setData(wvpResult);
        resultHolder.invokeAllResult(requestMessage);
        return result;
    }

    /**
     * ISUP 预览播放：按 channelId 反查通道取 channelNo 与设备，开流结果通过回调包装成 StreamContent 返回。
     */
    private DeferredResult<WVPResult<StreamContent>> playIsup(HttpServletRequest request, Device device, String channelId) {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
        Assert.notNull(channel, "ISUP通道不存在: " + channelId);
        Integer channelNo = Integer.parseInt(channel.getChannelNo());
        Long deviceDbId = channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();

        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channelId;
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());

        result.onTimeout(() -> {
            log.info("[统一播放-ISUP] 点播超时 deviceId: {}, channelId: {}", device.getDeviceId(), channelId);
            WVPResult<StreamInfo> wvpResult = new WVPResult<>();
            wvpResult.setCode(ErrorCode.ERROR100.getCode());
            wvpResult.setMsg("点播超时");
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        });

        resultHolder.put(key, uuid, result);

        RtpServerParam rtpServerParam = new RtpServerParam();
        rtpServerParam.setId(deviceDbId);
        rtpServerParam.setChannel(channelNo);
        rtpServerParam.setStreamMode("1"); // 主码流
        try {
            IsupStreamCallback callback = (code, msg, streamInfo) -> {
                WVPResult<StreamContent> wvpResult = buildStreamWvpResult(request, code, msg, streamInfo, null);
                requestMessage.setData(wvpResult);
                resultHolder.invokeAllResult(requestMessage);
            };
            deviceIsupStreamService.startPlay(rtpServerParam, callback);
        } catch (Exception e) {
            log.error("[统一播放-ISUP] 开流异常 deviceId: {}, channelId: {}", device.getDeviceId(), channelId, e);
            WVPResult<StreamContent> wvpResult = new WVPResult<>();
            wvpResult.setCode(ErrorCode.ERROR100.getCode());
            wvpResult.setMsg(e.getMessage());
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        }
        return result;
    }

    /** 构建流信息异步回调 WVPResult（GB / 代理 / 推流 共用） */
    private WVPResult<StreamContent> buildStreamWvpResult(HttpServletRequest request, int code, String msg,
                                                           StreamInfo streamInfo, MediaServer mediaServerItem) {
        WVPResult<StreamContent> wvpResult = new WVPResult<>();
        if (code == InviteErrorCode.SUCCESS.getCode()) {
            wvpResult.setCode(ErrorCode.SUCCESS.getCode());
            wvpResult.setMsg(ErrorCode.SUCCESS.getMsg());
            if (streamInfo != null) {
                // ISUP / 拉流代理 / 推流 这几个分支没有单独的 MediaServer 变量，回调里传的是 null，
                // 直接跳过同源化会返回 ZLM 裸地址（https://<IP>:<sslport>/rtp/...），
                // 浏览器因证书 CN 与 IP 不匹配而拒播。这里兜底取 streamInfo 上已经带好的那台媒体服务器。
                MediaServer proxyTarget = mediaServerItem != null ? mediaServerItem : streamInfo.getMediaServer();
                streamInfo = streamUrlHelper.applyNginxProxy(streamInfo, proxyTarget);
                if (userSetting.getUseSourceIpAsStreamIp() && (proxyTarget == null || !proxyTarget.isNginxProxyEnabled())) {
                    streamInfo = streamInfo.clone();
                    String host = resolveRequestHost(request);
                    streamInfo.channgeStreamIp(host);
                }
                if (mediaServerItem != null && !ObjectUtils.isEmpty(mediaServerItem.getTranscodeSuffix())
                        && !"null".equalsIgnoreCase(mediaServerItem.getTranscodeSuffix())) {
                    streamInfo.setStream(streamInfo.getStream() + "_" + mediaServerItem.getTranscodeSuffix());
                }
                wvpResult.setData(new StreamContent(streamInfo));
            } else {
                wvpResult.setCode(code);
                wvpResult.setMsg(msg);
            }
        } else {
            wvpResult.setCode(code);
            wvpResult.setMsg(msg);
        }
        return wvpResult;
    }

    /** 构建流信息同步 WVPResult（ONVIF 拉流） */
    private WVPResult<StreamContent> buildStreamWvpResultSync(HttpServletRequest request, StreamInfo streamInfo) {
        WVPResult<StreamContent> wvpResult = new WVPResult<>();
        wvpResult.setCode(ErrorCode.SUCCESS.getCode());
        wvpResult.setMsg(ErrorCode.SUCCESS.getMsg());
        if (streamInfo != null) {
            streamInfo = streamUrlHelper.applyNginxProxy(streamInfo, streamInfo.getMediaServer());
            if (userSetting.getUseSourceIpAsStreamIp()) {
                MediaServer ms = streamInfo.getMediaServer();
                if (ms == null || !ms.isNginxProxyEnabled()) {
                    streamInfo = streamInfo.clone();
                    String host = resolveRequestHost(request);
                    streamInfo.channgeStreamIp(host);
                }
            }
        }
        wvpResult.setData(new StreamContent(streamInfo));
        return wvpResult;
    }

    /** 从请求中解析 host（IP替换用） */
    private String resolveRequestHost(HttpServletRequest request) {
        try {
            return new URL(request.getRequestURL().toString()).getHost();
        } catch (MalformedURLException e) {
            return request.getLocalAddr();
        }
    }

    // ==================== 云台控制 ====================

    /**
     * 统一云台方向控制 —— 兼容 GB28181 / ONVIF
     * <p>
     * 参数风格与国标接口一致：command (left/right/up/down/...), horizonSpeed (0-255), verticalSpeed (0-255), zoomSpeed (0-15)
     * <p>
     * ONVIF 内部自动将 0-255 归一化为 -1.0~1.0
     *
     * @param deviceId      设备国标编号
     * @param channelId     通道国标编号
     * @param command        控制指令: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop
     * @param horizonSpeed  水平速度(0-255)，默认100
     * @param verticalSpeed 垂直速度(0-255)，默认100
     * @param zoomSpeed     缩放速度(0-15 for GB / 0-255 for ONVIF 统一入口，ONVIF内部归一化)，默认16
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/ptz/{deviceId}/{channelId}")
    public AjaxResult ptz(@PathVariable String deviceId, @PathVariable String channelId,
                          String command, Integer horizonSpeed, Integer verticalSpeed, Integer zoomSpeed) {
        log.info("[统一云台] deviceId: {}, channelId: {}, command: {}, horizonSpeed: {}, verticalSpeed: {}, zoomSpeed: {}",
                deviceId, channelId, command, horizonSpeed, verticalSpeed, zoomSpeed);

        if (horizonSpeed == null) horizonSpeed = 100;
        if (verticalSpeed == null) verticalSpeed = 100;
        if (zoomSpeed == null) zoomSpeed = 16;

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        // ---- ONVIF ----
        if ("ONVIF".equals(protocolType)) {
            return ptzOnvif(channelId, command, horizonSpeed, verticalSpeed, zoomSpeed);
        }

        // ---- ISUP（海康 EHOME/ISUP 私有协议）----
        if ("ISUP".equals(protocolType)) {
            return ptzIsup(device, channelId, command, horizonSpeed, verticalSpeed, zoomSpeed);
        }

        // ---- GB28181 / STREAM_PUSH ----
        return ptzGb(deviceId, channelId, command, horizonSpeed, verticalSpeed, zoomSpeed);
    }

    /** GB 云台控制（SIP 命令） */
    private AjaxResult ptzGb(String deviceId, String channelId, String command,
                             int horizonSpeed, int verticalSpeed, int zoomSpeed) {
        // 速度校验
        if (horizonSpeed < 0 || horizonSpeed > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "horizonSpeed 为 0-255的数字");
        }
        if (verticalSpeed < 0 || verticalSpeed > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "verticalSpeed 为 0-255的数字");
        }
        if (zoomSpeed < 0 || zoomSpeed > 15) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "zoomSpeed 为 0-15的数字");
        }

        int cmdCode;
        switch (command) {
            case "left":      cmdCode = 2;  break;
            case "right":     cmdCode = 1;  break;
            case "up":        cmdCode = 8;  break;
            case "down":      cmdCode = 4;  break;
            case "upleft":    cmdCode = 10; break;
            case "upright":   cmdCode = 9;  break;
            case "downleft":  cmdCode = 6;  break;
            case "downright": cmdCode = 5;  break;
            case "zoomin":    cmdCode = 16; break;
            case "zoomout":   cmdCode = 32; break;
            case "stop":
                cmdCode = 0;
                horizonSpeed = 0;
                verticalSpeed = 0;
                zoomSpeed = 0;
                break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效指令: " + command);
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        ptzService.frontEndCommand(device, channelId, cmdCode, horizonSpeed, verticalSpeed, zoomSpeed);
        return success();
    }

    /**
     * ISUP 云台控制（CMS NET_ECMS_RemoteControl）
     * <p>
     * ISUP 用 byPTZCmd 指定指令、byAction 区分 0=开始/1=结束，速度档位 1-7。
     * 前端 stop 不带方向，这里用 ISUP_LAST_PTZ_CMD 记录本次按下的指令来收尾。
     */
    private AjaxResult ptzIsup(Device device, String channelId, String command,
                               int horizonSpeed, int verticalSpeed, int zoomSpeed) {
        DeviceOnvifChannel channel = resolveIsupChannel(channelId);
        Long deviceDbId = isupDeviceDbId(device, channel);
        Integer channelNo = isupChannelNo(channel);
        String key = device.getDeviceId() + ":" + channelId;
        int speed = isupSpeed(Math.max(horizonSpeed, verticalSpeed));

        if ("stop".equals(command)) {
            Integer lastCmd = ISUP_LAST_PTZ_CMD.remove(key);
            if (lastCmd == null) {
                return success();
            }
            log.info("[统一云台-ISUP] 停止 deviceDbId: {}, channelNo: {}, ptzCmd: {}, speed: {}",
                    deviceDbId, channelNo, lastCmd, speed);
            deviceIsupService.endPtz(deviceDbId, channelNo, lastCmd, speed);
            return success();
        }

        int ptzCmd = isupPtzCmd(command);
        log.info("[统一云台-ISUP] deviceDbId: {}, channelNo: {}, command: {}, ptzCmd: {}, speed: {}",
                deviceDbId, channelNo, command, ptzCmd, speed);
        deviceIsupService.startPtz(deviceDbId, channelNo, ptzCmd, speed);
        ISUP_LAST_PTZ_CMD.put(key, ptzCmd);
        return success();
    }

    /** ONVIF 云台控制（SOAP ContinuousMove，自动归一化速度） */
    private AjaxResult ptzOnvif(String channelId, String command,
                                int horizonSpeed, int verticalSpeed, int zoomSpeed) {
        Long onvifChannelId = resolveOnvifChannelId(channelId);

        double x = 0, y = 0, z = 0;
        switch (command) {
            case "left":
                x = -horizonSpeed / 255.0;
                break;
            case "right":
                x =  horizonSpeed / 255.0;
                break;
            case "up":
                y =  verticalSpeed / 255.0;
                break;
            case "down":
                y = -verticalSpeed / 255.0;
                break;
            case "upleft":
                x = -horizonSpeed / 255.0; y =  verticalSpeed / 255.0;
                break;
            case "upright":   x =  horizonSpeed / 255.0; y =  verticalSpeed / 255.0;  break;
            case "downleft":  x = -horizonSpeed / 255.0; y = -verticalSpeed / 255.0;  break;
            case "downright": x =  horizonSpeed / 255.0; y = -verticalSpeed / 255.0;  break;
            case "zoomin":    z =  zoomSpeed / 15.0;                              break;
            case "zoomout":   z = -zoomSpeed / 15.0;                              break;
            case "stop":      /* x=0, y=0, z=0 → ptzStop */                     break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效指令: " + command);
        }
        log.info("[统一云台-ONVIF] onvifChannelId: {}, x: {}, y: {}, z: {}", onvifChannelId, x, y, z);
        deviceOnvifService.continuousMoveByChannelId(onvifChannelId, x, y, z,command);
        return AjaxResult.success();
    }

    // ==================== 聚焦控制 ====================

    /**
     * 统一聚焦控制 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: command (near/far/stop) + speed (0-255) → SIP 命令
     * ONVIF: command (near/far/stop) + speed (0-255) → 归一化为 -1.0~1.0 → focusMove/focusStop
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @param command   控制指令: near(近焦), far(远焦), stop(停止)
     * @param speed     聚焦速度(0-255)，默认100
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/focus/{deviceId}/{channelId}")
    public AjaxResult focus(@PathVariable String deviceId, @PathVariable String channelId,
                            String command, Integer speed) {
        log.info("[统一聚焦] deviceId: {}, channelId: {}, command: {}, speed: {}", deviceId, channelId, command, speed);

        if (speed == null) speed = 100;

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        if ("ONVIF".equals(protocolType)) {
            return focusOnvif(channelId, command, speed);
        }

        // ---- ISUP：SDK 无独立聚焦指令，静默返回成功（前端不弹错误提示） ----
        if ("ISUP".equals(protocolType)) {
            return success();
        }

        return focusGb(deviceId, channelId, command, speed);
    }

    /** GB 聚焦控制（SIP 命令） */
    private AjaxResult focusGb(String deviceId, String channelId, String command, int speed) {
        if (speed < 0 || speed > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "speed 为 0-255的数字");
        }

        int cmdCode = 0x40;
        switch (command) {
            case "near": cmdCode = 0x42; break;
            case "far":  cmdCode = 0x41; break;
            case "stop": speed = 0;       break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效聚焦指令: " + command);
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        ptzService.frontEndCommand(device, channelId, cmdCode, speed, 0, 0);
        return success();
    }

    /** ONVIF 聚焦控制（SOAP Imaging Move） */
    private AjaxResult focusOnvif(String channelId, String command, int speed) {
        Long onvifChannelId = resolveOnvifChannelId(channelId);

        float focusSpeed;
        switch (command) {
            case "near": focusSpeed = -(speed / 255.0f); break;
            case "far":  focusSpeed =  (speed / 255.0f); break;
            case "stop": focusSpeed = 0;                  break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效聚焦指令: " + command);
        }

        log.info("[统一聚焦-ONVIF] onvifChannelId: {}, focusSpeed: {}", onvifChannelId, focusSpeed);
        deviceOnvifService.focusMoveByChannelId(onvifChannelId, focusSpeed);
        return success();
    }

    // ==================== 光圈控制 ====================

    /**
     * 统一光圈控制 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: command (in=缩小光圈/out=放大光圈/stop) + speed (0-255) → SIP 命令（连续光圈）
     * ONVIF: command (in/out/stop) + speed (0-255) → irisSet(speed/255.0) 绝对值控制
     * <p>
     * 注意：ONVIF 光圈为绝对值定位（irisSet 0.0~1.0），与 GB 的连续光圈模型有差异。
     * 此接口将 speed 映射为 iris 绝对值：in → 向 0 方向、out → 向 1 方向、stop → 维持当前。
     * 若需精确 ONVIF 光圈绝对值控制，建议使用 speed 参数直接传入 0-255 映射的 iris 值。
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @param command   控制指令: in(缩小), out(放大), stop(停止)
     * @param speed     光圈速度(0-255 GB连续 / ONVIF绝对值映射)，默认100
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/iris/{deviceId}/{channelId}")
    public AjaxResult iris(@PathVariable String deviceId, @PathVariable String channelId,
                           String command, Integer speed) {
        log.info("[统一光圈] deviceId: {}, channelId: {}, command: {}, speed: {}", deviceId, channelId, command, speed);

        if (speed == null) speed = 100;

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        if ("ONVIF".equals(protocolType)) {
            return irisOnvif(channelId, command, speed);
        }

        // ---- ISUP：SDK 无独立光圈指令，静默返回成功（前端不弹错误提示） ----
        if ("ISUP".equals(protocolType)) {
            return success();
        }

        return irisGb(deviceId, channelId, command, speed);
    }

    /** GB 光圈控制（SIP 命令） */
    private AjaxResult irisGb(String deviceId, String channelId, String command, int speed) {
        if (speed < 0 || speed > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "speed 为 0-255的数字");
        }

        int cmdCode = 0x40;
        switch (command) {
            case "in":  cmdCode = 0x44; break;
            case "out": cmdCode = 0x48; break;
            case "stop": speed = 0;      break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效光圈指令: " + command);
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        ptzService.frontEndCommand(device, channelId, cmdCode, 0, speed, 0);
        return success();
    }

    /** ONVIF 光圈控制（SOAP Imaging SetImagingSettings）
     *  将 GB 风格参数映射到 ONVIF 绝对值模型 */
    private AjaxResult irisOnvif(String channelId, String command, int speed) {
        Long onvifChannelId = resolveOnvifChannelId(channelId);

        float irisValue;
        switch (command) {
            case "in":  irisValue = (255 - speed) / 255.0f; break;  // 缩小光圈 → iris 值减小
            case "out": irisValue = speed / 255.0f;          break;  // 放大光圈 → iris 值增大
            case "stop":
                // ONVIF iris 为绝对值定位，"stop" 概念不适用，直接返回成功（维持当前值）
                return success("ONVIF光圈为绝对值定位，已维持当前光圈值");
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "无效光圈指令: " + command);
        }

        // 钳位到 0.0~1.0
        irisValue = Math.max(0.0f, Math.min(1.0f, irisValue));

        log.info("[统一光圈-ONVIF] onvifChannelId: {}, irisValue: {}", onvifChannelId, irisValue);
         deviceOnvifService.irisSetByChannelId(onvifChannelId, irisValue);
        return success();
    }

    // ==================== 预置位 ====================

    /**
     * 统一预置位查询 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: 异步 SIP 查询 → DeferredResult
     * ONVIF: 同步 SOAP 查询 → 立即返回 AjaxResult
     * <p>
     * 为保持接口一致性，返回 DeferredResult；ONVIF 立即设值。
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/preset/query/{deviceId}/{channelId}")
    public DeferredResult<AjaxResult> presetQuery(@PathVariable String deviceId, @PathVariable String channelId) {
        log.info("[统一预置位查询] deviceId: {}, channelId: {}", deviceId, channelId);

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        // ---- ONVIF ----
        if ("ONVIF".equals(protocolType)) {
            DeferredResult<AjaxResult> result = new DeferredResult<>(5 * 1000L);
            try {
                Long onvifChannelId = resolveOnvifChannelId(channelId);
                List<OnvifUtils.PresetInfo> presetList = deviceOnvifService.getPresetListByChannelId(onvifChannelId);
                if (presetList == null) {
                    presetList = new ArrayList<>();
                }
                result.setResult(AjaxResult.success(presetList));
            } catch (Exception e) {
                // 设备不支持/查询失败时静默返回空列表，避免前端弹窗
                log.warn("[统一预置位查询-ONVIF] 查询失败，返回空列表, channelId: {}, msg: {}", channelId, e.getMessage());
                result.setResult(AjaxResult.success(new ArrayList<>()));
            }
            return result;
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeferredResult<AjaxResult> result = new DeferredResult<>(5 * 1000L);
            List<Map<String, Object>> presetList = new ArrayList<>();
            try {
                DeviceOnvifChannel channel = resolveIsupChannel(channelId);
                List<HaiKangIsupPresetInfo> presets = deviceIsupService.getPresetList(isupDeviceDbId(device, channel),
                        isupChannelNo(channel));
                // 统一成与 GB/ONVIF 一致的 presetId / presetName 结构
                if (presets != null) {
                    for (HaiKangIsupPresetInfo preset : presets) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("presetId", preset.getIndex());
                        item.put("presetName", preset.getName());
                        presetList.add(item);
                    }
                }
            } catch (Exception e) {
                // 设备不支持/查询失败时静默返回空列表，避免前端弹窗
                log.warn("[统一预置位查询-ISUP] 查询失败，返回空列表, channelId: {}, msg: {}", channelId, e.getMessage());
            }
            result.setResult(AjaxResult.success(presetList));
            return result;
        }

        // ---- GB28181 ----
        // 说明：部分国标设备不支持预置位查询或响应很慢，此处统一"静默降级"为返回空列表，
        // 不再向前端返回错误码，避免播放界面反复弹「获取设备预置位超时」。
        DeferredResult<AjaxResult> result = new DeferredResult<>(3 * 1000L);
        String uuid = UUID.randomUUID().toString();
        String key = DeferredResultHolder.CALLBACK_CMD_PRESETQUERY + channelId;

        result.onTimeout(() -> {
            log.warn("[统一预置位查询-GB] 获取设备预置位超时，返回空列表, deviceId: {}, channelId: {}", deviceId, channelId);
            RequestMessage msg = new RequestMessage();
            msg.setId(uuid);
            msg.setKey(key);
            msg.setData(AjaxResult.success(new ArrayList<>()));
            resultHolder.invokeResult(msg);
        });

        if (resultHolder.exist(key, null)) {
            return result;
        }

        resultHolder.put(key, uuid, result);

        try {
            cmder.presetQuery(device, channelId, event -> {
                log.warn("[统一预置位查询-GB] 获取设备预置位失败，返回空列表, statusCode: {}, msg: {}",
                        event.statusCode, event.msg);
                RequestMessage msg = new RequestMessage();
                msg.setId(uuid);
                msg.setKey(key);
                msg.setData(AjaxResult.success(new ArrayList<>()));
                resultHolder.invokeResult(msg);
            });
        } catch (Exception e) {
            log.warn("[统一预置位查询-GB] 命令发送失败，返回空列表, deviceId: {}, channelId: {}, msg: {}",
                    deviceId, channelId, e.getMessage());
            RequestMessage msg = new RequestMessage();
            msg.setId(uuid);
            msg.setKey(key);
            msg.setData(AjaxResult.success(new ArrayList<>()));
            resultHolder.invokeResult(msg);
        }
        return result;
    }

    /**
     * 统一预置位添加 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: presetId 为数字编号 (1-255) → SIP 命令
     * ONVIF: presetId 为预置位名称 (字符串) → SOAP setPreset
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @param presetId  预置位编号(GB: 1-255) 或 名称(ONVIF: 字符串)
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/preset/add/{deviceId}/{channelId}")
    public AjaxResult presetAdd(@PathVariable String deviceId, @PathVariable String channelId,
                                String presetId) {
        log.info("[统一预置位添加] deviceId: {}, channelId: {}, presetId: {}", deviceId, channelId, presetId);

        if (presetId == null || presetId.isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "预置位编号/名称不可为空");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        if ("ONVIF".equals(protocolType)) {
            Long onvifChannelId = resolveOnvifChannelId(channelId);
            deviceOnvifService.addPresetByChannelId(onvifChannelId, presetId);
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            deviceIsupService.setPreset(isupDeviceDbId(device, channel), isupChannelNo(channel), isupPresetId(presetId));
            return success();
        }

        // GB: presetId 必须为 1-255 的数字
        int gbPresetId;
        try {
            gbPresetId = Integer.parseInt(presetId);
        } catch (NumberFormatException e) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }
        if (gbPresetId < 1 || gbPresetId > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }

        ptzService.frontEndCommand(device, channelId, 0x81, 1, gbPresetId, 0);
        return success();
    }

    /**
     * 统一预置位调用 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: presetId 为数字编号 (1-255) → SIP 命令
     * ONVIF: presetId 为预置位 token (字符串) → SOAP gotoPreset
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @param presetId  预置位编号(GB: 1-255) 或 token(ONVIF: 字符串)
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/preset/call/{deviceId}/{channelId}")
    public AjaxResult presetCall(@PathVariable String deviceId, @PathVariable String channelId,
                                 String presetId) {
        log.info("[统一预置位调用] deviceId: {}, channelId: {}, presetId: {}", deviceId, channelId, presetId);

        if (presetId == null || presetId.isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "预置位编号/token不可为空");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        if ("ONVIF".equals(protocolType)) {
            Long onvifChannelId = resolveOnvifChannelId(channelId);
            deviceOnvifService.gotoPresetByChannelId(onvifChannelId, presetId);
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            deviceIsupService.gotoPreset(isupDeviceDbId(device, channel), isupChannelNo(channel), isupPresetId(presetId));
            return success();
        }

        // GB: presetId 必须为 1-255 的数字
        int gbPresetId;
        try {
            gbPresetId = Integer.parseInt(presetId);
        } catch (NumberFormatException e) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }
        if (gbPresetId < 1 || gbPresetId > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }

        ptzService.frontEndCommand(device, channelId, 0x82, 1, gbPresetId, 0);
        return success();
    }

    /**
     * 统一预置位删除 —— 兼容 GB28181 / ONVIF
     * <p>
     * GB: presetId 为数字编号 (1-255) → SIP 命令
     * ONVIF: presetId 为预置位 token (字符串) → SOAP removePreset
     *
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @param presetId  预置位编号(GB: 1-255) 或 token(ONVIF: 字符串)
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/preset/delete/{deviceId}/{channelId}")
    public AjaxResult presetDelete(@PathVariable String deviceId, @PathVariable String channelId,
                                   String presetId) {
        log.info("[统一预置位删除] deviceId: {}, channelId: {}, presetId: {}", deviceId, channelId, presetId);

        if (presetId == null || presetId.isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "预置位编号/token不可为空");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        if ("ONVIF".equals(protocolType)) {
            Long onvifChannelId = resolveOnvifChannelId(channelId);
            deviceOnvifService.removePresetByChannelId(onvifChannelId, presetId);
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            deviceIsupService.clearPreset(isupDeviceDbId(device, channel), isupChannelNo(channel), isupPresetId(presetId));
            return success();
        }

        // GB: presetId 必须为 1-255 的数字
        int gbPresetId;
        try {
            gbPresetId = Integer.parseInt(presetId);
        } catch (NumberFormatException e) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }
        if (gbPresetId < 1 || gbPresetId > 255) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "国标设备预置位编号必须为1-255之间的数字");
        }

        ptzService.frontEndCommand(device, channelId, 0x83, 1, gbPresetId, 0);
        return success();
    }

    // ==================== 云台扩展：巡航 / 扫描 / 雨刷 / 辅助开关 ====================

    /**
     * 统一巡航控制（兼容 GB28181 / ISUP，ONVIF 无巡航组概念，静默成功）
     * <p>
     * operation:
     * add_to_cruise     加入巡航点（presetId）
     * remove_from_cruise 删除巡航点（presetId）
     * set_cruise_speed  设置巡航速度（value）
     * set_cruise_dwell  设置巡航停留时间（value，秒）
     * start_cruise      开始巡航
     * stop_cruise       停止巡航
     *
     * @param route    巡航组号，默认 1
     * @param presetId 预置位编号（add/remove 使用）
     * @param value    速度或停留时间（set_cruise_* 使用）
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/cruise/{deviceId}/{channelId}")
    public AjaxResult cruise(@PathVariable String deviceId, @PathVariable String channelId,
                             String operation, Integer route, Integer presetId, Integer value) {
        log.info("[统一巡航] deviceId: {}, channelId: {}, operation: {}, route: {}, presetId: {}, value: {}",
                deviceId, channelId, operation, route, presetId, value);

        if (operation == null || operation.isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "巡航操作不可为空");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();
        int routeNo = (route == null || route <= 0) ? 1 : route;

        // ---- ONVIF：无巡航组概念，静默成功 ----
        if ("ONVIF".equals(protocolType)) {
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            Long dbId = isupDeviceDbId(device, channel);
            int chNo = isupChannelNo(channel);
            switch (operation) {
                case "add_to_cruise":
                case "remove_from_cruise":
                    deviceIsupService.cruiseControl(dbId, chNo, operation,
                            requirePositive(presetId, "预置位编号"));
                    break;
                case "set_cruise_speed":
                case "set_cruise_dwell":
                    deviceIsupService.cruiseControl(dbId, chNo, operation,
                            requirePositive(value, "参数值"));
                    break;
                case "start_cruise":
                case "stop_cruise":
                    deviceIsupService.cruiseControl(dbId, chNo, operation, routeNo);
                    break;
                default:
                    throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持的巡航操作: " + operation);
            }
            return success();
        }

        // ---- GB28181 ----
        switch (operation) {
            case "add_to_cruise":
                ptzService.frontEndCommand(device, channelId, 0x84, routeNo, requirePositive(presetId, "预置位编号"), 0);
                break;
            case "remove_from_cruise":
                ptzService.frontEndCommand(device, channelId, 0x85, routeNo, requirePositive(presetId, "预置位编号"), 0);
                break;
            case "set_cruise_speed": {
                int v = requirePositive(value, "巡航速度");
                ptzService.frontEndCommand(device, channelId, 0x86, routeNo, v & 0xFF, v >> 8);
                break;
            }
            case "set_cruise_dwell": {
                int v = requirePositive(value, "停留时间");
                ptzService.frontEndCommand(device, channelId, 0x87, routeNo, v & 0xFF, v >> 8);
                break;
            }
            case "start_cruise":
                ptzService.frontEndCommand(device, channelId, 0x88, routeNo, 0, 0);
                break;
            case "stop_cruise":
                ptzService.frontEndCommand(device, channelId, 0x00, 0, 0, 0);
                break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持的巡航操作: " + operation);
        }
        return success();
    }

    /**
     * 统一自动扫描控制（兼容 GB28181 / ISUP）
     * <p>
     * operation:
     * set_scan_left     设置左边界
     * set_scan_right    设置右边界
     * set_scan_speed    设置扫描速度（speed）
     * start_auto_scan   开始自动扫描
     * stop_auto_scan    停止自动扫描
     * <p>
     * ISUP 的边界与速度由设备本地配置，平台侧只支持开始/停止，其余操作静默成功。
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/scan/{deviceId}/{channelId}")
    public AjaxResult scan(@PathVariable String deviceId, @PathVariable String channelId,
                           String operation, Integer scanId, Integer speed) {
        log.info("[统一扫描] deviceId: {}, channelId: {}, operation: {}, scanId: {}, speed: {}",
                deviceId, channelId, operation, scanId, speed);

        if (operation == null || operation.isEmpty()) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "扫描操作不可为空");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();
        int scanNo = (scanId == null || scanId <= 0) ? 1 : scanId;

        // ---- ONVIF：不支持自动扫描，静默成功 ----
        if ("ONVIF".equals(protocolType)) {
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            Long dbId = isupDeviceDbId(device, channel);
            int chNo = isupChannelNo(channel);
            switch (operation) {
                case "start_auto_scan":
                case "stop_auto_scan":
                    deviceIsupService.cruiseControl(dbId, chNo, operation, scanNo);
                    break;
                case "set_scan_left":
                case "set_scan_right":
                case "set_scan_speed":
                    // ISUP 自动扫描的边界/速度在设备端配置，平台侧不下发
                    log.info("[统一扫描-ISUP] 设备端配置项，忽略: {}", operation);
                    break;
                default:
                    throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持的扫描操作: " + operation);
            }
            return success();
        }

        // ---- GB28181 ----
        switch (operation) {
            case "set_scan_left":
                ptzService.frontEndCommand(device, channelId, 0x89, scanNo, 1, 0);
                break;
            case "set_scan_right":
                ptzService.frontEndCommand(device, channelId, 0x89, scanNo, 2, 0);
                break;
            case "set_scan_speed": {
                int v = requirePositive(speed, "扫描速度");
                ptzService.frontEndCommand(device, channelId, 0x8A, scanNo, v & 0xFF, v >> 8);
                break;
            }
            case "start_auto_scan":
                ptzService.frontEndCommand(device, channelId, 0x89, scanNo, 0, 0);
                break;
            case "stop_auto_scan":
                ptzService.frontEndCommand(device, channelId, 0x00, 0, 0, 0);
                break;
            default:
                throw new ControllerException(ErrorCode.ERROR100.getCode(), "不支持的扫描操作: " + operation);
        }
        return success();
    }

    /**
     * 统一雨刷控制（兼容 GB28181 / ISUP，ONVIF 静默成功）
     *
     * @param command on / off
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/wiper/{deviceId}/{channelId}")
    public AjaxResult wiper(@PathVariable String deviceId, @PathVariable String channelId, String command) {
        log.info("[统一雨刷] deviceId: {}, channelId: {}, command: {}", deviceId, channelId, command);

        boolean start = "on".equalsIgnoreCase(command);
        boolean stop = "off".equalsIgnoreCase(command);
        if (!start && !stop) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "雨刷指令仅支持 on/off");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        // ---- ONVIF：静默成功 ----
        if ("ONVIF".equals(protocolType)) {
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            deviceIsupService.cameraAuxControl(isupDeviceDbId(device, channel), isupChannelNo(channel), "wiper", start);
            return success();
        }

        // ---- GB28181 ----
        ptzService.frontEndCommand(device, channelId, start ? 0x8c : 0x8d, 1, 0, 0);
        return success();
    }

    /**
     * 统一辅助开关控制（兼容 GB28181 / ISUP，ONVIF 静默成功）
     *
     * @param command   on / off
     * @param switchId  GB 开关编号（默认 1）
     * @param operation ISUP 辅助设备类型：light / fan / heater / aux1 / aux2 / wiper（默认 light）
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:ptz')")
    @GetMapping("/auxiliary/{deviceId}/{channelId}")
    public AjaxResult auxiliary(@PathVariable String deviceId, @PathVariable String channelId,
                                String command, Integer switchId, String operation) {
        log.info("[统一辅助开关] deviceId: {}, channelId: {}, command: {}, switchId: {}, operation: {}",
                deviceId, channelId, command, switchId, operation);

        boolean start = "on".equalsIgnoreCase(command);
        boolean stop = "off".equalsIgnoreCase(command);
        if (!start && !stop) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "辅助开关指令仅支持 on/off");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        assertPtzSupported(device);

        String protocolType = device.getProtocolType();

        // ---- ONVIF：静默成功 ----
        if ("ONVIF".equals(protocolType)) {
            return success();
        }

        // ---- ISUP ----
        if ("ISUP".equals(protocolType)) {
            DeviceOnvifChannel channel = resolveIsupChannel(channelId);
            String auxType = (operation == null || operation.isEmpty()) ? "light" : operation;
            deviceIsupService.cameraAuxControl(isupDeviceDbId(device, channel), isupChannelNo(channel), auxType, start);
            return success();
        }

        // ---- GB28181 ----
        int sw = (switchId == null || switchId <= 0) ? 1 : switchId;
        ptzService.frontEndCommand(device, channelId, start ? 0x8c : 0x8d, sw, 0, 0);
        return success();
    }

    /** 云台扩展参数必填校验（正整数） */
    private int requirePositive(Integer value, String name) {
        if (value == null || value <= 0) {
            throw new ControllerException(ErrorCode.ERROR100.getCode(), name + "必须为正整数");
        }
        return value;
    }

    // ==================== 语音对讲（StmServer 模式，目前仅海康ISUP） ====================

    /**
     * 开始语音对讲。
     * <p>
     * ISUP 走 {@code NET_ECMS_StartVoiceWithStmServer}：设备对讲音频回推到平台流服务的
     * {@code haikang-isup.ports.voice} 端口，平台侧音频随后通过 {@link #voiceAudio} 下发。
     * 现场声音由实时预览的音频轨提供，避免双路音频造成回声。
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @GetMapping("/voice/start/{deviceId}/{channelId}")
    public AjaxResult voiceStart(@PathVariable String deviceId, @PathVariable String channelId) {
        log.info("[统一语音对讲-开始] deviceId: {}, channelId: {}", deviceId, channelId);

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");

        if (!"ISUP".equals(device.getProtocolType())) {
            return AjaxResult.error("仅海康ISUP 设备支持该对讲接口，其他协议请使用原有对讲入口");
        }

        DeviceOnvifChannel channel = resolveIsupChannel(channelId);
        deviceIsupService.startVoiceTalk(isupDeviceDbId(device, channel), isupChannelNo(channel));
        return AjaxResult.success("对讲已开始");
    }

    /**
     * 上送平台侧语音数据。
     * <p>
     * 请求体 {@code {"data": "<base64 PCM16LE 8000Hz 单声道>"}}，服务端转 G.711A 后下发设备；
     * 设备尚未回连对讲端口时返回 sent=false，数据丢弃，前端持续发送即可。
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @PostMapping("/voice/audio/{deviceId}/{channelId}")
    public AjaxResult voiceAudio(@PathVariable String deviceId, @PathVariable String channelId,
                                 @RequestBody Map<String, String> body) {
        String base64 = body == null ? null : body.get("data");
        if (ObjectUtils.isEmpty(base64)) {
            return AjaxResult.error("语音数据为空");
        }
        byte[] pcm;
        try {
            pcm = Base64.getDecoder().decode(base64);
        } catch (IllegalArgumentException e) {
            return AjaxResult.error("语音数据格式错误");
        }
        if (pcm.length > 64 * 1024) {
            return AjaxResult.error("单次语音数据过大");
        }

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        if (!"ISUP".equals(device.getProtocolType())) {
            return AjaxResult.error("仅海康ISUP 设备支持该对讲接口");
        }
        DeviceOnvifChannel channel = resolveIsupChannel(channelId);
        boolean sent = deviceIsupService.sendVoiceTalkAudio(isupDeviceDbId(device, channel), isupChannelNo(channel), pcm);
        return AjaxResult.success(sent);
    }

    /**
     * 停止语音对讲（幂等）
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @GetMapping("/voice/stop/{deviceId}/{channelId}")
    public AjaxResult voiceStop(@PathVariable String deviceId, @PathVariable String channelId) {
        log.info("[统一语音对讲-停止] deviceId: {}, channelId: {}", deviceId, channelId);

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在");
        if (!"ISUP".equals(device.getProtocolType())) {
            return AjaxResult.success("当前协议无需释放对讲资源");
        }
        DeviceOnvifChannel channel = resolveIsupChannel(channelId);
        deviceIsupService.stopVoiceTalk(isupDeviceDbId(device, channel), isupChannelNo(channel));
        return AjaxResult.success("对讲已停止");
    }

    /**
     * 对讲链路状态：前端用来区分“等待设备回连”和“已连接”，也方便排查音频是否真的下发
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @GetMapping("/voice/status/{deviceId}/{channelId}")
    public AjaxResult voiceStatus(@PathVariable String deviceId, @PathVariable String channelId) {
        Device device = deviceService.getDeviceByDeviceId(deviceId);
        if (device == null || !"ISUP".equals(device.getProtocolType())) {
            return AjaxResult.success("当前协议无对讲链路状态");
        }
        DeviceOnvifChannel channel = resolveIsupChannel(channelId);
        return AjaxResult.success(deviceIsupService.getVoiceTalkStatus(isupDeviceDbId(device, channel),
                isupChannelNo(channel)));
    }

    // ==================== 统一通道删除 ====================

    /**
     * 统一通道删除（兼容国标 / ONVIF / 推流 / 拉流代理）
     * @param id CommonGBChannel.gbId
     */
    @PreAuthorize("@ss.hasPermi('wvp:channel:edit')")
    @PostMapping("/channel/delete/{id}")
    public AjaxResult deleteChannel(@PathVariable Integer id) {
        CommonGBChannel channel = gbChannelService.getOne(id);
        if (channel == null) {
            return error("通道不存在");
        }

        int dataType = channel.getDataType() != null ? channel.getDataType() : 0;
        Integer dataDeviceId = channel.getDataDeviceId();

        if (dataType == ChannelDataType.ONVIF.value) {
            // ONVIF: 根据 channel.deviceId 查到 wvp_device_channel 记录后删除
            DeviceOnvifChannel onvifChannel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channel.getDeviceId());
            if (onvifChannel != null) {
                deviceOnvifChannelService.deleteDeviceOnvifChannelById(onvifChannel.getId());
            }
        } else if (dataType == ChannelDataType.STREAM_PUSH.value) {
            // 推流: 删除 stream_push 记录
            streamPushService.delete(dataDeviceId);
        } else if (dataType == ChannelDataType.STREAM_PROXY.value) {
            // 拉流代理: 删除 stream_proxy 记录
            streamProxyService.delete(dataDeviceId);
        }
        // 国标等其他类型直接删 CommonGBChannel

        gbChannelService.delete(id);
        return success();
    }
}
