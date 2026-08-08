package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.LogoConfigDTO;
import com.ruoyi.system.domain.MapConfigDTO;
import com.ruoyi.system.domain.SysConfig;
import com.ruoyi.system.service.ISysConfigService;

/**
 * 参数配置 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController
{
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config)
    {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @Log(title = "参数管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysConfig config)
    {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        util.exportExcel(response, list, "参数数据");
    }

    /**
     * 根据参数编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public AjaxResult getInfo(@PathVariable Long configId)
    {
        return success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @Anonymous
    @GetMapping(value = "/configKey/{configKey}")
    public AjaxResult getConfigKey(@PathVariable String configKey)
    {
        return success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(getUsername());
        return toAjax(configService.insertConfig(config));
    }

    /**
     * 修改参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysConfig config)
    {
        if (!configService.checkConfigKeyUnique(config))
        {
            return error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(getUsername());
        return toAjax(configService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public AjaxResult remove(@PathVariable Long[] configIds)
    {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    /**
     * 刷新参数缓存
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public AjaxResult refreshCache()
    {
        configService.resetConfigCache();
        return success();
    }

    /**
     * 获取地图配置
     */
    @Anonymous
    @GetMapping("/mapConfig")
    public AjaxResult getMapConfig()
    {
        MapConfigDTO dto = new MapConfigDTO();
        dto.setMapType(configService.selectConfigByKey("map_type"));
        // 高德
        dto.setGaodeMapKey(configService.selectConfigByKey("gaode_map_key"));
        dto.setGaodeMapSecurityCode(configService.selectConfigByKey("gaode_map_security_code"));
        dto.setGaodeMapCenter(configService.selectConfigByKey("gaode_map_center"));
        // 天地图
        dto.setTiandituMapKey(configService.selectConfigByKey("tianditu_map_key"));
        dto.setTiandituMapSecurityCode(configService.selectConfigByKey("tianditu_map_security_code"));
        dto.setTiandituMapCenter(configService.selectConfigByKey("tianditu_map_center"));
        return success(dto);
    }

    /**
     * 保存地图配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "地图配置", businessType = BusinessType.UPDATE)
    @PutMapping("/mapConfig")
    public AjaxResult saveMapConfig(@RequestBody MapConfigDTO dto)
    {
        saveOrUpdateConfig("map_type", "地图类型", dto.getMapType() != null ? dto.getMapType() : "amap");
        // 高德
        saveOrUpdateConfig("gaode_map_key", "高德地图KEY", dto.getGaodeMapKey());
        saveOrUpdateConfig("gaode_map_security_code", "高德地图安全秘钥", dto.getGaodeMapSecurityCode());
        saveOrUpdateConfig("gaode_map_center", "高德地图中心点坐标", dto.getGaodeMapCenter());
        // 天地图
        saveOrUpdateConfig("tianditu_map_key", "天地图KEY", dto.getTiandituMapKey());
        saveOrUpdateConfig("tianditu_map_security_code", "天地图安全秘钥", dto.getTiandituMapSecurityCode());
        saveOrUpdateConfig("tianditu_map_center", "天地图中心点坐标", dto.getTiandituMapCenter());
        configService.resetConfigCache();
        return success();
    }

    /**
     * 保存或更新单个配置项
     */
    private void saveOrUpdateConfig(String key, String name, String value)
    {
        SysConfig config = configService.selectConfigObjByKey(key);
        if (config != null)
        {
            // 更新已有配置
            config.setConfigValue(value);
            config.setUpdateBy(getUsername());
            configService.updateConfig(config);
        }
        else
        {
            // 不存在则新增
            config = new SysConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            config.setConfigName(name);
            config.setConfigType("Y");
            config.setCreateBy(getUsername());
            configService.insertConfig(config);
        }
    }

    // ========== Logo 配置 ==========

    /**
     * 获取Logo配置（无需登录，供登录页等公开页面使用）
     */
    @Anonymous
    @GetMapping("/logo/public")
    public AjaxResult getPublicLogoConfig() {
        return buildLogoConfig();
    }

    /**
     * 获取Logo配置（管理后台，需登录）
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("/logo")
    public AjaxResult getLogoConfig() {
        return buildLogoConfig();
    }

    /**
     * 保存Logo配置（存在则更新，不存在则新增）
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "Logo配置", businessType = BusinessType.UPDATE)
    @PutMapping("/logo")
    public AjaxResult saveLogoConfig(@RequestBody LogoConfigDTO dto) {
        saveOrUpdateConfig(LogoConfigDTO.KEY_SITE_LOGO_LOGIN,   "后台登录页Logo", dto.getSiteLogoLogin());
        saveOrUpdateConfig(LogoConfigDTO.KEY_SITE_LOGO_SQUARE,  "后台小Logo",     dto.getSiteLogoSquare());
        saveOrUpdateConfig(LogoConfigDTO.KEY_SITE_LOGO_LEFTTOP, "后台大Logo",     dto.getSiteLogoLefttop());
        saveOrUpdateConfig(LogoConfigDTO.KEY_MOBILE_LOGIN_LOGO, "移动端登录Logo", dto.getMobileLoginLogo());
        saveOrUpdateConfig(LogoConfigDTO.KEY_SITE_FAVICON,      "系统ICO图标",    dto.getSiteFavicon());
        configService.resetConfigCache();
        return success();
    }

    private AjaxResult buildLogoConfig() {
        LogoConfigDTO dto = new LogoConfigDTO();
        dto.setSiteLogoLogin(emptyToNull(configService.selectConfigByKey(LogoConfigDTO.KEY_SITE_LOGO_LOGIN)));
        dto.setSiteLogoSquare(emptyToNull(configService.selectConfigByKey(LogoConfigDTO.KEY_SITE_LOGO_SQUARE)));
        dto.setSiteLogoLefttop(emptyToNull(configService.selectConfigByKey(LogoConfigDTO.KEY_SITE_LOGO_LEFTTOP)));
        dto.setMobileLoginLogo(emptyToNull(configService.selectConfigByKey(LogoConfigDTO.KEY_MOBILE_LOGIN_LOGO)));
        dto.setSiteFavicon(emptyToNull(configService.selectConfigByKey(LogoConfigDTO.KEY_SITE_FAVICON)));
        return success(dto);
    }

    private String emptyToNull(String val) {
        return StringUtils.isEmpty(val) ? null : val;
    }
}
