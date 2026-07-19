package com.ruoyi.web.controller.large;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysLargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 大屏数据接口
 *
 * @FileName SysLargeController
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
@RestController
@RequestMapping("/api/large")
public class SysLargeController extends BaseController {

    @Autowired
    private ISysLargeService sysLargeService;

    /**
     * 获取总设备数量
     *
     * @return
     */
    @Anonymous
    @GetMapping("/countDeviceNum")
    public AjaxResult countDeviceNum(){
        return success(sysLargeService.countDeviceNum());
    }

    /**
     * 获取国标总览
     *
     * @return
     */
    @Anonymous
    @GetMapping("/countGbNum")
    public AjaxResult countGbNum() {
        return success(sysLargeService.countGbNum());
    }

    /**
     * 获取国标设备提醒
     *
     * @return
     */
    @Anonymous
    @GetMapping("/leftGbDevice")
    public AjaxResult leftGbDevice() {
        return success(sysLargeService.leftGbDevice());
    }
}
