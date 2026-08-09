package com.ruoyi.wvp.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.wvp.gb28181.bean.SipBlackEntity;
import com.ruoyi.wvp.service.ISipBlackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SIP 黑名单管理控制器
 *
 * @author ruoyi-wvp
 */
@Slf4j
@RestController
@RequestMapping("/api/sip/black")
public class SipBlackController extends BaseController {

    @Autowired
    private ISipBlackService sipBlackService;

    /**
     * 获取黑名单列表
     */
    @GetMapping("/list")
    public AjaxResult list() {
        return success(sipBlackService.list());
    }

    /**
     * 根据ID查询黑名单
     */
    @GetMapping("/{id}")
    public AjaxResult getById(@PathVariable Long id) {
        SipBlackEntity entity = sipBlackService.getById(id);
        if (entity == null) {
            return error("黑名单记录不存在");
        }
        return success(entity);
    }

    /**
     * 新增黑名单
     */
    @Log(title = "黑名单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SipBlackEntity entity) {
        if (entity.getDeviceId() == null || entity.getDeviceId().trim().isEmpty()) {
            return error("设备ID不能为空");
        }
        entity.setDeviceId(entity.getDeviceId().trim());
        // 检查是否已存在
        if (sipBlackService.getByDeviceId(entity.getDeviceId()) != null) {
            return error("设备ID [" + entity.getDeviceId() + "] 已在黑名单中");
        }
        boolean result = sipBlackService.add(entity);
        return result ? success("添加成功") : error("添加失败");
    }

    /**
     * 更新黑名单
     */
    @Log(title = "黑名单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult update(@RequestBody SipBlackEntity entity) {
        if (entity.getId() == null) {
            return error("ID不能为空");
        }
        if (entity.getDeviceId() == null || entity.getDeviceId().trim().isEmpty()) {
            return error("设备ID不能为空");
        }
        boolean result = sipBlackService.update(entity);
        return result ? success("更新成功") : error("更新失败");
    }

    /**
     * 删除黑名单
     */
    @Log(title = "黑名单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        boolean result = sipBlackService.delete(id);
        return result ? success("删除成功") : error("删除失败");
    }
}
