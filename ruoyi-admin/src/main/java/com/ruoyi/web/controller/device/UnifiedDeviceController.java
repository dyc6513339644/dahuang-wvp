package com.ruoyi.web.controller.device;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.domain.vo.DeviceChannelReqVo;
import com.ruoyi.onvif.domain.vo.DeviceReqVo;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import com.ruoyi.wvp.conf.DynamicTask;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.service.IGbChannelService;
import com.ruoyi.wvp.gb28181.service.IDeviceService;
import com.ruoyi.wvp.gb28181.service.IInviteStreamService;
import com.ruoyi.wvp.gb28181.task.ISubscribeTask;
import com.ruoyi.wvp.service.IUserChannelService;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.media.domain.StreamInfo;
import com.ruoyi.wvp.storager.IRedisCatchStorage;
import com.ruoyi.wvp.streamProxy.bean.StreamProxyParam;
import com.ruoyi.wvp.streamProxy.service.IStreamProxyService;
import com.ruoyi.wvp.streamPush.bean.StreamPush;
import com.ruoyi.wvp.streamPush.service.IStreamPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 统一设备管理
 */
@Slf4j
@RestController
@RequestMapping("/api/unified/device")
public class UnifiedDeviceController extends BaseController {

    @Autowired
    IDeviceOnvifService deviceOnvifService;

    @Autowired
    IDeviceOnvifChannelService deviceOnvifChannelService;

    @Autowired
    private IStreamPushService streamPushService;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;



    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IStreamProxyService streamProxyService;

    @Autowired
    private IInviteStreamService inviteStreamService;

    @Autowired
    private DynamicTask dynamicTask;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private IUserChannelService userChannelService;


    // ==================== 用户通道过滤辅助方法 ====================

    /**
     * 判断当前用户是否需要按分配通道过滤
     * @return true=需要过滤，false=不过滤（无分配通道则返回全部）
     */
    private boolean shouldFilterByUserChannel() {
        Long userId = getUserId();
        boolean needFilter = userChannelService.hasUserChannels(userId);
        log.debug("[UnifiedDevice] userId={}, needFilter={}", userId, needFilter);
        return needFilter;
    }

    /**
     * 获取当前用户分配的设备ID列表
     */
    private List<String> getUserAssignedDeviceIds() {
        return userChannelService.getDistinctDeviceIdsByUserId(getUserId());
    }

    // ==================== 设备查询 ====================

    //查询所有设备（含分配通道过滤：无分配通道则返回全部，有则只返回分配的设备）
    @PreAuthorize("@ss.hasPermi('wvp:device:list')")
    @GetMapping("/listAllDevice")
    public AjaxResult listAllDevice(DeviceReqVo deviceReqVo) {
        if (shouldFilterByUserChannel()) {
            List<String> deviceIds = getUserAssignedDeviceIds();
            if (deviceIds.isEmpty()) {
                return success(Collections.emptyList());
            }
            return success(deviceOnvifService.selectAllDeviceListFiltered(deviceReqVo, deviceIds));
        }
        return success(deviceOnvifService.selectAllDeviceList(deviceReqVo));
    }

    //查下设备带分页（含分配通道过滤）
    @PreAuthorize("@ss.hasPermi('wvp:device:list')")
    @GetMapping("/listDevicePage")
    public TableDataInfo listDevicePage(DeviceReqVo deviceReqVo){
        List<DeviceOnvif> list;
        if (shouldFilterByUserChannel()) {
            List<String> deviceIds = getUserAssignedDeviceIds();
            if (deviceIds.isEmpty()) {
                list = Collections.emptyList();
            } else {
                startPage();
                list = deviceOnvifService.selectAllDeviceListFiltered(deviceReqVo, deviceIds);
            }
        } else {
            startPage();
            list = deviceOnvifService.selectAllDeviceList(deviceReqVo);
        }
        return getDataTable(list);
    }

    // ==================== 通道查询 ====================

    //根据设备查下通道（含分配通道过滤）
    @PreAuthorize("@ss.hasPermi('wvp:device:list')")
    @GetMapping("/listDeviceChannel")
    public AjaxResult listDeviceChannel(DeviceChannelReqVo deviceChannelReqVo){
        if (shouldFilterByUserChannel()) {
            List<String> deviceIds = getUserAssignedDeviceIds();
            if (deviceIds.isEmpty()) {
                return success(Collections.emptyList());
            }
            return success(deviceOnvifChannelService.selectDeviceChannelListFiltered(deviceChannelReqVo, deviceIds));
        }
        return success(deviceOnvifChannelService.selectDeviceChannelList(deviceChannelReqVo));
    }


