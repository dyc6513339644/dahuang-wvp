package com.ruoyi.alarms.service;

import com.ruoyi.alarms.domain.ref.AlarmFilterItem;

import java.util.List;

/**
 * 报警过滤规则维护，规则落在字典表 sys_dict_data。
 * 国标用 dict_type = alarm_filter，海康 ISUP 用 isup_alarm_filter，两套规则互不影响。
 */
public interface IAlarmFilterService {

    /** 指定类型的规则列表，按排序号升序 */
    List<AlarmFilterItem> listRules(String dictType);

    /** 新增规则，原始码在同一类型内重复时抛业务异常 */
    int addRule(AlarmFilterItem item);

    /** 删除指定类型的规则，跨类型不允许删 */
    int deleteRules(String dictType, Long[] dictCodes);
}
