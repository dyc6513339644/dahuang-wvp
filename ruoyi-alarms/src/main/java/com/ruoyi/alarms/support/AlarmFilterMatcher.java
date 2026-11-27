package com.ruoyi.alarms.support;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.domain.dto.AlarmMsg;
import com.ruoyi.alarms.domain.ref.AlarmFilterItem;
import com.ruoyi.alarms.mapper.AlarmFilterItemMapper;
import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 报警过滤：命中字典原始码的报警不再入库。<p>
 * 规则按协议分类存放，互不干扰：国标走 alarm_filter，海康 ISUP 走 isup_alarm_filter，后续新增协议往 SUPPORTED 里追加即可。<br>
 * 规则走进程内快照，5 秒自动过期重载；维护接口写入后会主动刷新，改完立即生效。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmFilterMatcher {

    /** 国标报警过滤规则字典类型 */
    public static final String DICT_TYPE_GB = "alarm_filter";

    /** 海康 ISUP 报警过滤规则字典类型 */
    public static final String DICT_TYPE_ISUP = "isup_alarm_filter";

    /** 已支持的过滤类型，新增协议在这里追加 */
    private static final List<String> SUPPORTED_TYPES =
            Collections.unmodifiableList(Arrays.asList(DICT_TYPE_GB, DICT_TYPE_ISUP));

    /** 规则快照有效期，过期后下次匹配时重载 */
    private static final long REFRESH_MS = 5_000L;

    private final AlarmFilterItemMapper alarmFilterItemMapper;

    private final ReentrantLock loadLock = new ReentrantLock();

    /** 当前生效的规则集合，key 为字典类型，value 为该类型的原始码集合（已去空格转小写） */
    private volatile Map<String, Set<String>> rules = Collections.emptyMap();

    /** 快照加载时间 */
    private volatile long loadedAt = 0L;

    /** 各类型累计过滤条数，供监控接口查看 */
    private final Map<String, AtomicLong> filteredCounters = new ConcurrentHashMap<>();

    /** 判断该条报警是否应被丢弃，按报警协议选择对应的规则集 */
    public boolean isFiltered(AlarmMsg msg) {
        if (msg == null) {
            return false;
        }
        String dictType = resolveDictType(msg.getProtocolType());
        Set<String> snapshot = currentRules().get(dictType);
        if (snapshot == null || snapshot.isEmpty()) {
            return false;
        }
        for (String candidate : candidates(msg)) {
            if (snapshot.contains(candidate)) {
                counter(dictType).incrementAndGet();
                if (log.isDebugEnabled()) {
                    log.debug("[报警过滤] 命中 {} 规则 {}，跳过入库 protocol={}, deviceId={}, alarmCode={}",
                            dictType, candidate, msg.getProtocolType(), msg.getDeviceId(), msg.getAlarmCode());
                }
                return true;
            }
        }
        return false;
    }

    /** 规则变更后调用，下一轮匹配立即重载 */
    public void refresh() {
        loadedAt = 0L;
    }

    /** 过滤监控数据：各类型生效规则数与累计拦截条数 */
    public Map<String, Object> stats() {
        Map<String, Object> types = new LinkedHashMap<>(4);
        long total = 0L;
        for (String dictType : SUPPORTED_TYPES) {
            Set<String> snapshot = rules.get(dictType);
            long filtered = counter(dictType).get();
            total += filtered;
            Map<String, Object> item = new LinkedHashMap<>(4);
            item.put("dictType", dictType);
            item.put("ruleCount", snapshot == null ? 0 : snapshot.size());
            item.put("filtered", filtered);
            types.put(dictType, item);
        }
        Map<String, Object> stat = new LinkedHashMap<>(4);
        stat.put("types", types);
        stat.put("filtered", total);
        return stat;
    }

    /** 该字典类型是否为已支持的过滤类型 */
    public static boolean isSupportedType(String dictType) {
        return dictType != null && SUPPORTED_TYPES.contains(dictType.trim());
    }

    /** 按协议取过滤类型：海康走 isup_alarm_filter，其余（国标/ONVIF/推拉流等）统一走 alarm_filter */
    public static String resolveDictType(String protocolType) {
        if (protocolType != null && AlarmConstants.PROTOCOL_ISUP.equalsIgnoreCase(protocolType.trim())) {
            return DICT_TYPE_ISUP;
        }
        return DICT_TYPE_GB;
    }

    private AtomicLong counter(String dictType) {
        return filteredCounters.computeIfAbsent(dictType, key -> new AtomicLong());
    }

    /** 取当前规则快照，过期则加锁重载（抢占失败的线程直接沿用旧快照，不阻塞协议线程） */
    private Map<String, Set<String>> currentRules() {
        if (System.currentTimeMillis() - loadedAt <= REFRESH_MS) {
            return rules;
        }
        if (loadLock.tryLock()) {
            try {
                if (System.currentTimeMillis() - loadedAt > REFRESH_MS) {
                    load();
                }
            } finally {
                loadLock.unlock();
            }
        }
        return rules;
    }

    private void load() {
        try {
            List<AlarmFilterItem> items = alarmFilterItemMapper.selectList(
                    new LambdaQueryWrapper<AlarmFilterItem>()
                            .select(AlarmFilterItem::getDictType, AlarmFilterItem::getDictValue)
                            .in(AlarmFilterItem::getDictType, SUPPORTED_TYPES)
                            .eq(AlarmFilterItem::getStatus, "0"));
            Map<String, Set<String>> loaded = new HashMap<>(4);
            if (items != null) {
                for (AlarmFilterItem item : items) {
                    String dictType = item.getDictType();
                    String value = normalize(item.getDictValue());
                    if (dictType == null || value == null) {
                        continue;
                    }
                    loaded.computeIfAbsent(dictType.trim(), key -> new HashSet<>()).add(value);
                }
            }
            // 冻结集合，避免匹配线程读到半成品
            Map<String, Set<String>> snapshot = new HashMap<>(4);
            for (Map.Entry<String, Set<String>> entry : loaded.entrySet()) {
                snapshot.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));
            }
            rules = snapshot;
            if (log.isDebugEnabled()) {
                log.debug("[报警过滤] 规则加载完成，国标 {} 条，ISUP {} 条",
                        size(snapshot, DICT_TYPE_GB), size(snapshot, DICT_TYPE_ISUP));
            }
        } catch (Exception e) {
            // 加载失败保留旧快照，避免过滤功能因一次抖动整体失效
            log.warn("[报警过滤] 规则加载失败，沿用上一份快照", e);
        } finally {
            // 失败也推迟下一次重试，防止每条报警都去查库
            loadedAt = System.currentTimeMillis();
        }
    }

    private int size(Map<String, Set<String>> snapshot, String dictType) {
        Set<String> values = snapshot.get(dictType);
        return values == null ? 0 : values.size();
    }

    /** 参与匹配的候选值：原始码、事件类型、国标「报警方式-报警类型」 */
    private List<String> candidates(AlarmMsg msg) {
        List<String> keys = new ArrayList<>(3);
        addCandidate(keys, msg.getAlarmCode());
        addCandidate(keys, msg.getEventType());
        String method = normalize(msg.getAlarmMethod());
        String code = normalize(msg.getAlarmCode());
        if (method != null && code != null) {
            keys.add(method + "-" + code);
        }
        return keys;
    }

    private void addCandidate(List<String> keys, String value) {
        String normalized = normalize(value);
        if (normalized != null) {
            keys.add(normalized);
        }
    }

    private String normalize(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
