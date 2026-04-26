package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BaseConfig;
import com.ruoyi.system.service.IBaseConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 基础配置Controller
 *
 * @author ruoyi
 * @date 2026-04-26
 */
@RestController
@RequestMapping("/system/baseconfig")
public class BaseConfigController extends BaseController
{
    @Autowired
    private IBaseConfigService baseConfigService;

    /**
     * 查询基础配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseConfig baseConfig)
    {
        startPage();
        List<BaseConfig> list = baseConfigService.selectBaseConfigList(baseConfig);
        return getDataTable(list);
    }

    /**
     * 查询所有基础配置（不分页）
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(BaseConfig baseConfig)
    {
        baseConfig.setConfigStatus(0);
        List<BaseConfig> list = baseConfigService.selectBaseConfigList(baseConfig);
        return success(list);
    }




    /**
     * 导出基础配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:export')")
    @Log(title = "基础配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseConfig baseConfig)
    {
        List<BaseConfig> list = baseConfigService.selectBaseConfigList(baseConfig);
        ExcelUtil<BaseConfig> util = new ExcelUtil<BaseConfig>(BaseConfig.class);
        util.exportExcel(response, list, "基础配置数据");
    }

    /**
     * 获取基础配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(baseConfigService.selectBaseConfigById(id));
    }

    /**
     * 新增基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:add')")
    @Log(title = "基础配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseConfig baseConfig)
    {
        return toAjax(baseConfigService.insertBaseConfig(baseConfig));
    }

    /**
     * 修改基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:edit')")
    @Log(title = "基础配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseConfig baseConfig)
    {
        return toAjax(baseConfigService.updateBaseConfig(baseConfig));
    }

    /**
     * 批量修改基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:edit')")
    @Log(title = "基础配置", businessType = BusinessType.UPDATE)
    @PutMapping("/batchEdit")
    public AjaxResult batchEdit(@RequestBody List<BaseConfig> baseConfigs)
    {


        if (baseConfigs == null || baseConfigs.isEmpty()) {
            return error("请选择要修改的配置数据");
        }
        return toAjax(baseConfigService.batchUpdateBaseConfig(baseConfigs));
    }

    /**
     * 删除基础配置
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:remove')")
    @Log(title = "基础配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(baseConfigService.deleteBaseConfigByIds(ids));
    }
}