    //通道带分页（含分配通道过滤）
    @PreAuthorize("@ss.hasPermi('wvp:device:list')")
    @GetMapping("/listDeviceChannelPage")
    public TableDataInfo listDeviceChannelPage(DeviceChannelReqVo deviceChannelReqVo){
        List<DeviceOnvifChannel> list;
        if (shouldFilterByUserChannel()) {
            List<String> deviceIds = getUserAssignedDeviceIds();
            if (deviceIds.isEmpty()) {
                list = Collections.emptyList();
            } else {
                startPage();
                list = deviceOnvifChannelService.selectDeviceChannelListFiltered(deviceChannelReqVo, deviceIds);
            }
        } else {
            startPage();
            list = deviceOnvifChannelService.selectDeviceChannelList(deviceChannelReqVo);
        }
        return getDataTable(list);
    }



    /**
     * 统一查询设备详情 — 支持所有协议类型
     */
    @PreAuthorize("@ss.hasPermi('wvp:device:query')")
    @GetMapping("/{deviceId}")
    public AjaxResult getDeviceByDeviceId(@PathVariable String deviceId) {
        DeviceOnvif device = deviceOnvifService.selectDeviceOnvifByDeviceId(deviceId);
        if (device == null) {
            return error("设备不存在：" + deviceId);
        }
        return success(device);
    }

    /**
     * 统一设备添加 — 兼容 ONVIF / STREAM_PUSH / STREAM_PROXY
     */
    @PreAuthorize("@ss.hasPermi('wvp:device:add')")
    @PostMapping("/add")
    public AjaxResult addDevice(@RequestBody Map<String, Object> params) {
        String protocolType = (String) params.get("protocolType");
        if (protocolType == null || protocolType.isEmpty()) {
            return error("协议类型不能为空");
        }

        switch (protocolType.toUpperCase()) {
            case "ONVIF": {
                DeviceOnvif deviceOnvif = new DeviceOnvif();
                deviceOnvif.setName((String) params.get("name"));
                deviceOnvif.setIp((String) params.get("ip"));
                deviceOnvif.setPort(Long.parseLong(String.valueOf(params.getOrDefault("port", "80"))));
                deviceOnvif.setUserName((String) params.get("userName"));
                deviceOnvif.setPassword((String) params.get("password"));
                deviceOnvif.setOnvifAuthType((String) params.getOrDefault("onvifAuthType", "1"));
                int result = deviceOnvifService.quickAdd(deviceOnvif);
                return result > 0 ? success("添加成功") : error("添加ONVIF设备失败");
            }
            case "STREAM_PUSH": {
                StreamPush streamPush = new StreamPush();
                streamPush.setName((String) params.get("name"));
                String app = (String) params.get("app");
                if (app == null || app.isEmpty()) {
                    app = "push_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
                }
                streamPush.setApp(app);
                streamPush.setStream(app + "_01");
                streamPush.setPushing(false);
                boolean result = streamPushService.add(streamPush);
                return result ? success("添加成功") : error("添加推流设备失败");
            }
            case "STREAM_PROXY": {
                StreamProxyParam param = new StreamProxyParam();
                param.setType((String) params.getOrDefault("type", "default"));
                param.setName((String) params.get("name"));
                param.setUrl((String) params.get("url"));
                param.setTimeoutMs(Integer.parseInt(String.valueOf(params.getOrDefault("timeoutMs", "15"))));
                param.setRtpType((String) params.getOrDefault("rtpType", "0"));
                param.setEnable(Boolean.TRUE.equals(params.get("enable")) || params.get("enable") == null);
                param.setEnableAudio(Boolean.TRUE.equals(params.get("enableAudio")));
                param.setEnableMp4(Boolean.TRUE.equals(params.get("enableMp4")));
                param.setEnableRemoveNoneReader(Boolean.TRUE.equals(params.get("enableRemoveNoneReader")));
                param.setEnableDisableNoneReader(Boolean.TRUE.equals(params.get("enableDisableNoneReader")));
                if (params.get("mediaServerId") != null) {
                    param.setMediaServerId((String) params.get("mediaServerId"));
                }
                streamProxyService.save(param);
                return success("添加成功");
            }
            default:
                return error("不支持的协议类型：" + protocolType);
        }
    }

