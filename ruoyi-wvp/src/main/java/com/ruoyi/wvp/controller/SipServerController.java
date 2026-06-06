package com.ruoyi.wvp.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.gb28181.SipLayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SIP服务管理控制器
 *
 * @author ruoyi-wvp
 */
@Slf4j
@RestController
@RequestMapping("/api/sip/server")
public class SipServerController extends BaseController {

    @Autowired
    private SipLayer sipLayer;

    @Autowired
    private SipConfig sipConfig;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 获取SIP服务状态
     */
    @PreAuthorize("@ss.hasPermi('wvp:sip:server:query')")
    @GetMapping("/status")
    public AjaxResult status() {
        Map<String, Object> status = new HashMap<>();
        status.put("running", sipLayer.isRunning());
        status.put("monitorIps", sipLayer.getMonitorIps());
        status.put("port", sipConfig.getPort());
        status.put("domain", sipConfig.getDomain());
        status.put("serverId", sipConfig.getId());
        return success(status);
    }

    /**
     * 启动SIP服务
     */
    @Log(title = "SIP服务管理", businessType = BusinessType.OTHER)
    @PreAuthorize("@ss.hasPermi('wvp:sip:server:control')")
    @PostMapping("/start")
    public AjaxResult start() {
        if (sipLayer.isRunning()) {
            return error("SIP服务已经在运行中");
        }
        try {
            sipLayer.run();
            return success("SIP服务启动指令已执行");
        } catch (Exception e) {
            log.error("启动SIP服务失败", e);
            return error("启动失败：" + e.getMessage());
        }
    }

    /**
     * 停止SIP服务
     */
    @Log(title = "SIP服务管理", businessType = BusinessType.OTHER)
    @PreAuthorize("@ss.hasPermi('wvp:sip:server:control')")
    @PostMapping("/stop")
    public AjaxResult stop() {
        if (!sipLayer.isRunning()) {
            return error("SIP服务未运行");
        }
        try {
            sipLayer.stop();
            return success("SIP服务已停止");
        } catch (Exception e) {
            log.error("停止SIP服务失败", e);
            return error("停止失败：" + e.getMessage());
        }
    }

    /**
     * 重启SIP服务
     */
    @Log(title = "SIP服务管理", businessType = BusinessType.OTHER)
    @PreAuthorize("@ss.hasPermi('wvp:sip:server:control')")
    @PostMapping("/restart")
    public AjaxResult restart() {
        taskExecutor.execute(() -> {
            try {
                // 延迟3秒执行，确保HTTP响应先返回
                Thread.sleep(3000);
                sipLayer.restart();
                log.info("[SIP服务] 重启完成");
            } catch (Exception e) {
                log.error("[SIP服务] 重启失败", e);
            }
        });
        return success("SIP服务重启指令已发送，请稍后刷新查看状态");
    }
}
