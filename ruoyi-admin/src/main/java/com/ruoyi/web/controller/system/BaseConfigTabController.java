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
import com.ruoyi.system.domain.BaseConfigTab;
import com.ruoyi.system.service.IBaseConfigTabService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 基础配置分类Controller
 *
 * @author ruoyi
 * @date 2026-04-26
 */
@RestController
@RequestMapping("/system/baseconfigtab")
public class BaseConfigTabController extends BaseController
{
    @Autowired
    private IBaseConfigTabService baseConfigTabService;

    /**
     * 查询基础配置分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(BaseConfigTab baseConfigTab)
    {
        startPage();
        List<BaseConfigTab> list = baseConfigTabService.selectBaseConfigTabList(baseConfigTab);
        return getDataTable(list);
    }

    /**
     * 导出基础配置分类列表
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:export')")
    @Log(title = "基础配置分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BaseConfigTab baseConfigTab)
    {
        List<BaseConfigTab> list = baseConfigTabService.selectBaseConfigTabList(baseConfigTab);
        ExcelUtil<BaseConfigTab> util = new ExcelUtil<BaseConfigTab>(BaseConfigTab.class);
        util.exportExcel(response, list, "基础配置分类数据");
    }

    /**
     * 获取基础配置分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(baseConfigTabService.selectBaseConfigTabById(id));
    }

    /**
     * 新增基础配置分类
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:add')")
    @Log(title = "基础配置分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BaseConfigTab baseConfigTab)
    {
        return toAjax(baseConfigTabService.insertBaseConfigTab(baseConfigTab));
    }

    /**
     * 修改基础配置分类
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:edit')")
    @Log(title = "基础配置分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BaseConfigTab baseConfigTab)
    {
        return toAjax(baseConfigTabService.updateBaseConfigTab(baseConfigTab));
    }

    /**
     * 删除基础配置分类
     */
    @PreAuthorize("@ss.hasPermi('system:baseconfig:remove')")
    @Log(title = "基础配置分类", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(baseConfigTabService.deleteBaseConfigTabByIds(ids));
    }
}
