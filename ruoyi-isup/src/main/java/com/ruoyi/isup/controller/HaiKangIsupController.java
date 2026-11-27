package com.ruoyi.isup.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.RtpServerParam;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.isup.domain.HaikangIsupRecordDownloadRequest;
import com.ruoyi.isup.service.IDeviceIsupService;
import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 海康 ISUP 设备控制器
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/isup/device")
@RequiredArgsConstructor
public class HaiKangIsupController extends BaseController {

    private final IDeviceIsupService deviceIsupService;

    /**
     * 获取已注册设备列表（开源版未启用 ISUP 原生 SDK，恒为空）
     */
    @GetMapping("/list")
    public AjaxResult deviceList() {
        return success(new HashMap<String, Object>());
    }

    /**
     * 查询 ISUP 设备列表（来自数据库）
     */
    @PreAuthorize("@ss.hasPermi('isup:device:list')")
    @GetMapping("/dbList")
    public TableDataInfo dbList(DeviceOnvif device) {
        startPage();
        List<DeviceOnvif> list = deviceIsupService.selectIsupDeviceList(device);
        return getDataTable(list);
    }

    /**
     * 新增 ISUP 设备
     */
    @PreAuthorize("@ss.hasPermi('isup:device:add')")
    @PostMapping
    public AjaxResult add(@RequestBody DeviceOnvif device) {
        return toAjax(deviceIsupService.insertIsupDevice(device));
    }