    /**
     * 统一设备更新 — 支持所有协议类型
     * 当修改国标设备的订阅配置时，自动触发 SIP 订阅的开启/关闭
     */
    @PreAuthorize("@ss.hasPermi('wvp:device:edit')")
    @PostMapping("/update")
    public AjaxResult updateDevice(@RequestBody DeviceOnvif device) {
        if (device.getId() == null) {
            return error("设备ID不能为空");
        }

        // 保存更新前的国标设备状态，用于比对订阅变更
        Device oldGbDevice = null;
        Long deviceId = device.getId();
        Device dbDevice = deviceService.getDevice(deviceId.intValue());
        if (dbDevice != null && "GB28181".equals(dbDevice.getProtocolType())) {
            oldGbDevice = deviceService.getDevice(deviceId.intValue());
        }

        deviceOnvifService.updateDeviceOnvif(device);

        // 根据 DB 中实际协议类型分发特殊处理
        if (dbDevice != null) {
            if ("GB28181".equals(dbDevice.getProtocolType())) {
                // 重新读取更新后的设备信息同步到 Redis
                Device updatedDevice = deviceService.getDevice(deviceId.intValue());
                redisCatchStorage.updateDevice(updatedDevice);

                // 同步订阅状态变更：对比更新前后的 subscribeCycle 值
                if (oldGbDevice != null) {
                    syncSubscriptionChanges(device, oldGbDevice, deviceId);
                }
            }
            if ("STREAM_PUSH".equals(dbDevice.getProtocolType()) && device.getName() != null) {
                StreamPush sp = new StreamPush();
                sp.setId(dbDevice.getId());
                sp.setName(device.getName());
                streamPushService.update(sp);
            }
        }
        return success();
    }

    /**
     * 对比更新前后的订阅配置，变化时触发实际的 SIP 订阅开关
     */
    private void syncSubscriptionChanges(DeviceOnvif newDevice, Device oldGbDevice, Long deviceId) {
        Boolean newCatalogCycle = newDevice.getSubscribeCycleForCatalog();
        Boolean newAlarmCycle = newDevice.getSubscribeCycleForAlarm();
        Boolean newMobilePosCycle = newDevice.getSubscribeCycleForMobilePosition();
        Integer newMobilePosInterval = newDevice.getMobilePositionSubmissionInterval();

        // 目录订阅：Boolean(true=开启, false=关闭)
        if (newCatalogCycle != null) {
            int cycle = newCatalogCycle ? 1 : 0;
            if (cycle != oldGbDevice.getSubscribeCycleForCatalog()) {
                log.info("[统一更新] 目录订阅变更: deviceId={}, {} -> {}", deviceId,
                        oldGbDevice.getSubscribeCycleForCatalog(), cycle);
                deviceService.subscribeCatalog(deviceId.intValue(), cycle);
            }
        }

        // 移动位置订阅
        if (newMobilePosCycle != null) {
            int cycle = newMobilePosCycle ? 1 : 0;
            if (cycle != oldGbDevice.getSubscribeCycleForMobilePosition()) {
                int interval = newMobilePosInterval != null ? newMobilePosInterval : oldGbDevice.getMobilePositionSubmissionInterval();
                log.info("[统一更新] 移动位置订阅变更: deviceId={}, {} -> {}", deviceId,
                        oldGbDevice.getSubscribeCycleForMobilePosition(), cycle);
                deviceService.subscribeMobilePosition(deviceId.intValue(), cycle, interval);
            }
        }

        // 报警订阅
        if (newAlarmCycle != null) {
            int cycle = newAlarmCycle ? 1 : 0;
            if (cycle != oldGbDevice.getSubscribeCycleForAlarm()) {
                log.info("[统一更新] 报警订阅变更: deviceId={}, {} -> {}", deviceId,
                        oldGbDevice.getSubscribeCycleForAlarm(), cycle);
                deviceService.subscribeAlarm(deviceId.intValue(), cycle);
            }
        }
    }


