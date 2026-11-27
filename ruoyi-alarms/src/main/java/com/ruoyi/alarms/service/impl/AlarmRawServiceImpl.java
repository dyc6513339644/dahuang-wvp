package com.ruoyi.alarms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.alarms.config.AlarmProperties;
import com.ruoyi.alarms.domain.AlarmRaw;
import com.ruoyi.alarms.mapper.AlarmRawMapper;
import com.ruoyi.alarms.service.IAlarmRawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 原始表（wvp_alarms_raw）监控实现。
 * <p>
 * 报警正常链路只走 Redis Stream；本表仅存放 Stream 投递超限的死信与历史遗留数据，
 * 由 {@code AlarmRawConsumer} 消费清理，因此这里不再维护内存缓冲队列。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmRawServiceImpl implements IAlarmRawService {

    private final AlarmRawMapper alarmRawMapper;
    private final AlarmProperties alarmProperties;

    @Override
    public Map<String, Object> stats() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("enabled", alarmProperties.getRaw().isEnabled());
        // 表内积压：待处理/处理中/失败。表未建或库异常时不影响监控接口
        try {
            result.put("pendingRows", countByStatus(AlarmRaw.STATUS_PENDING));
            result.put("processingRows", countByStatus(AlarmRaw.STATUS_PROCESSING));
            result.put("failedRows", countByStatus(AlarmRaw.STATUS_FAILED));
        } catch (Exception e) {
            result.put("pendingRows", -1);
            result.put("processingRows", -1);
            result.put("failedRows", -1);
        }
        return result;
    }

    /** 按状态计数（MyBatis-Plus selectCount，返回 Long） */
    private Long countByStatus(int status) {
        return alarmRawMapper.selectCount(new LambdaQueryWrapper<AlarmRaw>()
                .eq(AlarmRaw::getStatus, status));
    }
}
