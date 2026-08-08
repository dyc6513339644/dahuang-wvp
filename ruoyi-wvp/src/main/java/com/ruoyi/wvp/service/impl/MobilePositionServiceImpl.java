package com.ruoyi.wvp.service.impl;


import com.ruoyi.wvp.conf.UserSetting;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import com.ruoyi.wvp.gb28181.bean.MobilePosition;
import com.ruoyi.wvp.gb28181.bean.Platform;
import com.ruoyi.wvp.mapper.DeviceChannelMapper;
import com.ruoyi.wvp.mapper.DeviceMobilePositionMapper;
import com.ruoyi.wvp.mapper.PlatformMapper;
import com.ruoyi.wvp.service.IMobilePositionService;
import com.ruoyi.wvp.service.bean.GPSMsgInfo;
import com.ruoyi.wvp.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MobilePositionServiceImpl implements IMobilePositionService {

    @Autowired
    private DeviceChannelMapper channelMapper;

    @Autowired
    private DeviceMobilePositionMapper mobilePositionMapper;

    @Autowired
    private UserSetting userSetting;


    @Autowired
    private PlatformMapper platformMapper;

    @Autowired
    private RedisTemplate<String, MobilePosition> redisTemplate;

    private final String REDIS_MOBILE_POSITION_LIST = "redis_mobile_position_list";

    /**
     * Lua脚本：从List右侧读取最多 count 条（只读，不删）
     */
    private final DefaultRedisScript<List> BATCH_READ = new DefaultRedisScript<>(
        "local n = math.min(tonumber(ARGV[1]), redis.call('LLEN', KEYS[1])); " +
        "if n <= 0 then return {} end; " +
        "return redis.call('LRANGE', KEYS[1], -n, -1);", List.class);

    /**
     * Lua脚本：从List右侧切除 count 条（只删，不读）
     */
    private final DefaultRedisScript<Long> TRIM_RIGHT = new DefaultRedisScript<>(
        "local size = redis.call('LLEN', KEYS[1]); " +
        "local n = math.min(tonumber(ARGV[1]), size); " +
        "if n <= 0 then return 0 end; " +
        "if n == size then redis.call('DEL', KEYS[1]) " +
        "else redis.call('LTRIM', KEYS[1], 0, size - n - 1) end; " +
        "return n;", Long.class);

    @Override
    public void add(MobilePosition mobilePosition) {
        List<MobilePosition> list = new ArrayList<>();
        list.add(mobilePosition);
        add(list);
    }

    @Override
    public void add(List<MobilePosition> mobilePositionList) {
        redisTemplate.opsForList().leftPushAll(REDIS_MOBILE_POSITION_LIST, mobilePositionList);
    }

    /**
     * 只读取，不删除。DB写入成功后由 trimRight 删除
     */
    @SuppressWarnings("unchecked")
    private List<MobilePosition> readBatch(int length) {
        List<MobilePosition> result = redisTemplate.execute(
            BATCH_READ,
            Collections.singletonList(REDIS_MOBILE_POSITION_LIST),
            length
        );
        return result != null ? result : new ArrayList<>();
    }

    /**
     * 删除已成功处理的数据。必须在 DB 写入成功后才调用
     */
    private long trimRight(int count) {
        Long deleted = redisTemplate.execute(
            TRIM_RIGHT,
            Collections.singletonList(REDIS_MOBILE_POSITION_LIST),
            count
        );
        return deleted != null ? deleted : 0;
    }

    /**
     * 查询移动位置轨迹
     */
    @Override
    public synchronized List<MobilePosition> queryMobilePositions(String deviceId, String channelId, String startTime, String endTime) {
        return mobilePositionMapper.queryPositionByDeviceIdAndTime(deviceId, channelId, startTime, endTime);
    }

    @Override
    public List<Platform> queryEnablePlatformListWithAsMessageChannel() {
        return platformMapper.queryEnablePlatformListWithAsMessageChannel();
    }

    /**
     * 查询最新移动位置
     * @param deviceId
     */
    @Override
    public MobilePosition queryLatestPosition(String deviceId) {
        return mobilePositionMapper.queryLatestPositionByDevice(deviceId);
    }

    @Override
    public void updateStreamGPS(List<GPSMsgInfo> gpsMsgInfoList) {
        channelMapper.updateStreamGPS(gpsMsgInfoList);
    }

    /**
     * 定时消费移动位置队列。先读后删：DB写入成功才从Redis删除，确保不丢数据。
     */
    @Scheduled(fixedDelay = 1000)
    public void executeTaskQueue() {
        int countLimit = 3000;

        // 阶段1：只读不删
        List<MobilePosition> mobilePositions = readBatch(countLimit);
        if (mobilePositions == null || mobilePositions.isEmpty()) {
            return;
        }

        int batchSize = mobilePositions.size();
        Long queueDepthBefore = redisTemplate.opsForList().size(REDIS_MOBILE_POSITION_LIST);

        try {
            long startTime = System.currentTimeMillis();

            // 阶段2：写入历史轨迹
            if (userSetting.getSavePositionHistory()) {
                savePositionHistory(mobilePositions);
            }

            // 阶段3：更新通道位置
            updateChannelPositions(mobilePositions);

            // 阶段4：DB写入成功，安全删除Redis数据
            long trimmed = trimRight(batchSize);
            Long queueDepthAfter = redisTemplate.opsForList().size(REDIS_MOBILE_POSITION_LIST);
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("[移动位置] 消费完成 size={}, 删除={}, 队列 {}->{}, 耗时={}ms",
                batchSize, trimmed, queueDepthBefore, queueDepthAfter, elapsed);

        } catch (Exception e) {
            // DB写入失败，Redis数据保留，下次重试
            log.error("[移动位置] DB写入失败，数据保留在Redis待重试, size={}", batchSize, e);
        }
    }

    /**
     * 写入历史轨迹（独立事务，与通道更新解耦）
     */
    public void savePositionHistory(List<MobilePosition> mobilePositions) {
        mobilePositionMapper.batchadd(mobilePositions);
    }

    /**
     * 更新通道最新位置
     */
    public void updateChannelPositions(List<MobilePosition> mobilePositions) {
        Map<String, DeviceChannel> updateChannelMap = new HashMap<>();
        for (MobilePosition mobilePosition : mobilePositions) {
            DeviceChannel deviceChannel = new DeviceChannel();
            deviceChannel.setId(mobilePosition.getChannelId());
            deviceChannel.setDeviceId(mobilePosition.getDeviceId());
            deviceChannel.setLongitude(mobilePosition.getLongitude());
            deviceChannel.setLatitude(mobilePosition.getLatitude());
            deviceChannel.setGpsTime(mobilePosition.getTime());
            deviceChannel.setUpdateTime(DateUtil.getNow());
            updateChannelMap.put(mobilePosition.getDeviceId() + mobilePosition.getChannelId(), deviceChannel);
        }
        List<DeviceChannel> channels = new ArrayList<>(updateChannelMap.values());
        channelMapper.batchUpdatePosition(channels);
    }

}
