package com.ruoyi.wvp.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wvp.conf.SipConfig;
import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;
import com.ruoyi.wvp.service.ISipConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SIP配置管理控制器
 *
 * @author ruoyi-wvp
 */
@Slf4j
@RestController
@RequestMapping("/api/sip/config")
public class SipConfigController extends BaseController {

    @Autowired
    private ISipConfigService sipConfigService;

    @Autowired
    private SipConfig sipConfig;

    /**
     * 获取SIP配置
     */
    @GetMapping
    public AjaxResult getConfig() {
        SipConfigEntity config = sipConfigService.getSipConfig();
        if (config == null) {
            // 数据库没有配置，返回application.yml中的配置
            config = new SipConfigEntity();
            config.setIp(sipConfig.getIp());
            config.setShowIp(sipConfig.getShowIp());
            config.setPort(sipConfig.getPort());
            config.setGbDomain(sipConfig.getDomain());
            config.setServerId(sipConfig.getId());
            config.setGbPwd(sipConfig.getPassword());
            config.setPtzSpeed(sipConfig.getPtzSpeed());
            config.setRegisterTimeInterval(sipConfig.getRegisterTimeInterval());
            config.setAlarm(sipConfig.isAlarm());
            config.setTimeout(sipConfig.getTimeout());
        }
        return success(config);
    }

    /**
     * 保存或更新SIP配置
     */
    @Log(title = "SIP配置管理", businessType = BusinessType.UPDATE)
    @PostMapping
    public AjaxResult saveOrUpdate(@RequestBody SipConfigEntity config) {
        // 参数校验
        if (config.getPort() == null) {
            return error("SIP端口不能为空");
        }
        if (config.getGbDomain() == null || config.getGbDomain().isEmpty()) {
            return error("国标域不能为空");
        }
        if (config.getServerId() == null || config.getServerId().isEmpty()) {
            return error("国标ID不能为空");
        }

        boolean result;
        SipConfigEntity existingConfig = sipConfigService.getSipConfig();
        if (existingConfig != null) {
            // 更新
            config.setId(existingConfig.getId());
            result = sipConfigService.update(config);
            if (result) {
                return success("SIP配置更新成功，重启SIP服务后生效");
            }
        } else {
            // 新增
            result = sipConfigService.save(config);
            if (result) {
                return success("SIP配置保存成功，重启SIP服务后生效");
            }
        }
        return error("SIP配置保存失败");
    }

    /**
     * 获取SIP配置状态（是否已从数据库加载）
     */
    @PreAuthorize("@ss.hasPermi('wvp:sip:config:query')")
    @GetMapping("/status")
    public AjaxResult getStatus() {
        Map<String, Object> status = new HashMap<>();
        SipConfigEntity dbConfig = sipConfigService.getSipConfig();
        status.put("hasDbConfig", dbConfig != null);
        status.put("currentIp", sipConfig.getIp());
        status.put("currentPort", sipConfig.getPort());
        status.put("currentDomain", sipConfig.getDomain());
        status.put("currentServerId", sipConfig.getId());
        return success(status);
    }
}
