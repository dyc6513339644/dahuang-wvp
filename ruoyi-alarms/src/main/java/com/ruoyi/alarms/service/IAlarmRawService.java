package com.ruoyi.alarms.service;

import java.util.Map;

/**
 * 原始表（wvp_alarms_raw）监控服务。
 * <p>
 * 该表现在只承担两件事：Stream 投递超限后的死信留档，以及历史遗留数据的消费清理，
 * 不再作为内存队列的降级缓冲。
 */
public interface IAlarmRawService {

    /** 表内各状态行数统计（待处理 / 处理中 / 失败） */
    Map<String, Object> stats();
}
