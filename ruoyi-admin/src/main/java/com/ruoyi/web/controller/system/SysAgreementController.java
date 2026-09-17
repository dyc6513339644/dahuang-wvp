package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAgreement;
import com.ruoyi.system.service.ISysAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 协议管理Controller（用户协议 / 隐私政策等）
 * <p>
 * 管理端：type/{type}（按类型查询）、PUT（保存，按类型 upsert）。
 * 公开端：public/{type}（移动端匿名查询协议内容）。
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/agreement")
public class SysAgreementController extends BaseController {

    @Autowired
    private ISysAgreementService agreementService;

    /**
     * 管理端：按协议类型查询（agreement用户协议 / privacy隐私政策）
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:query')")
    @GetMapping("/type/{agreementType}")
    public AjaxResult getByType(@PathVariable String agreementType) {
        return success(agreementService.selectAgreementByType(agreementType));
    }

    /**
     * 管理端：保存协议（按类型 upsert：存在更新、不存在新增）
     */
    @PreAuthorize("@ss.hasPermi('system:agreement:edit')")
    @Log(title = "协议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult save(@RequestBody SysAgreement agreement) {
        if (StringUtils.isEmpty(agreement.getAgreementType())) {
            return error("协议类型不能为空");
        }
        if (StringUtils.isEmpty(agreement.getAgreementName())) {
            return error("协议名称不能为空");
        }
        agreement.setUpdateBy(getUsername());
        SysAgreement existing = agreementService.selectAgreementByType(agreement.getAgreementType());
        boolean result;
        if (existing != null) {
            agreement.setAgreementId(existing.getAgreementId());
            result = agreementService.updateAgreement(agreement);
        } else {
            agreement.setCreateBy(getUsername());
            result = agreementService.insertAgreement(agreement);
        }
        return result ? success() : error("保存协议失败");
    }

    /**
     * 公开端：移动端匿名查询协议内容
     * <p>
     * 入参 agreementType：agreement用户协议 / privacy隐私政策
     */
    @Anonymous
    @GetMapping("/public/{agreementType}")
    public AjaxResult publicInfo(@PathVariable String agreementType) {
        return success(agreementService.selectAgreementByType(agreementType));
    }
}