    /**
     * 修改 ISUP 设备
     */
    @PreAuthorize("@ss.hasPermi('isup:device:edit')")
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceOnvif device) {
        return toAjax(deviceIsupService.updateIsupDevice(device));
    }

    /**
     * 删除 ISUP 设备
     */
    @PreAuthorize("@ss.hasPermi('isup:device:remove')")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        deviceIsupService.deleteIsupDeviceByIds(ids);
        return success();
    }

    /**
     * 获取 ISUP 设备详情
     */
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(deviceIsupService.getHaiKangIsupDeviceDetail(id));
    }

    /**
     * 同步设备通道
     */
    @PreAuthorize("@ss.hasPermi('isup:device:edit')")
    @GetMapping("/syncChannels/{id}")
    public AjaxResult syncChannels(@PathVariable Long id) {
        DeviceOnvif device = deviceIsupService.selectIsupDeviceById(id);
        if (device == null) {
            return error("设备不存在");
        }
        deviceIsupService.syncChannels(device);
        return success();
    }

    /**
     * 从设备拉取设备信息（名称/型号/固件），需设备在线
     */
    @PreAuthorize("@ss.hasPermi('isup:device:edit')")
    @GetMapping("/syncDeviceInfo/{id}")
    public AjaxResult syncDeviceInfo(@PathVariable Long id) {
        return deviceIsupService.syncDeviceInfoFromDevice(id)
                ? success("设备信息已同步")
                : error("设备未注册或信息查询失败，请确认设备在线后重试");
    }

    /**
     * 查询设备通道列表
     */
    @PreAuthorize("@ss.hasPermi('isup:channel:list')")
    @GetMapping("/channelList/{deviceId}")
    public TableDataInfo channelList(@PathVariable Long deviceId) {
        startPage();
        List<DeviceOnvifChannel> list = deviceIsupService.selectIsupChannelList(deviceId);
        return getDataTable(list);
    }

    /**
     * 重启设备
     */
    @PreAuthorize("@ss.hasPermi('isup:device:edit')")
    @GetMapping("/reboot/{deviceId}")
    public AjaxResult reboot(@PathVariable Long deviceId) {
        deviceIsupService.restartDevice(deviceId);
        return success();
    }

    /**
     * 获取设备时间
     */
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/getDevTime/{deviceId}")
    public AjaxResult getDevTime(@PathVariable Long deviceId) {
        return success(deviceIsupService.getDevTime(deviceId));
    }

    /**
     * 设置设备时间
     */
    @PreAuthorize("@ss.hasPermi('isup:device:edit')")
    @GetMapping("/setDevTime/{deviceId}")
    public AjaxResult setDevTime(@PathVariable Long deviceId, String time) {
        deviceIsupService.setDevTime(deviceId, time);
        return success();
    }

    /**
     * 开始云台控制
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/startPtz/{deviceId}/{channelId}")
    public AjaxResult startPtz(@PathVariable Long deviceId, @PathVariable Integer channelId,
                               @RequestParam int ptzCmd, @RequestParam(defaultValue = "4") int speed) {
        deviceIsupService.startPtz(deviceId, channelId, ptzCmd, speed);
        return success();
    }

    /**
     * 结束云台控制
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/endPtz/{deviceId}/{channelId}")
    public AjaxResult endPtz(@PathVariable Long deviceId, @PathVariable Integer channelId,
                             @RequestParam int ptzCmd, @RequestParam(defaultValue = "4") int speed) {
        deviceIsupService.endPtz(deviceId, channelId, ptzCmd, speed);
        return success();
    }

    /**
     * 设置预置点
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/setPreset/{deviceId}/{channelId}/{presetIndex}")
    public AjaxResult setPreset(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                @PathVariable int presetIndex) {
        deviceIsupService.setPreset(deviceId, channelId, presetIndex);
        return success();
    }

    /**
     * 清除预置点
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/clearPreset/{deviceId}/{channelId}/{presetIndex}")
    public AjaxResult clearPreset(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                  @PathVariable int presetIndex) {
        deviceIsupService.clearPreset(deviceId, channelId, presetIndex);
        return success();
    }

    /**
     * 调用预置点
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/gotoPreset/{deviceId}/{channelId}/{presetIndex}")
    public AjaxResult gotoPreset(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                 @PathVariable int presetIndex) {
        deviceIsupService.gotoPreset(deviceId, channelId, presetIndex);
        return success();
    }

    /**
     * 获取预置点列表
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/getPresetList/{deviceId}/{channelId}")
    public AjaxResult getPresetList(@PathVariable Long deviceId, @PathVariable Integer channelId) {
        return success(deviceIsupService.getPresetList(deviceId, channelId));
    }

    /**
     * 查询录像
     */
    @PreAuthorize("@ss.hasPermi('isup:device:record')")
    @GetMapping("/getRecMonth/{deviceId}/{channelId}")
    public AjaxResult getRecMonth(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                  @RequestParam String startTime, @RequestParam String endTime) {
        ArrayList<HashMap<String, Object>> list = deviceIsupService.queryRecord(deviceId, channelId, startTime, endTime);
        return success(list);
    }

    /**
     * 获取设备基础信息
     */
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/getDeviceInfo/{deviceId}")
    public AjaxResult getDeviceInfo(@PathVariable Long deviceId) {
        return success(deviceIsupService.getHaiKangIsupDeviceInfo(deviceId));
    }

    /**
     * 获取存储信息
     */
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/getStorageInfo/{deviceId}")
    public AjaxResult getStorageInfo(@PathVariable Long deviceId) {
        return success(deviceIsupService.getHaiKangIsupStorageInfo(deviceId));
    }

    /**
     * 获取系统状态
     */
    @PreAuthorize("@ss.hasPermi('isup:device:query')")
    @GetMapping("/getSystemStatus/{deviceId}")
    public AjaxResult getSystemStatus(@PathVariable Long deviceId) {
        return success(deviceIsupService.getHaiKangIsupSystemStatus(deviceId));
    }

    /**
     * 开始预览
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:play')")
    @PostMapping("/startPlay")
    public AjaxResult startPlay(@RequestBody RtpServerParam rtpServerParam) {
        deviceIsupService.startPlay(rtpServerParam);
        return success();
    }

    /**
     * 停止预览
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:play')")
    @GetMapping("/stopPlay/{id}/{channel}")
    public AjaxResult stopPlay(@PathVariable Long id, @PathVariable Integer channel) {
        deviceIsupService.stopPlay(id, channel);
        return success();
    }

    /**
     * 开始回放
     */
    @PreAuthorize("@ss.hasPermi('isup:device:playback')")
    @PostMapping("/startPlayback")
    public AjaxResult startPlayback(@RequestBody RtpServerParam rtpServerParam) {
        deviceIsupService.startPlayback(rtpServerParam);
        return success();
    }

    /**
     * 停止回放
     */
    @PreAuthorize("@ss.hasPermi('isup:device:playback')")
    @GetMapping("/stopPlayback/{id}/{channel}")
    public AjaxResult stopPlayback(@PathVariable Long id, @PathVariable Integer channel) {
        deviceIsupService.stopPlayback(id, channel);
        return success();
    }

    /**
     * 获取播放地址（前端轮询：预览/回放地址由 ZLMediaKit hook 异步生成）
     * @param type preview-预览，playback-回放
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:play')")
    @GetMapping("/getPlayUrl/{deviceId}/{channel}")
    public AjaxResult getPlayUrl(@PathVariable Long deviceId, @PathVariable Integer channel,
                                 @RequestParam(defaultValue = "preview") String type) {
        return success(deviceIsupService.getPlayUrl(deviceId, channel, type));
    }

    /**
     * 辅助设备控制（灯光/雨刮/风扇/加热器等）
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/cameraAuxControl/{deviceId}/{channelId}")
    public AjaxResult cameraAuxControl(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                       @RequestParam String operation,
                                       @RequestParam(defaultValue = "true") boolean isStart) {
        deviceIsupService.cameraAuxControl(deviceId, channelId, operation, isStart);
        return success();
    }

    /**
     * 巡航控制（加入/删除巡航点、设置停顿/速度、开始/停止巡航、自动扫描）
     */
    @Deprecated
    @PreAuthorize("@ss.hasPermi('isup:device:ptz')")
    @GetMapping("/cruiseControl/{deviceId}/{channelId}")
    public AjaxResult cruiseControl(@PathVariable Long deviceId, @PathVariable Integer channelId,
                                    @RequestParam String operation,
                                    @RequestParam(required = false) Integer param) {
        deviceIsupService.cruiseControl(deviceId, channelId, operation, param);
        return success();
    }

    /**
     * 抓图并保存
     */
    @PreAuthorize("@ss.hasPermi('isup:device:capture')")
    @PostMapping("/captureAndSave/{deviceId}/{channelId}")
    public AjaxResult captureAndSave(@PathVariable Long deviceId, @PathVariable Integer channelId, String snapshotType) throws Exception {
        if (snapshotType == null || snapshotType.isEmpty()) {
            snapshotType = "manual";
        }
        return success(deviceIsupService.captureAndSave(deviceId, channelId, snapshotType));
    }

    /**
     * 录像下载
     */
    @PreAuthorize("@ss.hasPermi('isup:device:download')")
    @PostMapping("/downloadRecord")
    public AjaxResult downloadRecord(@RequestBody HaikangIsupRecordDownloadRequest request) {
        return success(deviceIsupService.downloadRecord(request));
    }

    /**
     * 录像直接下载
     */
    @PreAuthorize("@ss.hasPermi('isup:device:download')")
    @PostMapping("/downloadRecordDirect")
    public ResponseEntity<Resource> downloadRecordDirect(@RequestBody HaikangIsupRecordDownloadRequest request) throws Exception {
        File file = deviceIsupService.downloadRecordFile(request);
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }
}
