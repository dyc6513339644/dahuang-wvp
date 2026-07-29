package com.ruoyi.wvp.service.impl;

import com.ruoyi.wvp.mapper.UserChannelMapper;
import com.ruoyi.wvp.service.IUserChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户分配通道 Service 实现
 *
 * @author ruoyi
 */
@Service
public class UserChannelServiceImpl implements IUserChannelService {

    @Autowired
    private UserChannelMapper userChannelMapper;

    @Override
    public boolean hasUserChannels(Long userId) {
        return userChannelMapper.countByUserId(userId) > 0;
    }

    @Override
    public List<String> getDistinctDeviceIdsByUserId(Long userId) {
        return userChannelMapper.selectDistinctDeviceIdsByUserId(userId);
    }
}
