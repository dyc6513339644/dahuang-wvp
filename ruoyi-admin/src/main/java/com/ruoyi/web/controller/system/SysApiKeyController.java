package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.WvpUserApiKey;
import com.ruoyi.system.service.IWvpUserApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户API密钥管理Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/apikey")
public class SysApiKeyController extends BaseController {

    @Autowired
    private IWvpUserApiKeyService apiKeyService;

    /**
     * 获取用户的API密钥列表
     *
     * @param userId 用户ID
     * @return 密钥列表（不含secretKey）
     */
    @PreAuthorize("@ss.hasPermi('system:apikey:query')")
    @GetMapping("/list/{userId}")
    public AjaxResult list(@PathVariable Long userId) {
        List<WvpUserApiKey> list = apiKeyService.listByUserId(userId);
        return success(list);
    }

    /**
     * 生成新的API密钥（AK/SK）
     * 创建成功后返回完整的密钥信息，其中secretKey仅此一次可见
     *
     * @param params { userId, appName, remark }
     * @return 完整的密钥信息（含secretKey明文）
     */
    @PreAuthorize("@ss.hasPermi('system:apikey:create')")
    @Log(title = "API密钥管理", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public AjaxResult generate(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        String appName = params.getOrDefault("appName", "").toString();
        String remark = params.getOrDefault("remark", "").toString();

        WvpUserApiKey result = apiKeyService.generate(userId, appName, remark, getUsername());

        // 构建返回数据，SK仅此一次可见
        Map<String, Object> data = new HashMap<>();
        data.put("id", result.getId());
        data.put("userId", result.getUserId());
        data.put("accessKey", result.getAccessKey());
        data.put("secretKey", result.getSecretKey());  // 仅创建时返回
        data.put("appName", result.getAppName());
        data.put("enable", result.getEnable());
        data.put("createTime", result.getCreateTime());
        data.put("remark", result.getRemark());

        return success(data);
    }

    /**
     * 切换密钥启用/停用状态
     *
     * @param id 密钥ID
     */
    @PreAuthorize("@ss.hasPermi('system:apikey:edit')")
    @Log(title = "API密钥管理", businessType = BusinessType.UPDATE)
    @PutMapping("/toggle/{id}")
    public AjaxResult toggle(@PathVariable Long id) {
        boolean result = apiKeyService.toggleEnable(id, getUsername());
        return result ? success() : error("操作失败，密钥不存在");
    }

    /**
     * 删除密钥
     *
     * @param id 密钥ID
     */
    @PreAuthorize("@ss.hasPermi('system:apikey:remove')")
    @Log(title = "API密钥管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        boolean result = apiKeyService.delete(id);
        return result ? success() : error("删除失败，密钥不存在");
    }
}
