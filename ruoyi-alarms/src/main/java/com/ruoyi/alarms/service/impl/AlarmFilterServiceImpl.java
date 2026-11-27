package com.ruoyi.alarms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.alarms.domain.ref.AlarmFilterItem;
import com.ruoyi.alarms.mapper.AlarmFilterItemMapper;
import com.ruoyi.alarms.service.IAlarmFilterService;
import com.ruoyi.alarms.support.AlarmFilterMatcher;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DictUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/** 报警过滤规则维护实现，写入后同步失效字典缓存与规则快照 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmFilterServiceImpl implements IAlarmFilterService {

    private final AlarmFilterItemMapper alarmFilterItemMapper;
    private final AlarmFilterMatcher alarmFilterMatcher;

    @Override
    public List<AlarmFilterItem> listRules(String dictType) {
        return alarmFilterItemMapper.selectList(new LambdaQueryWrapper<AlarmFilterItem>()
                .eq(AlarmFilterItem::getDictType, checkType(dictType))
                .orderByAsc(AlarmFilterItem::getDictSort)
                .orderByAsc(AlarmFilterItem::getDictCode));
    }

    @Override
    public int addRule(AlarmFilterItem item) {
        if (item == null) {
            throw new ServiceException("过滤规则不能为空");
        }
        String dictType = checkType(item.getDictType());
        String value = item.getDictValue() == null ? "" : item.getDictValue().trim();
        if (value.isEmpty()) {
            throw new ServiceException("报警原始码不能为空");
        }
        if (value.length() > 100) {
            throw new ServiceException("报警原始码长度不能超过 100 个字符");
        }
        // 判重只在同类型内进行，国标与 ISUP 可以各自维护同名原始码
        Long exists = alarmFilterItemMapper.selectCount(new LambdaQueryWrapper<AlarmFilterItem>()
                .eq(AlarmFilterItem::getDictType, dictType)
                .eq(AlarmFilterItem::getDictValue, value));
        if (exists != null && exists > 0) {
            throw new ServiceException("原始码 " + value + " 已在当前类型的过滤规则中");
        }

        AlarmFilterItem record = new AlarmFilterItem();
        record.setDictType(dictType);
        record.setDictValue(value);
        // 字典标签必填，未填说明时直接复用原始码，便于列表展示
        record.setDictLabel(StringUtils.isBlank(item.getDictLabel()) ? value : item.getDictLabel().trim());
        record.setDictSort(item.getDictSort() == null ? 0L : item.getDictSort());
        record.setStatus("0");
        record.setRemark(StringUtils.isBlank(item.getRemark()) ? null : item.getRemark().trim());
        record.setCreateTime(new Date());
        int rows = alarmFilterItemMapper.insert(record);
        refreshCache(dictType);
        return rows;
    }

    @Override
    public int deleteRules(String dictType, Long[] dictCodes) {
        if (dictCodes == null || dictCodes.length == 0) {
            return 0;
        }
        String type = checkType(dictType);
        // 带上字典类型条件，避免误删另一个协议类型的规则
        int rows = alarmFilterItemMapper.delete(new LambdaQueryWrapper<AlarmFilterItem>()
                .in(AlarmFilterItem::getDictCode, Arrays.asList(dictCodes))
                .eq(AlarmFilterItem::getDictType, type));
        refreshCache(type);
        return rows;
    }

    /** 只允许操作已支持的过滤类型，防止借接口改到其它字典 */
    private String checkType(String dictType) {
        String type = dictType == null ? AlarmFilterMatcher.DICT_TYPE_GB : dictType.trim();
        if (!AlarmFilterMatcher.isSupportedType(type)) {
            throw new ServiceException("不支持的报警过滤类型：" + dictType);
        }
        return type;
    }

    /** 规则变更后立刻生效：清字典缓存 + 置空快照加载时间 */
    private void refreshCache(String dictType) {
        alarmFilterMatcher.refresh();
        try {
            DictUtils.removeDictCache(dictType);
        } catch (Exception e) {
            // Redis 不可用不影响过滤，快照重建走的是数据库
            log.debug("[报警过滤] 字典缓存清理失败", e);
        }
    }
}
