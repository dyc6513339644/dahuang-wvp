package com.ruoyi.alarms.service;

import com.ruoyi.alarms.domain.dto.AlarmMsg;

import java.util.Map;

/** 报警队列服务，协议线程只投递，解析落库由 Stream 消费者异步完成 */
public interface IAlarmQueueService {

    /** 投递报警报文（非阻塞），失败返回 false 由调用方同步直写 */
    boolean publish(AlarmMsg msg);

    /** 队列监控数据（Stream 长度、pending 积压、投递计数） */
    Map<String, Object> stats();
}
