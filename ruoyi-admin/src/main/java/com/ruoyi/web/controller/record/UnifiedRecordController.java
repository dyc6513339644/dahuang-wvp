package com.ruoyi.web.controller.record;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.isup.service.IDeviceIsupService;
import com.ruoyi.isup.service.IDeviceIsupStreamService;
import com.ruoyi.isup.service.IsupStreamCallback;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.domain.StreamContent;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.media.domain.WVPResult;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.wvp.common.InviteSessionType;
import com.ruoyi.wvp.common.StreamUrlHelper;
import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.bean.RecordItem;
import com.ruoyi.wvp.gb28181.service.IDeviceChannelService;
import com.ruoyi.wvp.gb28181.service.IDeviceService;
import com.ruoyi.wvp.gb28181.service.IPlayService;
import com.ruoyi.wvp.gb28181.transmit.callback.DeferredResultHolder;
import com.ruoyi.wvp.gb28181.transmit.callback.RequestMessage;
import com.ruoyi.wvp.gb28181.transmit.cmd.impl.SIPCommander;
import com.ruoyi.wvp.service.bean.InviteErrorCode;
import com.ruoyi.wvp.vmanager.bean.DeferredResultEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 统一录像接口：对外只暴露一套查询/回放/停止，内部按设备协议路由。
 * <p>
 * 目前支持：
 * <ul>
 *   <li>GB28181 —— SIP 录像查询（RecordInfo 消息）、SIP 回放邀请</li>
 *   <li>海康 ISUP —— SDK {@code NET_ECMS_StartFindFile_V11} 检索、{@code NET_ECMS_StartPlayBack} 回放</li>
 * </ul>
 * 其余协议（ONVIF / 推流 / 拉流）没有本地录像检索能力，直接返回不支持。
 * <p>
 * 对外统一的时间格式为 {@code yyyy-MM-dd HH:mm:ss}，统一的录像类型为
 * {@code time-定时 / alarm-报警 / manual-手动}，同时带 {@code typeName} 中文描述。
 */
@Slf4j
@RestController
@RequestMapping("/api/unified/record")
public class UnifiedRecordController extends BaseController {

    /** 协议：海康 ISUP */
    private static final String PROTOCOL_ISUP = "ISUP";

    /** 协议：国标 GB28181（库里的默认值，未显式标记协议时也归到这里） */
    private static final String PROTOCOL_GB = "GB28181";

    /** 统一类型：定时录像 */
    private static final String TYPE_TIME = "time";
    /** 统一类型：报警录像 */
    private static final String TYPE_ALARM = "alarm";
    /** 统一类型：手动录像 */
    private static final String TYPE_MANUAL = "manual";

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IDeviceChannelService deviceChannelService;

    @Autowired
    private IPlayService playService;

    @Autowired
    private SIPCommander cmder;

    @Autowired
    private DeferredResultHolder resultHolder;

    @Autowired
    private UserSetting userSetting;

    @Autowired
    private StreamUrlHelper streamUrlHelper;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    @Autowired
    private IDeviceIsupService deviceIsupService;

    @Autowired
    private IDeviceIsupStreamService deviceIsupStreamService;

    // ==================== 录像查询 ====================

