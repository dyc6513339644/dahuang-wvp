package com.ruoyi.alarms.support;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 报警字典读取：名称、级别/状态颜色、下拉项全部以字典（sys_dict_type / sys_dict_data）为准。
 * <p>
 * 字典随建库脚本内置（见 sql/dict 下的 alarm_* 增量脚本），代码不再维护一份名称副本：
 * 取值失败只回显原编码，不做中文兜底。按 dictType 做 60 秒本地缓存，
 * 避免列表每行多个名称字段都打一次 Redis。
 */
@Slf4j
public final class AlarmDictUtils {

    /** 本地缓存有效期（毫秒）：字典改动最多 60 秒后生效 */
    private static final long CACHE_TTL_MS = 60_000L;

    /** 字典类型数量固定（当前 4 个），key 不会无限增长 */
    private static final Map<String, Snapshot> CACHE = new ConcurrentHashMap<>(8);

    private AlarmDictUtils() {
    }

    /** 取字典标签；字典里没有该键值时原样返回键值（便于发现未维护的编码） */
    public static String label(String dictType, String value) {
        if (StringUtils.isBlank(value)) {
            return "";
        }
        String label = snapshot(dictType).labels.get(value);
        return StringUtils.isBlank(label) ? value : label;
    }

    /** 取字典标签（数值型键值） */
    public static String label(String dictType, Integer value) {
        return value == null ? "" : label(dictType, String.valueOf(value));
    }

    /** 取启用的字典项（按 dict_sort 升序） */
    public static List<SysDictData> items(String dictType) {
        return snapshot(dictType).items;
    }

    /** 清空本地缓存，字典改动后需要立即生效时调用（正常 60 秒自动过期，无需手动） */
    public static void clearCache() {
        CACHE.clear();
    }

    // ---------- 内部

    private static Snapshot snapshot(String dictType) {
        long now = System.currentTimeMillis();
        Snapshot cached = CACHE.get(dictType);
        if (cached != null && cached.expireAt > now) {
            return cached;
        }
        Snapshot fresh = load(dictType, now);
        CACHE.put(dictType, fresh);
        return fresh;
    }

    private static Snapshot load(String dictType, long now) {
        List<SysDictData> cached;
        try {
            cached = DictUtils.getDictCache(dictType);
        } catch (Exception e) {
            // Redis 未就绪 / 非 Spring 环境（单元测试）：按空字典缓存，避免每次调用都抛异常
            log.debug("[统一报警] 读取字典失败 dictType={}, err={}", dictType, e.getMessage());
            cached = null;
        }
        if (cached == null || cached.isEmpty()) {
            return new Snapshot(Collections.emptyList(), Collections.emptyMap(), now + CACHE_TTL_MS);
        }

        List<SysDictData> items = new ArrayList<>(cached.size());
        Map<String, String> labels = new LinkedHashMap<>(cached.size());
        for (SysDictData item : cached) {
            if (item == null || StringUtils.isBlank(item.getDictValue())) {
                continue;
            }
            // 只认启用项：字典里停用的键值视同未配置
            if (item.getStatus() != null && !"0".equals(item.getStatus())) {
                continue;
            }
            items.add(item);
            labels.put(item.getDictValue(), item.getDictLabel());
        }
        items.sort(Comparator.comparing(SysDictData::getDictSort,
                Comparator.nullsLast(Long::compareTo)));
        return new Snapshot(Collections.unmodifiableList(items),
                Collections.unmodifiableMap(labels), now + CACHE_TTL_MS);
    }

    /** 单个字典类型的快照：有序列表 + 值到名称的索引 */
    private static final class Snapshot {

        private final List<SysDictData> items;
        private final Map<String, String> labels;
        private final long expireAt;

        private Snapshot(List<SysDictData> items, Map<String, String> labels, long expireAt) {
            this.items = items;
            this.labels = labels;
            this.expireAt = expireAt;
        }
    }
}