    /**
     * 统一通道刷新 — 兼容国标和ONVIF设备，其他协议直接返回成功不处理
     * @param deviceId     设备标识（GB为20位国标编码，ONVIF为wvp_device.device_id）
     */
    @PreAuthorize("@ss.hasPermi('wvp:device:sync')")
    @PostMapping("/refreshChannels/{deviceId}")
    public AjaxResult refreshChannels( @PathVariable String deviceId) {
        DeviceOnvif device = deviceOnvifService.selectDeviceOnvifByDeviceId(deviceId);
        if (device == null) {
            return AjaxResult.success();
        }
        String protocolType = device.getProtocolType();
        if ("GB28181".equals(protocolType)) {
            Device gbGevice = deviceService.getDeviceByDeviceId(deviceId);
            deviceService.sync(gbGevice);
            return new AjaxResult(0, "开始同步");
        } else if ("ONVIF".equals(protocolType)) {
            deviceOnvifService.refreshChannels(device.getId());
            return success("刷新成功");
        } else {
            return success("");
        }
    }


    /**
     * 获取通道信息
     * @param id 通道的数据库自增Id
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:channel:query')")
    @GetMapping(value = "/channel/{id}")
    public AjaxResult getChannelById(@PathVariable Long id) {
        return success(deviceOnvifChannelService.selectDeviceOnvifChannelById(id));
    }


    /**
     * 编辑通道信息
     * @param channel
     * @return
     */
    @PreAuthorize("@ss.hasPermi('wvp:channel:edit')")
    @PostMapping(value = "/channel/edit")
    public AjaxResult editChannel(@RequestBody DeviceOnvifChannel channel) {
        return success(deviceOnvifChannelService.updateDeviceOnvifChannel(channel));
    }


    /**
     * 统一设备删除 — 支持 GB28181 / ONVIF / STREAM_PUSH / STREAM_PROXY
     * @param deviceId 设备标识（GB为国标编码，ONVIF为设备ID，推流/拉流为app）
     */
    @PreAuthorize("@ss.hasPermi('wvp:device:remove')")
    @DeleteMapping("/delete/{deviceId}")
    public AjaxResult deleteDevice(@PathVariable String deviceId) {
        DeviceOnvif device = deviceOnvifService.selectDeviceOnvifByDeviceId(deviceId);
        if (device == null) {
            return error("设备不存在：" + deviceId);
        }

        String protocolType = device.getProtocolType();

        if ("GB28181".equals(protocolType)) {
            // 国标设备：删除通道 → 删除设备 → 清除Redis → 停止订阅任务
            boolean isSuccess = deviceService.delete(deviceId);
            if (!isSuccess) {
                return error("删除国标设备失败");
            }
            inviteStreamService.clearInviteInfo(deviceId);
            Set<String> allKeys = dynamicTask.getAllKeys();
            for (String key : allKeys) {
                if (key.startsWith(deviceId)) {
                    Runnable runnable = dynamicTask.get(key);
                    if (runnable instanceof ISubscribeTask) {
                        ISubscribeTask subscribeTask = (ISubscribeTask) runnable;
                        subscribeTask.stop(null);
                    }
                    dynamicTask.stop(key);
                }
            }
            return success();
        } else if ("ONVIF".equals(protocolType)) {
            // ONVIF 设备：先停止正在拉流的媒体流 → 删通道 → 删设备
            String mediaServerId = device.getMediaServerId();
            if (mediaServerId != null) {
                MediaServer mediaServer = mediaServerService.getOne(mediaServerId);
                if (mediaServer != null) {
                    List<StreamInfo> streams = mediaServerService.getMediaList(mediaServer, device.getDeviceId(), null, null);
                    if (streams != null) {
                        for (StreamInfo s : streams) {
                            mediaServerService.closeStreams(mediaServer, s.getApp(), s.getStream());
                        }
                    }
                }
            }
            deviceOnvifChannelService.deleteDeviceOnvifChannelByDataDeviceId(device.getId());
            deviceOnvifService.deleteDeviceOnvifById(device.getId());
            return success();
        } else if ("STREAM_PUSH".equals(protocolType)) {
            // 推流设备：关闭流 → 删通道 → 删设备
            streamPushService.delete(device.getId().intValue());
            return success();
        } else if ("STREAM_PROXY".equals(protocolType)) {
            // 拉流代理：停止拉流 → 删通道 → 删设备
            streamProxyService.delete(device.getId().intValue());
            return success();
        } else {
            return error("不支持的协议类型：" + protocolType);
        }
    }


    @PreAuthorize("@ss.hasPermi('wvp:device:query')")
    @GetMapping("/wsDiscovery")
    List<OnvifUtils.DiscoveryInfo> wsDiscovery(){
      return deviceOnvifService.wsDiscovery();
    }


}
