package com.ruoyi.alarms.service.impl;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.alarms.config.AlarmProperties;
import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.service.IAlarmQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.stream.ByteRecord;
import org.springframework.data.redis.connection.stream.PendingMessagesSummary;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Redis Stream 队列实现：XADD 投递、消费者组 XACK。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmQueueServiceImpl implements IAlarmQueueService {

    /** Stream 消息体字段名，单字段承载 AlarmMsg 的 JSON */
    public static final String FIELD_BODY = "m";

    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmProperties alarmProperties;

    private final AtomicLong published = new AtomicLong();
    private final AtomicLong failed = new AtomicLong();

    @PostConstruct
    public void init() {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        try {
            stringRedisTemplate.opsForStream().createGroup(queue.getStreamKey(), queue.getGroup());
            log.info("[统一报警] 消费者组已创建：{} / {}", queue.getStreamKey(), queue.getGroup());
        } catch (Exception e) {
            // 组已存在是正常情况（重启 / 多实例）；其余异常只告警，运行期投递失败会同步直写
            if (!isBusyGroup(e)) {
                log.error("[统一报警] Redis Stream 队列初始化失败 key={}, group={}，"
                                + "投递将失败并同步直写，请检查 Redis 连接",
                        queue.getStreamKey(), queue.getGroup(), e);
                return;
            }
        }
        log.info("[统一报警] Redis Stream 报警队列已就绪：key={}, group={}, maxLen={}, batchSize={}",
                queue.getStreamKey(), queue.getGroup(), queue.getMaxLen(), queue.getBatchSize());
    }

    @Override
    public boolean publish(AlarmMsg msg) {
        if (msg == null) {
            return false;
        }
        try {
            xadd(msg);
            published.incrementAndGet();
            return true;
        } catch (Exception e) {
            failed.incrementAndGet();
            log.error("[统一报警] Redis Stream 投递失败，改同步直写 deviceId={}", msg.getDeviceId(), e);
            return false;
        }
    }

    @Override
    public Map<String, Object> stats() {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("streamKey", queue.getStreamKey());
        result.put("group", queue.getGroup());
        result.put("published", published.get());
        result.put("publishFailed", failed.get());
        result.put("streamLength", streamLength());
        try {
            PendingMessagesSummary summary = stringRedisTemplate.opsForStream()
                    .pending(queue.getStreamKey(), queue.getGroup());
            result.put("pendingCount", summary == null ? 0L : summary.getTotalPendingMessages());
            result.put("consumerCount", summary == null || summary.getPendingMessagesPerConsumer() == null
                    ? 0 : summary.getPendingMessagesPerConsumer().size());
        } catch (Exception e) {
            result.put("pendingCount", -1L);
            result.put("consumerCount", -1L);
        }
        return result;
    }

    // ---------- 内部

    private void xadd(AlarmMsg msg) {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        // 原始报文体积大，入队前截断，避免单条消息过大挤占 Redis 内存
        if (msg.getRawPayload() != null && msg.getRawPayload().length() > AlarmConstants.RAW_PAYLOAD_MAX_LENGTH) {
            msg.setRawPayload(msg.getRawPayload().substring(0, AlarmConstants.RAW_PAYLOAD_MAX_LENGTH));
        }
        byte[] key = queue.getStreamKey().getBytes(StandardCharsets.UTF_8);
        Map<byte[], byte[]> body = Collections.singletonMap(
                FIELD_BODY.getBytes(StandardCharsets.UTF_8),
                JSON.toJSONString(msg).getBytes(StandardCharsets.UTF_8));
        ByteRecord record = StreamRecords.rawBytes(body).withStreamKey(key);
        // MAXLEN ~ N：近似裁剪，超过上限丢弃最旧消息，O(1) 不阻塞 Redis
        RedisStreamCommands.XAddOptions options = RedisStreamCommands.XAddOptions
                .maxlen(Math.max(1024L, queue.getMaxLen()))
                .approximateTrimming(true);
        stringRedisTemplate.execute((RedisCallback<RecordId>) connection ->
                connection.streamCommands().xAdd(record, options));
    }

    private boolean isBusyGroup(Exception e) {
        String message = e.getMessage();
        return message != null && message.toUpperCase().contains("BUSYGROUP");
    }

    private long streamLength() {
        try {
            Long size = stringRedisTemplate.opsForStream().size(alarmProperties.getQueue().getStreamKey());
            return size == null ? 0L : size;
        } catch (Exception e) {
            return -1L;
        }
    }
}