    /**
     * 统一录像查询。
     *
     * @param deviceId  统一设备编号（国标设备编号 / haikang_isup_xxx）
     * @param channelId 统一通道编号（国标通道编号 / ISUP 的 CommonGBChannel.deviceId）
     * @param startTime 开始时间 yyyy-MM-dd HH:mm:ss
     * @param endTime   结束时间 yyyy-MM-dd HH:mm:ss
     * @param type      类型过滤：all/time/alarm/manual，默认 all
     * @return 平铺结构：protocolType / deviceId / channelId / total / records[]
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start') or @ss.hasPermi('gb:record:query') or @ss.hasPermi('isup:device:record')")
    @GetMapping("/query")
    public DeferredResult<AjaxResult> query(String deviceId, String channelId,
                                            String startTime, String endTime,
                                            @RequestParam(required = false, defaultValue = "all") String type) {
        Assert.hasText(deviceId, "设备编号不可为空");
        Assert.hasText(channelId, "通道编号不可为空");
        Assert.hasText(startTime, "开始时间不可为空");
        Assert.hasText(endTime, "结束时间不可为空");

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在: " + deviceId);

        String protocolType = resolveProtocol(device);
        log.info("[统一录像查询] deviceId:{}, channelId:{}, protocol:{}, 时间:{} ~ {}",
                deviceId, channelId, protocolType, startTime, endTime);

        if (PROTOCOL_ISUP.equals(protocolType)) {
            return queryIsup(device, channelId, startTime, endTime, type);
        }
        if (PROTOCOL_GB.equals(protocolType)) {
            return queryGb(device, channelId, startTime, endTime, type);
        }
        // ONVIF / 推流 / 拉流 没有本地录像检索能力：按「空结果」返回而非报错，
        // 前端各入口统一展示「暂无录像」，避免不同协议出现不同提示文案
        DeferredResult<AjaxResult> result = new DeferredResult<>();
        result.setResult(buildQueryResult(protocolType, deviceId, channelId, Collections.emptyList()));
        return result;
    }

    /** ISUP：SDK 同步检索，套一层 DeferredResult 与国标保持同一返回类型 */
    private DeferredResult<AjaxResult> queryIsup(Device device, String channelId, String startTime,
                                                 String endTime, String type) {
        DeferredResult<AjaxResult> result = new DeferredResult<>(60_000L);
        List<Map<String, Object>> records = new ArrayList<>();
        try {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
            Assert.notNull(channel, "ISUP通道不存在: " + channelId);
            Long dbId = channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();
            Integer channelNo = parseIsupChannelNo(channel);

            ArrayList<HashMap<String, Object>> raw =
                    deviceIsupService.queryRecord(dbId, channelNo, startTime, endTime);
            if (raw != null) {
                for (HashMap<String, Object> item : raw) {
                    String typeName = asString(item.get("type"));
                    Map<String, Object> record = new LinkedHashMap<>(8);
                    record.put("startTime", asString(item.get("start")));
                    record.put("endTime", asString(item.get("end")));
                    record.put("type", toUnifiedType(typeName));
                    record.put("typeName", StringUtils.isBlank(typeName) ? "录像" : typeName);
                    record.put("fileName", asString(item.get("fileName")));
                    record.put("fileSize", item.get("fileSize"));
                    records.add(record);
                }
            }
        } catch (ServiceException e) {
            // 设备没有录像时会抛「未查询到录像文件」，这属于正常结果，不当作错误
            if (StringUtils.contains(e.getMessage(), "未查询到录像")) {
                log.info("[统一录像查询] ISUP 无录像 deviceId:{}, channelId:{}", device.getDeviceId(), channelId);
            } else {
                log.warn("[统一录像查询] ISUP 查询失败 deviceId:{}, channelId:{}, err:{}",
                        device.getDeviceId(), channelId, e.getMessage());
                result.setResult(AjaxResult.error(e.getMessage()));
                return result;
            }
        } catch (Exception e) {
            log.error("[统一录像查询] ISUP 查询异常 deviceId:{}, channelId:{}", device.getDeviceId(), channelId, e);
            result.setResult(AjaxResult.error("录像查询失败: " + e.getMessage()));
            return result;
        }
        result.setResult(buildQueryResult(PROTOCOL_ISUP, device.getDeviceId(), channelId,
                filterByType(records, type)));
        return result;
    }

