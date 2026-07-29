package com.ruoyi.wvp.service;

import java.util.List;

/**
 * 用户分配通道 Service 接口
 *
 * @author ruoyi
 */
public interface IUserChannelService {

    /**
     * 判断用户是否有分配通道
     * @param userId 用户ID
     * @return true=有分配通道，false=无
     */
    boolean hasUserChannels(Long userId);

    /**
     * 获取用户分配的设备ID列表（去重，过滤空值）
     * @param userId ���户ID
     * @return 设备ID列表
     */
    List<String> getDistinctDeviceIdsByUserId(Long userId);
}
