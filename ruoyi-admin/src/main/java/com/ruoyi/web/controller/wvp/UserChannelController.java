package com.ruoyi.web.controller.wvp;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.wvp.mapper.UserChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户通道关联控制器
 */
@RestController
@RequestMapping("/system/user/channel")
public class UserChannelController extends BaseController {

    @Autowired
    private UserChannelMapper userChannelMapper;

    /**
     * 获取用户已关联的通道ID列表
     */
    @GetMapping("/{userId}")
    public AjaxResult getUserChannels(@PathVariable Long userId) {
        List<String> channels = userChannelMapper.selectChannelsByUserId(userId);
        return success(channels);
    }

    /**
     * 保存用户通道关联（全量替换，前端传 {channelId, deviceId} 列表）
     */
    @PostMapping("/{userId}")
    public AjaxResult saveUserChannels(@PathVariable Long userId, @RequestBody Map<String, List<Map<String, String>>> params) {
        List<Map<String, String>> channels = params.get("channels");
        userChannelMapper.deleteByUserId(userId);
        if (channels != null && !channels.isEmpty()) {
            userChannelMapper.insertUserChannelsBatch(userId, channels);
        }
        return success();
    }
}
