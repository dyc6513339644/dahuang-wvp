package com.ruoyi.web.controller.play;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ControllerException;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import com.ruoyi.wvp.common.InviteSessionType;
import com.ruoyi.wvp.common.StreamInfo;
import com.ruoyi.wvp.common.enums.ChannelDataType;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.service.*;
import com.ruoyi.wvp.gb28181.transmit.callback.DeferredResultHolder;
import com.ruoyi.wvp.gb28181.transmit.callback.RequestMessage;
import com.ruoyi.wvp.gb28181.transmit.cmd.impl.SIPCommander;
import com.ruoyi.wvp.media.bean.MediaServer;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.service.bean.InviteErrorCode;
import com.ruoyi.wvp.streamProxy.bean.StreamProxy;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyPlayService;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.streamPush.service.IStreamPushPlayService;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import com.ruoyi.wvp.vmanager.bean.StreamContent;
import com.ruoyi.wvp.vmanager.bean.WVPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 统一播放控制器 —— 兼容国标(GB28181)、ONVIF、拉流代理(STREAM_PROXY)、推流(STREAM_PUSH)
 * <p>
 * 参数风格与国标播放接口保持一致（String deviceId / channelId、command、速度 0-255 等），
 * 内部根据 device.protocolType 自动分发到对应协议的服务层。
 * <p>
 * 注意：本控制器为新增接口，不改动任何原有国标播放/云台/预置位控制器。
 *
 * @author unified
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
    private IInviteStreamService inviteStreamService;

    @Autowired
    private DeferredResultHolder resultHolder;

    @Autowired
    private UserSetting userSetting;

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

    // ==================== 播放 ====================

    /**
     * 统一播放 —— 兼容 GB28181 / ONVIF / STREAM_PROXY / STREAM_PUSH
     * @param deviceId  设备国标编号
     * @param channelId 通道国标编号
     * @return 流信息 (DeferredResult)
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start')")
    @GetMapping("/play/start/{deviceId}/{channelId}")
    public DeferredResult<WVPResult<StreamContent>> play(HttpServletRequest request,
                                                          @PathVariable String deviceId,
                                                          @PathVariable String channelId) {
        log.info("[统一播放] deviceId: {}, channelId: {}", deviceId, channelId);
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

    /** 构建流信息异步回调 WVPResult（GB / 代理 / 推流 共用） */
    private WVPResult<StreamContent> buildStreamWvpResult(HttpServletRequest request, int code, String msg,
                                                           StreamInfo streamInfo, MediaServer mediaServerItem) {
        WVPResult<StreamContent> wvpResult = new WVPResult<>();
        if (code == InviteErrorCode.SUCCESS.getCode()) {
            wvpResult.setCode(ErrorCode.SUCCESS.getCode());
            wvpResult.setMsg(ErrorCode.SUCCESS.getMsg());
            if (streamInfo != null) {
                if (userSetting.getUseSourceIpAsStreamIp()) {
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
        if (streamInfo != null && userSetting.getUseSourceIpAsStreamIp()) {
            streamInfo = streamInfo.clone();
            String host = resolveRequestHost(request);
            streamInfo.channgeStreamIp(host);
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
            Long onvifChannelId = resolveOnvifChannelId(channelId);
            List<OnvifUtils.PresetInfo> presetList = deviceOnvifService.getPresetListByChannelId(onvifChannelId);
            result.setResult(AjaxResult.success(presetList));
            return result;
        }

        // ---- GB28181 ----
        DeferredResult<AjaxResult> result = new DeferredResult<>(3 * 1000L);
        String uuid = UUID.randomUUID().toString();
        String key = DeferredResultHolder.CALLBACK_CMD_PRESETQUERY + channelId;

        result.onTimeout(() -> {
            log.warn("[统一预置位查询-GB] 获取设备预置位超时");
            RequestMessage msg = new RequestMessage();
            msg.setId(uuid);
            msg.setKey(key);
            msg.setData(AjaxResult.error("获取设备预置位超时"));
            resultHolder.invokeResult(msg);
        });

        if (resultHolder.exist(key, null)) {
            return result;
        }

        resultHolder.put(key, uuid, result);

        try {
            cmder.presetQuery(device, channelId, event -> {
                RequestMessage msg = new RequestMessage();
                msg.setId(uuid);
                msg.setKey(key);
                msg.setData(AjaxResult.error("获取设备预置位失败，错误码: " + event.statusCode + ", " + event.msg));
                resultHolder.invokeResult(msg);
            });
        } catch (Exception e) {
            log.error("[命令发送失败] 获取设备预置位: {}", e.getMessage());
            throw new ControllerException(ErrorCode.ERROR100.getCode(), "命令发送失败: " + e.getMessage());
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