    /** GB28181：SIP 异步查询，结果通过 filter 转成统一结构 */
    private DeferredResult<AjaxResult> queryGb(Device device, String channelId, String startTime,
                                               String endTime, String type) {
        String uuid = UUID.randomUUID().toString();
        int sn = (int) ((Math.random() * 9 + 1) * 100000);
        String key = DeferredResultHolder.CALLBACK_CMD_RECORDINFO + device.getDeviceId() + sn;

        DeferredResult<AjaxResult> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());
        DeferredResultEx<AjaxResult> ex = new DeferredResultEx<>(result);
        ex.setFilter(data -> convertGbQueryResult(data, device.getDeviceId(), channelId, type));
        resultHolder.put(key, uuid, ex);

        result.onTimeout(() -> {
            log.warn("[统一录像查询] GB 查询超时 deviceId:{}, channelId:{}", device.getDeviceId(), channelId);
            finishHolder(key, uuid, "录像查询超时，设备未响应");
        });

        try {
            cmder.recordInfoQuery(device, channelId, startTime, endTime, sn, null, type, null, eventResult ->
                    finishHolder(key, uuid, "查询录像失败, status: " + eventResult.statusCode + ", message: " + eventResult.msg));
        } catch (Exception e) {
            log.error("[统一录像查询] GB 命令发送失败 deviceId:{}, channelId:{}, err:{}",
                    device.getDeviceId(), channelId, e.getMessage());
            finishHolder(key, uuid, "命令发送失败: " + e.getMessage());
        }
        return result;
    }

    /** 用 filter 完成挂起的请求（filter 会把错误文案转成 AjaxResult 并清理 key） */
    private void finishHolder(String key, String uuid, String message) {
        RequestMessage msg = new RequestMessage();
        msg.setKey(key);
        msg.setId(uuid);
        msg.setData(message);
        resultHolder.invokeAllResult(msg);
    }

    /**
     * 国标查询结果 → 统一结构。
     * 国标录像查询的 DeferredResult 里实际放的是 DeviceRecordInfo（也可能被包成 WVPResult），两种都相容。
     */
    @SuppressWarnings("rawtypes")
    private AjaxResult convertGbQueryResult(Object data, String deviceId, String channelId, String type) {
        if (data == null) {
            return AjaxResult.error("录像查询失败：设备无响应");
        }
        if (data instanceof String) {
            return AjaxResult.error((String) data);
        }
        Object payload = data;
        if (data instanceof WVPResult) {
            WVPResult wvpResult = (WVPResult) data;
            if (wvpResult.getCode() != ErrorCode.SUCCESS.getCode()) {
                return AjaxResult.error(StringUtils.defaultIfBlank(wvpResult.getMsg(), "录像查询失败"));
            }
            payload = wvpResult.getData();
        }
        if (!(payload instanceof com.ruoyi.wvp.gb28181.bean.RecordInfo)) {
            log.warn("[统一录像查询] GB 返回结构不认识: {}", payload.getClass().getName());
            return AjaxResult.error("录像查询失败：返回数据格式不支持");
        }
        com.ruoyi.wvp.gb28181.bean.RecordInfo info = (com.ruoyi.wvp.gb28181.bean.RecordInfo) payload;
        List<Map<String, Object>> records = new ArrayList<>();
        if (info.getRecordList() != null) {
            for (RecordItem item : info.getRecordList()) {
                String typeName = asString(item.getType());
                Map<String, Object> record = new LinkedHashMap<>(8);
                record.put("startTime", normalizeTime(item.getStartTime()));
                record.put("endTime", normalizeTime(item.getEndTime()));
                record.put("type", toUnifiedType(typeName));
                record.put("typeName", unifiedTypeName(toUnifiedType(typeName), item.getType()));
                record.put("fileName", StringUtils.defaultIfBlank(item.getName(), item.getFilePath()));
                record.put("fileSize", item.getFileSize());
                record.put("address", item.getAddress());
                records.add(record);
            }
        }
        return buildQueryResult(PROTOCOL_GB, deviceId, channelId, filterByType(records, type));
    }

    private AjaxResult buildQueryResult(String protocolType, String deviceId, String channelId,
                                        List<Map<String, Object>> records) {
        AjaxResult result = success();
        result.put("protocolType", protocolType);
        result.put("deviceId", deviceId);
        result.put("channelId", channelId);
        result.put("total", records.size());
        result.put("records", records);
        return result;
    }

    // ==================== 录像回放 ====================

    /**
     * 统一开始回放，返回与预览完全一致的流信息（flv / fmp4 / ws_flv）。
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:start') or @ss.hasPermi('gb:playback:start') or @ss.hasPermi('isup:device:playback')")
    @GetMapping("/playback/start")
    public DeferredResult<WVPResult<StreamContent>> playbackStart(HttpServletRequest request,
                                                                  String deviceId, String channelId,
                                                                  String startTime, String endTime) {
        Assert.hasText(deviceId, "设备编号不可为空");
        Assert.hasText(channelId, "通道编号不可为空");
        Assert.hasText(startTime, "开始时间不可为空");
        Assert.hasText(endTime, "结束时间不可为空");

        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在: " + deviceId);
        String protocolType = resolveProtocol(device);

        log.info("[统一录像回放] deviceId:{}, channelId:{}, protocol:{}, 时间:{} ~ {}",
                deviceId, channelId, protocolType, startTime, endTime);

        if (PROTOCOL_ISUP.equals(protocolType)) {
            return playbackIsup(request, device, channelId, startTime, endTime);
        }
        if (PROTOCOL_GB.equals(protocolType)) {
            return playbackGb(request, device, channelId, startTime, endTime);
        }
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>();
        result.setResult(WVPResult.fail(ErrorCode.ERROR100.getCode(),
                "当前协议不支持录像回放: " + protocolType));
        return result;
    }

    private DeferredResult<WVPResult<StreamContent>> playbackIsup(HttpServletRequest request, Device device,
                                                                  String channelId, String startTime, String endTime) {
        DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
        Assert.notNull(channel, "ISUP通道不存在: " + channelId);
        Long dbId = channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();
        Integer channelNo = parseIsupChannelNo(channel);

        String key = DeferredResultHolder.CALLBACK_CMD_PLAYBACK + device.getDeviceId() + channelId;
        String uuid = UUID.randomUUID().toString();
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());
        resultHolder.put(key, uuid, result);

        result.onTimeout(() -> {
            log.warn("[统一录像回放] ISUP 超时 deviceId:{}, channelId:{}", device.getDeviceId(), channelId);
            RequestMessage msg = new RequestMessage();
            msg.setKey(key);
            msg.setId(uuid);
            msg.setData(WVPResult.fail(ErrorCode.ERROR100.getCode(), "回放超时"));
            resultHolder.invokeAllResult(msg);
        });

        RtpServerParam rtpServerParam = new RtpServerParam();
        rtpServerParam.setId(dbId);
        rtpServerParam.setChannel(channelNo);
        rtpServerParam.setStartTime(startTime);
        rtpServerParam.setEndTime(endTime);
        try {
            IsupStreamCallback callback = (code, msg, streamInfo) -> {
                RequestMessage requestMessage = new RequestMessage();
                requestMessage.setKey(key);
                requestMessage.setId(uuid);
                requestMessage.setData(buildStreamResult(request, code, msg, streamInfo));
                resultHolder.invokeAllResult(requestMessage);
            };
            deviceIsupStreamService.startPlayback(rtpServerParam, callback);
        } catch (Exception e) {
            log.error("[统一录像回放] ISUP 开流异常 deviceId:{}, channelId:{}", device.getDeviceId(), channelId, e);
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setKey(key);
            requestMessage.setId(uuid);
            requestMessage.setData(WVPResult.fail(ErrorCode.ERROR100.getCode(), e.getMessage()));
            resultHolder.invokeAllResult(requestMessage);
        }
        return result;
    }

    private DeferredResult<WVPResult<StreamContent>> playbackGb(HttpServletRequest request, Device device,
                                                                String channelId, String startTime, String endTime) {
        DeviceChannel channel = deviceChannelService.getOne(device.getDeviceId(), channelId);
        Assert.notNull(channel, "GB通道不存在: " + channelId);

        String key = DeferredResultHolder.CALLBACK_CMD_PLAYBACK + device.getDeviceId() + channelId;
        String uuid = UUID.randomUUID().toString();
        DeferredResult<WVPResult<StreamContent>> result = new DeferredResult<>(userSetting.getPlayTimeout().longValue());
        resultHolder.put(key, uuid, result);

        result.onTimeout(() -> {
            log.warn("[统一录像回放] GB 超时 deviceId:{}, channelId:{}", device.getDeviceId(), channelId);
            RequestMessage msg = new RequestMessage();
            msg.setKey(key);
            msg.setId(uuid);
            msg.setData(WVPResult.fail(ErrorCode.ERROR100.getCode(), "回放超时"));
            resultHolder.invokeAllResult(msg);
        });

        playService.playBack(device, channel, startTime, endTime, (code, msg, data) -> {
            RequestMessage requestMessage = new RequestMessage();
            requestMessage.setKey(key);
            requestMessage.setId(uuid);
            requestMessage.setData(buildStreamResult(request, code, msg, (StreamInfo) data));
            resultHolder.invokeAllResult(requestMessage);
        });
        return result;
    }

    /**
     * 统一停止回放。
     * ISUP 只需设备+通道；GB 需要回放流 ID（start 响应里的 stream），不传时无法发 BYE。
     */
    @PreAuthorize("@ss.hasPermi('wvp:play:stop') or @ss.hasPermi('gb:playback:start') or @ss.hasPermi('isup:device:playback')")
    @GetMapping("/playback/stop/{deviceId}/{channelId}")
    public AjaxResult playbackStop(@PathVariable String deviceId, @PathVariable String channelId,
                                   @RequestParam(required = false) String stream) {
        Device device = deviceService.getDeviceByDeviceId(deviceId);
        Assert.notNull(device, "设备不存在: " + deviceId);
        String protocolType = resolveProtocol(device);

        if (PROTOCOL_ISUP.equals(protocolType)) {
            DeviceOnvifChannel channel = deviceOnvifChannelService.selectDeviceOnvifChannelByDeviceId(channelId);
            Assert.notNull(channel, "ISUP通道不存在: " + channelId);
            Long dbId = channel.getDataDeviceId() != null ? channel.getDataDeviceId() : device.getId();
            deviceIsupStreamService.stopPlayback(dbId, parseIsupChannelNo(channel));
            return success();
        }

        if (PROTOCOL_GB.equals(protocolType)) {
            Assert.hasText(stream, "国标停止回放需要 stream 参数（回放启动时返回的流ID）");
            DeviceChannel channel = deviceChannelService.getOneForSource(deviceId, channelId);
            Assert.notNull(channel, "GB通道不存在: " + channelId);
            playService.stop(InviteSessionType.PLAYBACK, device, channel, stream);
            return success();
        }

        return AjaxResult.error("当前协议不支持录像回放: " + protocolType);
    }

    // ==================== 内部工具 ====================

    /**
     * 协议识别：库里 protocolType 为空时按国标处理（历史数据大多如此）。
     * 只有显式标了 ISUP 才走海康分支，避免拦截到国标设备。
     */
    private String resolveProtocol(Device device) {
        String protocolType = device.getProtocolType();
        if (StringUtils.isBlank(protocolType) || "GB28181".equalsIgnoreCase(protocolType)) {
            return PROTOCOL_GB;
        }
        return protocolType.toUpperCase();
    }

    private int parseIsupChannelNo(DeviceOnvifChannel channel) {
        try {
            return Integer.parseInt(channel.getChannelNo());
        } catch (Exception e) {
            throw new ServiceException("ISUP通道号非法: " + channel.getChannelNo());
        }
    }

    private String asString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    /** 中文/英文类型名 → 统一三种类型 */
    private String toUnifiedType(String raw) {
        if (StringUtils.isBlank(raw)) {
            return TYPE_ALARM;
        }
        String value = raw.trim();
        if (StringUtils.contains(value, "定时") || "time".equalsIgnoreCase(value)) {
            return TYPE_TIME;
        }
        if (StringUtils.contains(value, "手动") || "manual".equalsIgnoreCase(value)) {
            return TYPE_MANUAL;
        }
        return TYPE_ALARM;
    }

    /** 统一类型 → 中文描述；设备给了更细的中文类型（如「移动侦测」）时优先用设备的 */
    private String unifiedTypeName(String unifiedType, String rawType) {
        if (StringUtils.isNotBlank(rawType) && !TYPE_TIME.equalsIgnoreCase(rawType)
                && !TYPE_ALARM.equalsIgnoreCase(rawType) && !TYPE_MANUAL.equalsIgnoreCase(rawType)
                && !"all".equalsIgnoreCase(rawType)) {
            return rawType;
        }
        switch (unifiedType) {
            case TYPE_TIME:
                return "定时录像";
            case TYPE_MANUAL:
                return "手动录像";
            default:
                return "报警录像";
        }
    }

    /** 按 type 过滤（all 或不传时不过滤） */
    private List<Map<String, Object>> filterByType(List<Map<String, Object>> records, String type) {
        if (StringUtils.isBlank(type) || "all".equalsIgnoreCase(type)) {
            return records;
        }
        List<Map<String, Object>> filtered = new ArrayList<>(records.size());
        for (Map<String, Object> record : records) {
            if (type.equalsIgnoreCase(String.valueOf(record.get("type")))) {
                filtered.add(record);
            }
        }
        return filtered;
    }

    /** 国标时间可能是 ISO8601（带 T / 时区），统一成 yyyy-MM-dd HH:mm:ss */
    private String normalizeTime(String time) {
        if (StringUtils.isBlank(time)) {
            return time;
        }
        String value = time.trim().replace('T', ' ');
        int dotIndex = value.indexOf('.');
        if (dotIndex > 0) {
            value = value.substring(0, dotIndex);
        }
        if (value.length() > 19) {
            value = value.substring(0, 19);
        }
        return value;
    }

    /** 流信息 → WVPResult，做 nginx 代理与非流媒体同源处理（与统一播放保持一致） */
    private WVPResult<StreamContent> buildStreamResult(HttpServletRequest request, int code, String msg,
                                                       StreamInfo streamInfo) {
        WVPResult<StreamContent> wvpResult = new WVPResult<>();
        if (code != InviteErrorCode.SUCCESS.getCode()) {
            wvpResult.setCode(code);
            wvpResult.setMsg(msg);
            return wvpResult;
        }
        wvpResult.setCode(ErrorCode.SUCCESS.getCode());
        wvpResult.setMsg(ErrorCode.SUCCESS.getMsg());
        if (streamInfo == null) {
            wvpResult.setCode(ErrorCode.ERROR100.getCode());
            wvpResult.setMsg("未获取到回放流信息");
            return wvpResult;
        }
        MediaServer mediaServer = streamInfo.getMediaServer();
        streamInfo = streamUrlHelper.applyNginxProxy(streamInfo, mediaServer);
        if (userSetting.getUseSourceIpAsStreamIp() && (mediaServer == null || !mediaServer.isNginxProxyEnabled())) {
            streamInfo = streamInfo.clone();
            streamInfo.channgeStreamIp(resolveRequestHost(request));
        }
        wvpResult.setData(new StreamContent(streamInfo));
        return wvpResult;
    }

    private String resolveRequestHost(HttpServletRequest request) {
        try {
            return new URL(request.getRequestURL().toString()).getHost();
        } catch (MalformedURLException e) {
            return request.getLocalAddr();
        }
    }
}
