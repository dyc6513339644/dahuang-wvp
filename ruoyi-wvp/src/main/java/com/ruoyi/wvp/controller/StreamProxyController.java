package com.ruoyi.wvp.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.ruoyi.wvp.common.StreamInfo;
import com.ruoyi.wvp.common.StreamUrlHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.service.IDeviceChannelService;
import com.ruoyi.wvp.mapper.DeviceMapper;
import com.ruoyi.wvp.media.bean.MediaServer;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.service.bean.ErrorCallback;
import com.ruoyi.wvp.service.bean.InviteErrorCode;
import com.ruoyi.wvp.streamProxy.bean.StreamProxyParam;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyPlayService;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.vmanager.bean.ResourceBaseInfo;
import com.ruoyi.wvp.vmanager.bean.StreamContent;
import com.ruoyi.wvp.vmanager.bean.WVPResult;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.transmit.callback.DeferredResultHolder;
import com.ruoyi.wvp.gb28181.transmit.callback.RequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 拉流代理控制器（基于 Device + DeviceChannel 表）
 */
@RestController
@RequestMapping("/api/proxy")
public class StreamProxyController extends BaseController {

    @Autowired
    private IStreamProxyService streamProxyService;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private IStreamProxyPlayService streamProxyPlayService;

    @Autowired
    private IDeviceChannelService deviceChannelService;

    @Autowired
    private DeferredResultHolder resultHolder;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private StreamUrlHelper streamUrlHelper;

    /**
     * 分页查询拉流代理列表（直接查 wvp_device，protocol_type='STREAM_PROXY'）
     */
    @GetMapping("/list")
    public TableDataInfo list(@RequestParam(required = false) String query, @RequestParam(required = false) Boolean onLine) {
        startPage();
        List<Device> list = deviceMapper.getProxyDeviceListByCondition(query, onLine);
        return getDataTable(list);
    }

    /**
     * 保存（新增或更新）拉流代理
     */
    @PostMapping("/save")
    @DS("master")
    public AjaxResult save(@RequestBody StreamProxyParam param) {
        StreamInfo streamInfo = streamProxyService.save(param);
        return AjaxResult.success(streamInfo);
    }

    /**
     * 删除拉流代理
     */
    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable int id) {
        streamProxyService.delete(id);
        return AjaxResult.success();
    }

    /**
     * 获取拉流代理详情
     */
    @GetMapping("/detail/{id}")
    public AjaxResult detail(@PathVariable int id) {
        return AjaxResult.success(streamProxyService.getStreamProxy(id));
    }

    /**
     * 获取ffmpeg命令列表
     */
    @GetMapping("/ffmpegcmds/{mediaServerId}")
    public AjaxResult getFFmpegCMDs(@PathVariable String mediaServerId) {
        MediaServer mediaServer = mediaServerService.getOne(mediaServerId);
        Map<String, String> ffmpegCMDs = streamProxyService.getFFmpegCMDs(mediaServer);
        return AjaxResult.success(ffmpegCMDs);
    }

    /**
     * 获取拉流代理统计概览
     */
    @GetMapping("/overview")
    public AjaxResult overview() {
        ResourceBaseInfo resourceBaseInfo = streamProxyService.getOverview();
        return AjaxResult.success(resourceBaseInfo);
    }

    /**
     * 启动拉流代理播放（id 为通道 ID，对应 wvp_device_channel.id）
     */
    @GetMapping("/start")
    public DeferredResult<WVPResult<StreamContent>> start(HttpServletRequest request, @RequestParam int id) {
        // 查询通道
        DeviceChannel channel = deviceChannelService.getOneById(id);
        if (channel == null) {
            throw new com.ruoyi.common.exception.ControllerException(ErrorCode.ERROR404.getCode(), "通道不存在");
        }
        // 查询设备
        Device device = deviceMapper.query(channel.getDataDeviceId());
        if (device == null || !"STREAM_PROXY".equals(device.getProtocolType())) {
            throw new com.ruoyi.common.exception.ControllerException(ErrorCode.ERROR404.getCode(), "拉流代理设备不存在");
        }

        RequestMessage requestMessage = new RequestMessage();
        String key = DeferredResultHolder.CALLBACK_CMD_PLAY + device.getDeviceId() + channel.getDeviceId();
        requestMessage.setKey(key);
        String uuid = UUID.randomUUID().toString();
        requestMessage.setId(uuid);
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());
        resultHolder.put(key, uuid, result);

        streamProxyPlayService.start(device.getId(), null, (code, msg, streamInfo) -> {
            WVPResult<StreamContent> wvpResult = new WVPResult<>();
            if (code == InviteErrorCode.SUCCESS.getCode()) {
                wvpResult.setCode(ErrorCode.SUCCESS.getCode());
                wvpResult.setMsg(ErrorCode.SUCCESS.getMsg());
                if (streamInfo != null) {
                    streamInfo = streamUrlHelper.applyNginxProxy(streamInfo, streamInfo.getMediaServer());
                    if (userSetting.getUseSourceIpAsStreamIp()) {
                        MediaServer ms = streamInfo.getMediaServer();
                        if (ms == null || !ms.isNginxProxyEnabled()) {
                            streamInfo = streamInfo.clone();
                            String host;
                            try {
                                URL url = new URL(request.getRequestURL().toString());
                                host = url.getHost();
                            } catch (MalformedURLException e) {
                                host = request.getLocalAddr();
                            }
                            streamInfo.channgeStreamIp(host);
                        }
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
            requestMessage.setData(wvpResult);
            resultHolder.invokeAllResult(requestMessage);
        });
        return result;
    }

    /**
     * 停止拉流代理
     */
    @GetMapping("/stop")
    public AjaxResult stop(@RequestParam(required = false) Integer id,
                           @RequestParam(required = false) String app,
                           @RequestParam(required = false) String stream) {
        if (id != null) {
            streamProxyService.stopByAppAndStream(
                    streamProxyService.getStreamProxy(id).getApp(),
                    streamProxyService.getStreamProxy(id).getStream());
        } else if (app != null && stream != null) {
            streamProxyService.stopByAppAndStream(app, stream);
        }
        return AjaxResult.success();
    }
}
