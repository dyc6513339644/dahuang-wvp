package com.ruoyi.alarms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.alarms.config.AlarmProperties;
import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.constant.AlarmDictType;
import com.ruoyi.alarms.constant.AlarmType;
import com.ruoyi.alarms.domain.AlarmRecord;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.domain.ref.AlarmChannelRef;
import com.ruoyi.alarms.domain.ref.AlarmDeviceRef;
import com.ruoyi.alarms.mapper.AlarmChannelRefMapper;
import com.ruoyi.alarms.mapper.AlarmDeviceRefMapper;
import com.ruoyi.alarms.mapper.AlarmRecordMapper;
import com.ruoyi.alarms.service.IAlarmIngestService;
import com.ruoyi.alarms.service.IAlarmQueueService;
import com.ruoyi.alarms.support.AlarmDictUtils;
import com.ruoyi.alarms.support.AlarmFilterMatcher;
import com.ruoyi.alarms.support.AlarmTypeResolver;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** 统一报警入库实现，运行在协议线程上，异常一律吞掉不影响主流程 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmIngestServiceImpl implements IAlarmIngestService {

    /** 报警去重键前缀，后接「设备:通道:类型」，TTL 即配置的去重间隔 */
    private static final String DEDUP_KEY_PREFIX = "alarm:dedup:";

    private final AlarmRecordMapper alarmRecordMapper;
    private final AlarmDeviceRefMapper alarmDeviceRefMapper;
    private final AlarmChannelRefMapper alarmChannelRefMapper;
    private final AlarmProperties alarmProperties;
    private final IAlarmQueueService alarmQueueService;
    private final AlarmFilterMatcher alarmFilterMatcher;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Long ingest(AlarmMsg msg) {
        if (msg == null || StringUtils.isBlank(msg.getDeviceId())) {
            return null;
        }
        // 投递前先过滤：命中规则的报警连队列和中间表都不写
        if (alarmFilterMatcher.isFiltered(msg)) {
            return null;
        }
        String protocolType = normalizeProtocol(msg.getProtocolType());
        // 间隔去重放在投递前，重复报警不进 Stream，消费侧不再重复判定
        if (isDuplicated(protocolType, msg)) {
            return null;
        }
        // 队列链路：协议线程只投递一次（Redis XADD 微秒级），落库交给消费者批量完成
        if (alarmQueueService.publish(msg)) {
            return null;
        }
        // 投递失败（Redis 不可用，无内存队列降级）直接同步入库，保证报警不丢
        log.warn("[统一报警] Redis Stream 投递失败，改同步入库 deviceId={}", msg.getDeviceId());
        return ingestQuiet(msg);
    }

    @Override
    public boolean[] ingestBatch(List<AlarmMsg> msgs) {
        if (msgs == null || msgs.isEmpty()) {
            return new boolean[0];
        }
        boolean[] results = new boolean[msgs.size()];
        List<AlarmRecord> records = new ArrayList<>(msgs.size());
        List<Integer> positions = new ArrayList<>(msgs.size());

        for (int i = 0; i < msgs.size(); i++) {
            AlarmMsg msg = msgs.get(i);
            if (msg == null || StringUtils.isBlank(msg.getDeviceId())) {
                // 无效报文属于终态，直接确认，避免反复重投
                results[i] = true;
                continue;
            }
            String protocolType = normalizeProtocol(msg.getProtocolType());
            if (!isEnabled()) {
                results[i] = true;
                continue;
            }
            // 消费侧再过一次：规则新增前已入队的数据、以及同步直写路径同样能过滤
            if (alarmFilterMatcher.isFiltered(msg)) {
                results[i] = true;
                continue;
            }
            // 间隔去重不在消费侧做：入队前已判定并写入去重 key，
            // 这里再判会把同一条报警当成自己的重复项拦掉
            try {
                records.add(buildRecord(msg, protocolType));
                positions.add(i);
            } catch (Exception e) {
                // 组装阶段失败基本是报文本身的问题，重试也不会成功，记录后丢弃
                log.error("[统一报警] 报警报文组装失败，已丢弃 deviceId={}, channelId={}",
                        msg.getDeviceId(), msg.getChannelId(), e);
                results[i] = true;
            }
        }
        if (records.isEmpty()) {
            return results;
        }

        // 批量路径不经过 MyBatis-Plus 的 insert，自动填充不生效，这里统一补创建时间
        Date now = new Date();
        for (AlarmRecord record : records) {
            if (record.getCreateTime() == null) {
                record.setCreateTime(now);
            }
        }

        try {
            alarmRecordMapper.insertBatch(records);
            for (int position : positions) {
                results[position] = true;
            }
            if (log.isDebugEnabled()) {
                log.debug("[统一报警] 批量入库成功 {} 条", records.size());
            }
            return results;
        } catch (Exception e) {
            // 整批失败时回退逐条，定位并跳过单条脏数据，避免一条报警拖垮整批
            log.error("[统一报警] 批量入库失败，回退逐条写入 count={}", records.size(), e);
        }

        for (int k = 0; k < records.size(); k++) {
            int position = positions.get(k);
            AlarmRecord record = records.get(k);
            try {
                alarmRecordMapper.insert(record);
                results[position] = true;
            } catch (Exception e) {
                log.error("[统一报警] 单条入库失败 deviceId={}, channelId={}, type={}",
                        record.getDeviceId(), record.getChannelId(), record.getAlarmType(), e);
                results[position] = false;
            }
        }
        return results;
    }

    @Override
    public boolean ingestDirect(AlarmMsg msg) {
        if (msg == null || StringUtils.isBlank(msg.getDeviceId())) {
            // 无效报文属于终态，标记消费完成，避免原始表反复重试
            return true;
        }
        String protocolType = normalizeProtocol(msg.getProtocolType());
        if (!isEnabled()) {
            return true;
        }
        if (alarmFilterMatcher.isFiltered(msg)) {
            return true;
        }
        // 这里不做间隔去重：raw 表存的是「入库失败待重试」的死信，
        // 重试往往就在去重窗口内，再判重会把本该补写的数据当成重复项丢掉
        try {
            doInsert(msg, protocolType);
            return true;
        } catch (Exception e) {
            log.error("[统一报警] 入库失败 protocol={}, deviceId={}, channelId={}",
                    protocolType, msg.getDeviceId(), msg.getChannelId(), e);
            return false;
        }
    }

    /** 同步入库（吞掉异常，绝不反噬协议线程） */
    private Long ingestQuiet(AlarmMsg msg) {
        String protocolType = normalizeProtocol(msg.getProtocolType());
        if (!isEnabled()) {
            return null;
        }
        try {
            return doInsert(msg, protocolType);
        } catch (Exception e) {
            // 报警表未初始化、字段超长等异常都不能反噬协议线程
            log.error("[统一报警] 入库失败 protocol={}, deviceId={}, channelId={}",
                    protocolType, msg.getDeviceId(), msg.getChannelId(), e);
            return null;
        }
    }

    /** 真正落库：协议归一化 + 类型/级别归一化 + 名称快照。异常向上抛，由调用方决定吞掉还是重试 */
    private Long doInsert(AlarmMsg msg, String protocolType) {
        AlarmRecord record = buildRecord(msg, protocolType);
        alarmRecordMapper.insert(record);
        if (log.isDebugEnabled()) {
            log.debug("[统一报警] 入库成功 id={}, protocol={}, deviceId={}, type={}",
                    record.getId(), protocolType, record.getDeviceId(), record.getAlarmType());
        }
        return record.getId();
    }

    /** 组装入库对象：协议归一化 + 类型/级别归一化 + 名称快照 */
    private AlarmRecord buildRecord(AlarmMsg msg, String protocolType) {
        String deviceId = normalizeDeviceId(protocolType, msg.getDeviceId());
        String channelId = normalizeChannelId(protocolType, deviceId, msg.getChannelId());

        AlarmType type = AlarmTypeResolver.resolve(protocolType, msg.getAlarmCode(), msg.getAlarmMethod(),
                msg.getEventType(), msg.getAlarmType());
        Integer level = msg.getAlarmLevel() != null ? msg.getAlarmLevel() : type.getDefaultLevel();
        if (level < AlarmConstants.LEVEL_CRITICAL || level > AlarmConstants.LEVEL_INFO) {
            level = type.getDefaultLevel();
        }

        AlarmRecord record = new AlarmRecord();
        record.setProtocolType(protocolType);
        record.setDeviceId(truncate(deviceId, 64));
        record.setDeviceName(truncate(firstNotBlank(msg.getDeviceName(), resolveDeviceName(deviceId)), 255));
        record.setChannelId(truncate(channelId, 64));
        record.setChannelName(truncate(firstNotBlank(msg.getChannelName(), resolveChannelName(channelId)), 255));
        record.setAlarmCode(truncate(msg.getAlarmCode(), 64));
        record.setAlarmType(type.getCode());
        // 名称快照写入字典中的当前名称
        record.setAlarmTypeName(AlarmDictUtils.label(AlarmDictType.ALARM_TYPE, type.getCode()));
        record.setAlarmLevel(level);
        record.setAlarmMethod(truncate(msg.getAlarmMethod(), 32));
        record.setAlarmTime(StringUtils.isNotBlank(msg.getAlarmTime()) ? truncate(msg.getAlarmTime(), 32) : DateUtils.getTime());
        record.setAlarmDesc(truncate(msg.getAlarmDesc(), 1000));
        record.setLongitude(msg.getLongitude());
        record.setLatitude(msg.getLatitude());
        record.setImageUrl(truncate(msg.getImageUrl(), 500));
        record.setHandleStatus(AlarmConstants.HANDLE_UNHANDLED);
        return record;
    }

    /** 入库开关：只看总开关，各协议统一写入 wvp_alarms */
    private boolean isEnabled() {
        return alarmProperties.isEnabled();
    }

    /**
     * 报警间隔去重：同一设备 + 同一通道 + 同一归一化类型，在
     * {@code alarm.dedup-interval-seconds} 内只放行第一条，其余直接丢弃。
     * <p>
     * key 带 interval 秒 TTL，多实例共用同一 Redis 时天然互斥；
     * 判定异常（Redis 抖动）时放行，宁可重复也不丢报警。
     * <p>
     * 只在协议入口 {@link #ingest} 判一次：判过之后同一窗口内的重复报警既不进 Stream 也不落库，
     * 消费侧（{@link #ingestBatch}）与死信重试（{@link #ingestDirect}）都不再判，避免自己拦自己。
     */
    private boolean isDuplicated(String protocolType, AlarmMsg msg) {
        int interval = alarmProperties.getDedupIntervalSeconds();
        if (interval <= 0) {
            return false;
        }
        try {
            String key = dedupKey(protocolType, msg);
            Boolean first = stringRedisTemplate.opsForValue()
                    .setIfAbsent(key, "1", interval, TimeUnit.SECONDS);
            if (Boolean.TRUE.equals(first)) {
                return false;
            }
            if (log.isDebugEnabled()) {
                log.debug("[统一报警] {} 秒内重复报警已过滤 key={}", interval, key);
            }
            return true;
        } catch (Exception e) {
            log.warn("[统一报警] 报警去重判定失败，本条放行 deviceId={}", msg.getDeviceId(), e);
            return false;
        }
    }

    /** 去重维度：设备 + 通道 + 归一化类型；类型码相同的不同原始码视为同一类报警 */
    private String dedupKey(String protocolType, AlarmMsg msg) {
        String deviceId = normalizeDeviceId(protocolType, msg.getDeviceId());
        String channelId = normalizeChannelId(protocolType, deviceId, msg.getChannelId());
        AlarmType type = AlarmTypeResolver.resolve(protocolType, msg.getAlarmCode(), msg.getAlarmMethod(),
                msg.getEventType(), msg.getAlarmType());
        return DEDUP_KEY_PREFIX + deviceId + ':' + (channelId == null ? "-" : channelId) + ':' + type.getCode();
    }

    private String normalizeProtocol(String protocolType) {
        if (StringUtils.isBlank(protocolType)) {
            return AlarmConstants.PROTOCOL_OTHER;
        }
        String protocol = protocolType.trim().toUpperCase();
        return AlarmConstants.isKnownProtocol(protocol) ? protocol : AlarmConstants.PROTOCOL_OTHER;
    }

    /** ISUP 设备在平台内统一使用 haikang_isup_ 前缀，回调只带设备序列号，这里补齐 */
    private String normalizeDeviceId(String protocolType, String deviceId) {
        String id = deviceId == null ? "" : deviceId.trim();
        if (AlarmConstants.PROTOCOL_ISUP.equals(protocolType) && !id.isEmpty()
                && !id.startsWith(AlarmConstants.ISUP_DEVICE_PREFIX)) {
            return AlarmConstants.ISUP_DEVICE_PREFIX + id;
        }
        return id;
    }

    /** ISUP 通道回调可能只给通道号（1 开始），补齐成 设备ID_通道号 */
    private String normalizeChannelId(String protocolType, String deviceId, String channelId) {
        String id = channelId == null ? "" : channelId.trim();
        if (id.isEmpty()) {
            return null;
        }
        if (AlarmConstants.PROTOCOL_ISUP.equals(protocolType) && id.matches("\\d+")) {
            return deviceId + "_" + id;
        }
        return id;
    }

    /** 设备名称回查，自定义名优先、设备名兜底，取值顺序放 Java 侧避免库间差异 */
    private String resolveDeviceName(String deviceId) {
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        try {
            List<AlarmDeviceRef> refs = alarmDeviceRefMapper.selectList(
                    new LambdaQueryWrapper<AlarmDeviceRef>()
                            .select(AlarmDeviceRef::getName, AlarmDeviceRef::getCustomName)
                            .eq(AlarmDeviceRef::getDeviceId, deviceId));
            if (refs == null) {
                return null;
            }
            for (AlarmDeviceRef ref : refs) {
                if (StringUtils.isNotBlank(ref.getCustomName())) {
                    return ref.getCustomName();
                }
                if (StringUtils.isNotBlank(ref.getName())) {
                    return ref.getName();
                }
            }
            return null;
        } catch (Exception e) {
            log.debug("[统一报警] 设备名称回查失败 deviceId={}", deviceId, e);
            return null;
        }
    }

    /** 通道名称：国标名优先、通道名兜底，按 device_id 匹配（该列存的是通道编码） */
    private String resolveChannelName(String channelId) {
        if (StringUtils.isBlank(channelId)) {
            return null;
        }
        try {
            List<AlarmChannelRef> refs = alarmChannelRefMapper.selectList(
                    new LambdaQueryWrapper<AlarmChannelRef>()
                            .select(AlarmChannelRef::getGbName, AlarmChannelRef::getName)
                            .eq(AlarmChannelRef::getDeviceId, channelId));
            if (refs == null) {
                return null;
            }
            for (AlarmChannelRef ref : refs) {
                if (StringUtils.isNotBlank(ref.getGbName())) {
                    return ref.getGbName();
                }
                if (StringUtils.isNotBlank(ref.getName())) {
                    return ref.getName();
                }
            }
            return null;
        } catch (Exception e) {
            log.debug("[统一报警] 通道名称回查失败 channelId={}", channelId, e);
            return null;
        }
    }

    private String firstNotBlank(String first, String second) {
        return StringUtils.isNotBlank(first) ? first : (StringUtils.isNotBlank(second) ? second : null);
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.length() > maxLength ? trimmed.substring(0, maxLength) : trimmed;
    }
}
