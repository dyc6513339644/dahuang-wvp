package com.ruoyi.alarms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 统一报警模块配置（前缀 alarm） */
@Data
@Component
@ConfigurationProperties(prefix = "alarm")
public class AlarmProperties {

    /** 统一报警总开关，关闭后各协议均不再写入 wvp_alarms */
    private boolean enabled = true;

    /**
     * 报警去重间隔（秒），默认 15。
     * <p>
     * 同一设备、同一通道、同一归一化报警类型在该时间内重复上报时，只入库第一条；
     * 不同类型各自独立计时，互不压制。0 或负数表示关闭去重。
     */
    private int dedupIntervalSeconds = 15;

    /** 原始数据中间表（wvp_alarms_raw）配置：只用于死信留档与历史遗留数据消费 */
    private Raw raw = new Raw();

    /** Redis Stream 报警队列配置 */
    private Queue queue = new Queue();

    /** Redis Stream 报警队列配置（投递失败自动同步直写，无单独开关） */
    @Data
    public static class Queue {

        /** Stream 键名 */
        private String streamKey = "alarm:stream:ingest";

        /** 消费者组名，多实例需保持一致 */
        private String group = "alarm-ingest";

        /** Stream 最大长度，近似裁剪，超出丢弃最旧消息 */
        private long maxLen = 200000L;

        /** 单批读取条数，同时决定单次批量 INSERT 的规模 */
        private int batchSize = 500;

        /** 轮询间隔（毫秒），越小延迟越低、Redis 压力越大 */
        private long pollIntervalMs = 200L;

        /** 消费线程数 */
        private int consumeThreads = 2;

        // pending 空闲认领 5 分钟、最大重试 3 次、回收扫描间隔 60s、单轮最多 50 批
        // 均为可靠性内部细节，已写死在 AlarmStreamConsumer 中，不再提供配置
    }

    @Data
    public static class Raw {

        /** 是否启用原始表消费（死信留档与历史遗留数据清理），false 时 raw 表不再被消费 */
        private boolean enabled = true;

        /** 每轮从 raw 表领取的条数 */
        private int claimBatchSize = 200;

        /** 消费间隔（毫秒） */
        private long consumeIntervalMs = 1000;

        /** 消费线程数 */
        private int consumeThreads = 1;

        /** 单条最大重试次数，超过后置为失败态（2），不再重试 */
        private int maxRetry = 3;

        /** 处理中超过该分钟数视为消费进程异常退出，自动解锁回待处理 */
        private int lockTimeoutMinutes = 5;

        /** 失败态与超期未处理数据的保留天数，0 表示不自动清理 */
        private int retainDays = 7;
    }
}
