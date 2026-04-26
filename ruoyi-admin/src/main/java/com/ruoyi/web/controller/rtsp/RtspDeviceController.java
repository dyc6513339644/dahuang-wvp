package com.ruoyi.web.controller.rtsp;

import ai.onnxruntime.OrtException;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.rtsp.domain.RtspDevice;
import com.ruoyi.rtsp.domain.bo.RtspDeviceBo;
import com.ruoyi.rtsp.domain.bo.alarmClockBo;
import com.ruoyi.rtsp.service.IRtspDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * rtsp设备Controller
 *
 * @author 陈江灿
 * @date 2025-04-22
 */
@RestController
@RequestMapping("/rtsp/RtspDevice")
public class RtspDeviceController extends BaseController {

    @Autowired
    private IRtspDeviceService rtspDeviceService;

    /**
     * 历史播放
     *
     * @param bo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:AlarmClock')")
    @GetMapping("/alarmClock")
    public AjaxResult alarmClock(alarmClockBo bo) {
        return success(rtspDeviceService.alarmClock(bo));
    }


    /**
     * 查询rtsp设备列表
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:list')")
    @GetMapping("/list")
    public TableDataInfo list(RtspDevice rtspDevice) {
        startPage();
        List<RtspDevice> list = rtspDeviceService.selectRtspDeviceList(rtspDevice);
        return getDataTable(list);
    }

    /**
     * 查询rtsp设备列表
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:list')")
    @GetMapping("/rtspDeviceList")
    public AjaxResult rtspDeviceList(RtspDevice rtspDevice, String mapType) {
        List<RtspDevice> list = new ArrayList<>();
        if ("map".equals(mapType)) {
            list = rtspDeviceService.selectRtspDeviceListMap(rtspDevice);
        } else {
            list = rtspDeviceService.selectRtspDeviceList(rtspDevice);
        }
        return success(list);
    }

    /**
     * 导出rtsp设备列表
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:export')")
    @Log(title = "rtsp设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RtspDevice rtspDevice) {
        List<RtspDevice> list = rtspDeviceService.selectRtspDeviceList(rtspDevice);
        ExcelUtil<RtspDevice> util = new ExcelUtil<RtspDevice>(RtspDevice.class);
        util.exportExcel(response, list, "rtsp设备数据");
    }

    /**
     * 获取rtsp设备详细信息
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(rtspDeviceService.selectRtspDeviceById(id));
    }

    /**
     * 新增rtsp设备
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:add')")
    @Log(title = "rtsp设备", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RtspDevice rtspDevice) {
        return toAjax(rtspDeviceService.insertRtspDevice(rtspDevice));
    }

    /**
     * 修改rtsp设备
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:edit')")
    @Log(title = "rtsp设备", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RtspDevice rtspDevice) {
        return toAjax(rtspDeviceService.updateRtspDevice(rtspDevice));
    }

    /**
     * 删除rtsp设备
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:remove')")
    @Log(title = "rtsp设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(rtspDeviceService.deleteRtspDeviceByIds(ids));
    }

    /**
     * 新增目标检测
     *
     * @param bo
     * @return
     */
    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:addDetection')")
    @PostMapping("/addDetection")
    public AjaxResult addDetection(@RequestBody RtspDeviceBo bo) throws IOException, OrtException {
        rtspDeviceService.insertRtspDetection(bo);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('rtsp:RtspDevice:stopDetection')")
    @PostMapping("/stopDetection")
    public AjaxResult stopDetection(@RequestBody RtspDeviceBo bo) throws IOException, OrtException {
        rtspDeviceService.stopRtspDetection(bo);
        return success();
    }


}
