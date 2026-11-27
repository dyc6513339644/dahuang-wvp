package com.ruoyi.alarms.task;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.alarms.config.AlarmProperties;
import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.domain.AlarmRaw;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.mapper.AlarmRawMapper;
import com.ruoyi.alarms.service.IAlarmIngestService;
import com.ruoyi.alarms.service.impl.AlarmQueueServiceImpl;
import com.ruoyi.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.PendingMessage;
import org.springframework.data.redis.connection.stream.PendingMessages;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/** Redis Stream 消费者，组读后批量落库再 XACK，失败不确认由 XCLAIM 回收重投 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmStreamConsumer {

    /** 每轮最多连续读取的批次数，避免单次占用线程过久 */
    private static final int MAX_ROUNDS = 50;

    /** 关闭时等待消费线程退出的总时长 */
    private static final long SHUTDOWN_WAIT_MS = 1500L;

    /** pending 消息空闲超过该分钟数即重新认领（消费者崩溃/宕机场景） */
    private static final int PENDING_IDLE_MINUTES = 5;

    /** 单条消息最大投递次数，超过后转入死信 wvp_alarms_raw（status=2） */
    private static final int MAX_RETRY = 3;

    /** pending 回收扫描间隔（毫秒） */
    private static final long RECLAIM_INTERVAL_MS = 60000L;

    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmProperties alarmProperties;
    private final IAlarmIngestService alarmIngestService;
    private final AlarmRawMapper alarmRawMapper;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicLong consumed = new AtomicLong();
    private final AtomicLong acked = new AtomicLong();
    private final AtomicLong consumeFailed = new AtomicLong();
    private final AtomicLong deadLettered = new AtomicLong();
    private final AtomicLong reclaimed = new AtomicLong();

    private final List<Thread> workers = new ArrayList<>();

    private final String instanceId = buildInstanceId();

    @PostConstruct
    public void start() {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        running.set(true);
        int threads = Math.max(1, queue.getConsumeThreads());
        for (int i = 0; i < threads; i++) {
            final int index = i;
            Thread t = new Thread(() -> loop(index), "alarm-stream-consumer-" + index);
            t.setDaemon(true);
            t.start();
            workers.add(t);
        }
        log.info("[统一报警] Stream 消费者已启动：threads={}, batchSize={}, pollInterval={}ms",
                threads, queue.getBatchSize(), queue.getPollIntervalMs());
    }

    @PreDestroy
    public void stop() {
        if (!running.getAndSet(false)) {
            return;
        }
        for (Thread t : workers) {
            t.interrupt();
        }
        // 等线程真正退出，避免连接池先被销毁时在途命令被 cancel 而刷错误日志
        long deadline = System.currentTimeMillis() + SHUTDOWN_WAIT_MS;
        for (Thread t : workers) {
            long wait = deadline - System.currentTimeMillis();
            if (wait <= 0L) {
                break;
            }
            try {
                t.join(wait);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        workers.clear();
        log.info("[统一报警] Stream 消费者已停止");
    }

    // ---------- 消费循环

    private void loop(int index) {
        String consumerName = instanceId + "-" + index;
        long pollInterval = Math.max(20L, alarmProperties.getQueue().getPollIntervalMs());
        while (running.get()) {
            try {
                int handled = drain(consumerName);
                if (handled <= 0) {
                    sleep(pollInterval);
                }
            } catch (Exception e) {
                // 关闭或中断打断阻塞读属正常退出，不能按故障刷堆栈
                if (!running.get() || Thread.currentThread().isInterrupted() || isInterrupt(e)) {
                    log.debug("[统一报警] Stream 消费线程退出 consumer={}, reason={}", consumerName, e.getMessage());
                    break;
                }
                log.error("[统一报警] Stream 消费异常 consumer={}", consumerName, e);
                sleep(1000L);
            }
            if (Thread.currentThread().isInterrupted()) {
                break;
            }
        }
    }

    /** 连续读取直到无新消息，返回本次处理的消息条数 */
    private int drain(String consumerName) {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        int batchSize = Math.max(1, queue.getBatchSize());
        int total = 0;
        for (int round = 0; round < MAX_ROUNDS && running.get(); round++) {
            List<MapRecord<String, Object, Object>> records = readNew(consumerName, batchSize);
            if (records == null || records.isEmpty()) {
                break;
            }
            handle(records);
            total += records.size();
            // 不足一批说明已经取空，直接结束本轮
            if (records.size() < batchSize) {
                break;
            }
        }
        return total;
    }

    private List<MapRecord<String, Object, Object>> readNew(String consumerName, int batchSize) {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        return stringRedisTemplate.opsForStream().read(
                Consumer.from(queue.getGroup(), consumerName),
                StreamReadOptions.empty().count(batchSize),
                StreamOffset.create(queue.getStreamKey(), ReadOffset.lastConsumed()));
    }

    // ---------- 消息处理

    /** 批量处理：一次 XREADGROUP 拿到的消息合并成一次批量 INSERT */
    private void handle(List<MapRecord<String, Object, Object>> records) {
        List<AlarmMsg> msgs = new ArrayList<>(records.size());
        List<String> ids = new ArrayList<>(records.size());
        for (MapRecord<String, Object, Object> record : records) {
            String json = bodyOf(record);
            AlarmMsg msg = parse(json);
            if (msg == null) {
                // 报文损坏属于终态，直接确认掉，避免无限重投占用队列
                log.warn("[统一报警] Stream 消息无法解析，已丢弃 id={}", record.getId());
                ack(record.getId().getValue());
                deadLettered.incrementAndGet();
                continue;
            }
            msgs.add(msg);
            ids.add(record.getId().getValue());
        }
        if (msgs.isEmpty()) {
            return;
        }
        consumed.addAndGet(msgs.size());

        boolean[] done;
        try {
            done = alarmIngestService.ingestBatch(msgs);
        } catch (Exception e) {
            // 批量入口自身异常：整批不确认，等 reclaim 重投
            log.error("[统一报警] 批量入库异常，{} 条等待重投", msgs.size(), e);
            consumeFailed.addAndGet(msgs.size());
            return;
        }

        List<String> ackIds = new ArrayList<>(ids.size());
        for (int i = 0; i < done.length && i < ids.size(); i++) {
            if (done[i]) {
                ackIds.add(ids.get(i));
            }
        }
        if (!ackIds.isEmpty()) {
            ack(ackIds.toArray(new String[0]));
            acked.addAndGet(ackIds.size());
        }
        int failed = ids.size() - ackIds.size();
        if (failed > 0) {
            consumeFailed.addAndGet(failed);
            log.warn("[统一报警] {} 条报警入库失败，保留在 pending 等待重投", failed);
        }
    }

    /** 确认消费：成功落库的消息从 pending 中移除 */
    private void ack(String... recordIds) {
        if (recordIds == null || recordIds.length == 0) {
            return;
        }
        try {
            stringRedisTemplate.opsForStream()
                    .acknowledge(alarmProperties.getQueue().getStreamKey(),
                            alarmProperties.getQueue().getGroup(), recordIds);
        } catch (Exception e) {
            log.warn("[统一报警] ACK 失败（消息会在空闲后被重新认领）count={}", recordIds.length, e);
        }
    }

    private AlarmMsg parse(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return JSON.parseObject(json, AlarmMsg.class);
        } catch (Exception e) {
            log.warn("[统一报警] 报警报文反序列化失败", e);
            return null;
        }
    }

    // ---------- pending 回收

    /** 回收空闲超时的 pending 消息，超过投递上限转入死信 */
    @Scheduled(fixedDelay = RECLAIM_INTERVAL_MS)
    public void reclaim() {
        if (!running.get()) {
            return;
        }
        try {
            doReclaim();
        } catch (Exception e) {
            log.error("[统一报警] pending 回收异常", e);
        }
    }

    private void doReclaim() {
        AlarmProperties.Queue queue = alarmProperties.getQueue();
        long idleSeconds = PENDING_IDLE_MINUTES * 60L;
        int batchSize = Math.max(1, queue.getBatchSize());

        PendingMessages pending = stringRedisTemplate.opsForStream()
                .pending(queue.getStreamKey(), queue.getGroup(), Range.<String>unbounded(), batchSize);
        if (pending == null || pending.isEmpty()) {
            return;
        }

        // 记录每条待认领消息的已投递次数，用于判断是否该进死信
        Map<String, Long> deliveries = new LinkedHashMap<>();
        List<RecordId> idleIds = new ArrayList<>();
        for (PendingMessage message : pending) {
            if (message.getElapsedTimeSinceLastDelivery() == null
                    || message.getElapsedTimeSinceLastDelivery().getSeconds() < idleSeconds) {
                continue;
            }
            idleIds.add(message.getId());
            deliveries.put(message.getIdAsString(), message.getTotalDeliveryCount());
        }
        if (idleIds.isEmpty()) {
            return;
        }

        String consumerName = instanceId + "-reclaim";
        List<MapRecord<String, Object, Object>> claimed = stringRedisTemplate.opsForStream()
                .claim(queue.getStreamKey(), queue.getGroup(), consumerName,
                        Duration.ofSeconds(idleSeconds), idleIds.toArray(new RecordId[0]));
        if (claimed == null || claimed.isEmpty()) {
            return;
        }
        reclaimed.addAndGet(claimed.size());
        log.info("[统一报警] 认领超时未确认的报警 {} 条（pending 堆积说明消费者曾异常或入库持续失败）",
                claimed.size());

        int maxRetry = MAX_RETRY;
        List<MapRecord<String, Object, Object>> retryable = new ArrayList<>();
        for (MapRecord<String, Object, Object> record : claimed) {
            Long count = deliveries.get(record.getId().getValue());
            if (count != null && count >= maxRetry) {
                // 超过重试上限：留档后确认，避免坏消息永久阻塞
                deadLetter(record, "投递 " + count + " 次仍未成功");
                ack(record.getId().getValue());
                deadLettered.incrementAndGet();
            } else {
                retryable.add(record);
            }
        }
        if (!retryable.isEmpty()) {
            handle(retryable);
        }
    }

    /** 死信：把原始报文写入 wvp_alarms_raw（status=2），便于排查而不阻塞队列 */
    private void deadLetter(MapRecord<String, Object, Object> record, String reason) {
        // 原始表消费关闭时不落库（否则只写不消费，白白堆积），只留日志
        if (!alarmProperties.getRaw().isEnabled()) {
            log.error("[统一报警] 报警进入死信 id={}, reason={}（原始表消费已关闭，仅记录日志）",
                    record.getId(), reason);
            return;
        }
        String json = bodyOf(record);
        AlarmMsg msg = parse(json);
        AlarmRaw row = new AlarmRaw();
        String now = DateUtils.getTime();
        row.setProtocolType(msg != null && msg.getProtocolType() != null
                ? msg.getProtocolType() : AlarmConstants.PROTOCOL_OTHER);
        row.setDeviceId(truncate(msg == null ? null : msg.getDeviceId(), 64));
        row.setChannelId(truncate(msg == null ? null : msg.getChannelId(), 64));
        row.setMsgJson(truncate(json, 60000));
        row.setStatus(AlarmRaw.STATUS_FAILED);
        row.setRetryCount(MAX_RETRY);
        row.setErrorMsg(truncate(reason, 500));
        row.setReceiveTime(now);
        row.setCreateTime(now);
        try {
            alarmRawMapper.insert(row);
            log.error("[统一报警] 报警已转入死信 wvp_alarms_raw id={}, streamRecordId={}, reason={}",
                    row.getId(), record.getId(), reason);
        } catch (Exception e) {
            // 死信表不可用时只能记日志，绝不能因此阻塞队列
            log.error("[统一报警] 死信落库失败 streamRecordId={}，报文已丢弃", record.getId(), e);
        }
    }

    // ---------- 工具

    /** 把 Stream 返回的 Object 统一收敛成 String */
    private String bodyOf(MapRecord<String, Object, Object> record) {
        if (record == null || record.getValue() == null) {
            return null;
        }
        Object value = record.getValue().get(AlarmQueueServiceImpl.FIELD_BODY);
        if (value == null) {
            return null;
        }
        return value instanceof String ? (String) value : String.valueOf(value);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** 沿 cause 链识别中断型异常：连接销毁、线程 interrupt、命令 cancel 都归此类 */
    private boolean isInterrupt(Throwable e) {
        Throwable cur = e;
        int depth = 0;
        while (cur != null && depth++ < 8) {
            if (cur instanceof InterruptedException
                    || cur instanceof java.util.concurrent.CancellationException) {
                return true;
            }
            String name = cur.getClass().getSimpleName();
            if (name.contains("Interrupt") || name.contains("Cancel")) {
                return true;
            }
            cur = cur.getCause();
        }
        return false;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    private static String buildInstanceId() {
        String host;
        try {
            host = java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            host = "unknown";
        }
        return "alarms-" + host + "-" + new java.text.SimpleDateFormat("HHmmss").format(new Date())
                + "-" + Math.abs(System.identityHashCode(new Object()) % 1000);
    }

    /** 运行时计数，供监控接口读取 */
    public Map<String, Object> counters() {
        Map<String, Object> result = new HashMap<>(8);
        result.put("consumed", consumed.get());
        result.put("acked", acked.get());
        result.put("consumeFailed", consumeFailed.get());
        result.put("deadLettered", deadLettered.get());
        result.put("reclaimed", reclaimed.get());
        return result;
    }
}
