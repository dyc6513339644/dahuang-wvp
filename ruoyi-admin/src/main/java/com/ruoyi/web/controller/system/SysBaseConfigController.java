package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.config.SslConfig;
import com.ruoyi.system.domain.SysBaseConfig;
import com.ruoyi.system.service.ISysBaseConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基础配置Controller（SSL/HTTPS等系统级配置）
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/baseconfig")
public class SysBaseConfigController extends BaseController {

    @Autowired
    private ISysBaseConfigService baseConfigService;

    @Autowired
    private SslConfig sslConfig;

    /**
     * 获取基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:query')")
    @GetMapping("/get")
    public AjaxResult getConfig() {
        // 优先从DB取，如果没有则返回SslConfig中的默认值
        SysBaseConfig dbConfig = baseConfigService.getBaseConfig();
        if (dbConfig != null) {
            return success(dbConfig);
        }
        // 返回默认配置
        SysBaseConfig defaultConfig = new SysBaseConfig();
        defaultConfig.setSslEnabled(sslConfig.isEnabled() ? "1" : "0");
        defaultConfig.setSslKeyStore(sslConfig.getKeyStore());
        defaultConfig.setSslKeyStorePassword(sslConfig.getKeyStorePassword());
        defaultConfig.setSslPort(sslConfig.getPort());
        defaultConfig.setHttpPort(sslConfig.getHttpPort());
        defaultConfig.setAccessDomain(sslConfig.getAccessDomain());
        return success(defaultConfig);
    }

    /**
     * 保存/更新基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:edit')")
    @Log(title = "基础配置", businessType = BusinessType.UPDATE)
    @PutMapping("/save")
    public AjaxResult save(@RequestBody SysBaseConfig config) {
        config.setUpdateBy(getUsername());
        boolean result;
        if (baseConfigService.exists()) {
            // 更新
            SysBaseConfig existing = baseConfigService.getBaseConfig();
            config.setConfigId(existing.getConfigId());
            result = baseConfigService.update(config);
        } else {
            // 新增
            config.setCreateBy(getUsername());
            result = baseConfigService.save(config);
        }
        if (result) {
            // 重新加载SSL配置到内存
            sslConfig.reload();
            return success();
        }
        return error("保存基础配置失败");
    }

    /**
     * 检测端口是否可用
     * @param port 端口号
     * @return {available: true/false}
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:query')")
    @GetMapping("/checkPort")
    public AjaxResult checkPort(@RequestParam int port) {
        try (
            ServerSocket serverSocket = new ServerSocket(port, 1, InetAddress.getByName("0.0.0.0"))) {
            // 能正常绑定，端口空闲
            return success("端口可用");
        } catch (Exception e) {
            // 端口被占用、权限不足等
            return  error("端口已被占用");
        }
    }
}
