package com.ruoyi.alarms.task;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.alarms.config.AlarmProperties;
import com.ruoyi.alarms.domain.AlarmRaw;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.mapper.AlarmRawMapper;
import com.ruoyi.alarms.service.IAlarmIngestService;
import com.ruoyi.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/** 原始表消费器，抢占领取后反序列化写 wvp_alarms，成功删除失败累加重试 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmRawConsumer {

    /** 处理中的锁超时扫描间隔 */
    private static final long UNLOCK_SCAN_MS = 60_000L;
    /** 超期数据清理间隔 */
    private static final long CLEAN_SCAN_MS = 3_600_000L;

    private final AlarmRawMapper alarmRawMapper;
    private final IAlarmIngestService alarmIngestService;
    private final AlarmProperties alarmProperties;

    /** 集群实例标识，避免多实例 owner 冲突 */
    private final String instanceId = buildInstanceId();
    private final AtomicLong claimSeq = new AtomicLong();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private volatile long lastUnlockAt = 0L;
    private volatile long lastCleanAt = 0L;

    /** 原始表是否还有遗留积压（Stream 切换前写入但未消费的数据） */
    private volatile boolean legacyBacklog = false;
    private volatile long lastBacklogCheckAt = 0L;

    private ExecutorService worker;

    @PostConstruct
    public void init() {
        if (!alarmProperties.getRaw().isEnabled()) {
            return;
        }
        int threads = Math.max(1, alarmProperties.getRaw().getConsumeThreads());
        ThreadFactory factory = r -> {
            Thread t = new Thread(r, "alarm-raw-consumer");
            t.setDaemon(true);
            return t;
        };
        worker = Executors.newFixedThreadPool(threads, factory);
        log.info("[统一报警] 原始表消费器已启动：threads={}, claimBatch={}, interval={}ms",
                threads, alarmProperties.getRaw().getClaimBatchSize(), alarmProperties.getRaw().getConsumeIntervalMs());
    }

    @PreDestroy
    public void destroy() {
        if (worker != null) {
            worker.shutdownNow();
        }
    }

    /** 定时触发：真正的工作丢到独立线程池，避免长耗时占用 Spring 调度线程影响其它定时任务 */
    @Scheduled(fixedDelayString = "${alarm.raw.consume-interval-ms:1000}")
    public void tick() {
        if (worker == null || !alarmProperties.getRaw().isEnabled()) {
            return;
        }
        // 原始表只存 Stream 死信与历史遗留数据：无遗留积压时无需消费
        if (!hasLegacyBacklog()) {
            return;
        }
        if (!running.compareAndSet(false, true)) {
            return;
        }
        try {
            worker.execute(() -> {
                try {
                    drain();
                    maintain();
                } catch (Exception e) {
                    log.error("[统一报警] 原始表消费异常", e);
                } finally {
                    running.set(false);
                    refreshLegacyBacklog();
                }
            });
        } catch (Exception e) {
            running.set(false);
            log.error("[统一报警] 原始表消费任务提交失败", e);
        }
    }

    /** 循环消费直到待处理队列被取空（或本批不足一页，说明已消费完） */
    private void drain() {
        int claimBatch = Math.max(1, alarmProperties.getRaw().getClaimBatchSize());
        int maxRetry = Math.max(1, alarmProperties.getRaw().getMaxRetry());
        for (int round = 0; round < 50; round++) {
            List<AlarmRaw> batch = claim(claimBatch);
            if (batch.isEmpty()) {
                return;
            }
            List<Long> doneIds = new ArrayList<>(batch.size());
            boolean hasFailure = false;
            for (AlarmRaw raw : batch) {
                try {
                    AlarmMsg msg = JSON.parseObject(raw.getMsgJson(), AlarmMsg.class);
                    if (msg == null) {
                        alarmRawMapper.releaseFailed(raw.getId(), "报文解析为空", maxRetry);
                        hasFailure = true;
                        continue;
                    }
                    // 注意：必须走 ingestDirect，走 ingest 会再次入队造成死循环
                    if (alarmIngestService.ingestDirect(msg)) {
                        doneIds.add(raw.getId());
                    } else {
                        alarmRawMapper.releaseFailed(raw.getId(), "写入 wvp_alarms 失败", maxRetry);
                        hasFailure = true;
                    }
                } catch (Exception e) {
                    // 单条异常不能影响整批
                    log.warn("[统一报警] 原始报警处理失败 id={}", raw.getId(), e);
                    safeRelease(raw.getId(), e, maxRetry);
                    hasFailure = true;
                }
            }
            if (!doneIds.isEmpty()) {
                // MyBatis-Plus 内置批量删除：DELETE ... WHERE id IN (...)
                alarmRawMapper.deleteBatchIds(doneIds);
            }
            // 有失败就结束本轮，等下一个消费周期重试：避免数据库故障时在同一轮里把重试次数瞬间烧完
            if (hasFailure || batch.size() < claimBatch) {
                return;
            }
        }
    }

    /** 抢占一批待处理行，靠 status=0 条件更新保证同一行只有一个赢家 */
    private List<AlarmRaw> claim(int claimBatch) {
        LambdaQueryWrapper<AlarmRaw> wrapper = new LambdaQueryWrapper<AlarmRaw>()
                .eq(AlarmRaw::getStatus, AlarmRaw.STATUS_PENDING)
                .orderByAsc(AlarmRaw::getId);
        // searchCount=false：只取数据不跑 count，避免每次多一次全表统计
        Page<AlarmRaw> page = new Page<>(1, claimBatch, false);
        List<AlarmRaw> pending = alarmRawMapper.selectPage(page, wrapper).getRecords();
        if (pending == null || pending.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = new ArrayList<>(pending.size());
        for (AlarmRaw raw : pending) {
            ids.add(raw.getId());
        }
        String owner = instanceId + "-" + claimSeq.incrementAndGet();
        LambdaUpdateWrapper<AlarmRaw> lock = new LambdaUpdateWrapper<AlarmRaw>()
                .set(AlarmRaw::getStatus, AlarmRaw.STATUS_PROCESSING)
                .set(AlarmRaw::getLockOwner, owner)
                .set(AlarmRaw::getLockTime, DateUtils.getTime())
                .eq(AlarmRaw::getStatus, AlarmRaw.STATUS_PENDING)
                .in(AlarmRaw::getId, ids);
        if (alarmRawMapper.update(null, lock) <= 0) {
            return new ArrayList<>();
        }
        List<AlarmRaw> mine = alarmRawMapper.selectList(new LambdaQueryWrapper<AlarmRaw>()
                .eq(AlarmRaw::getStatus, AlarmRaw.STATUS_PROCESSING)
                .eq(AlarmRaw::getLockOwner, owner));
        return mine == null ? new ArrayList<>() : mine;
    }

    /** 维护任务：解锁超时行 + 清理超期数据（低频执行） */
    private void maintain() {
        long now = System.currentTimeMillis();
        if (now - lastUnlockAt > UNLOCK_SCAN_MS) {
            lastUnlockAt = now;
            try {
                int timeoutMinutes = Math.max(1, alarmProperties.getRaw().getLockTimeoutMinutes());
                String before = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,
                        new Date(now - timeoutMinutes * 60_000L));
                int unlocked = alarmRawMapper.unlockExpired(before);
                if (unlocked > 0) {
                    log.warn("[统一报警] 解锁处理超时的原始报警 {} 条（消费进程可能异常退出）", unlocked);
                }
            } catch (Exception e) {
                log.warn("[统一报警] 解锁超时原始报警失败", e);
            }
        }
        if (now - lastCleanAt > CLEAN_SCAN_MS) {
            lastCleanAt = now;
            int retainDays = alarmProperties.getRaw().getRetainDays();
            if (retainDays <= 0) {
                return;
            }
            try {
                String before = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,
                        new Date(now - retainDays * 86_400_000L));
                int cleaned = alarmRawMapper.delete(new LambdaQueryWrapper<AlarmRaw>()
                        .lt(AlarmRaw::getReceiveTime, before)
                        .in(AlarmRaw::getStatus, AlarmRaw.STATUS_PENDING, AlarmRaw.STATUS_FAILED));
                if (cleaned > 0) {
                    log.info("[统一报警] 清理超期原始报警 {} 条（保留 {} 天）", cleaned, retainDays);
                }
            } catch (Exception e) {
                log.warn("[统一报警] 清理超期原始报警失败", e);
            }
        }
    }

    /** Stream 正常时低频探测原始表遗留积压，避免每秒空查 */
    private boolean hasLegacyBacklog() {
        if (legacyBacklog) {
            return true;
        }
        long now = System.currentTimeMillis();
        if (now - lastBacklogCheckAt < 30_000L) {
            return false;
        }
        lastBacklogCheckAt = now;
        try {
            Long pending = countPending();
            legacyBacklog = pending != null && pending > 0;
            if (legacyBacklog) {
                log.info("[统一报警] 发现原始表遗留/死信报警 {} 条，继续消费", pending);
            }
        } catch (Exception e) {
            log.debug("[统一报警] 原始表积压探测失败", e);
        }
        return legacyBacklog;
    }

    /** 消费结束后重新评估遗留积压，已清空则停止空转 */
    private void refreshLegacyBacklog() {
        if (!legacyBacklog) {
            return;
        }
        try {
            Long pending = countPending();
            legacyBacklog = pending != null && pending > 0;
            if (!legacyBacklog) {
                log.info("[统一报警] 原始表遗留数据已清空，回到待命状态");
            }
        } catch (Exception e) {
            log.debug("[统一报警] 原始表积压复检失败", e);
        }
    }

    /** 待处理行数，selectCount 返回 Long */
    private Long countPending() {
        return alarmRawMapper.selectCount(new LambdaQueryWrapper<AlarmRaw>()
                .eq(AlarmRaw::getStatus, AlarmRaw.STATUS_PENDING));
    }

    private void safeRelease(Long id, Exception e, int maxRetry) {
        try {
            String reason = e.getClass().getSimpleName() + ": " + e.getMessage();
            alarmRawMapper.releaseFailed(id, reason.length() > 500 ? reason.substring(0, 500) : reason, maxRetry);
        } catch (Exception ex) {
            log.error("[统一报警] 回写原始报警失败状态异常 id={}", id, ex);
        }
    }

    private static String buildInstanceId() {
        try {
            return "alarms-" + java.net.InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "alarms-" + ProcessHandleHolder.PID;
        }
    }

    /** 拿不到主机名时退化为 PID，避免 owner 重复 */
    private static final class ProcessHandleHolder {
        private static final String PID = String.valueOf(new SimpleDateFormat("mmssSSS").format(new Date()))
                + Math.abs(System.identityHashCode(new Object()) % 1000);
    }
}
