package com.ruoyi.alarms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.alarms.domain.AlarmRecord;
import com.ruoyi.alarms.domain.bo.AlarmCleanBo;
import com.ruoyi.alarms.domain.bo.AlarmHandleBo;
import com.ruoyi.alarms.domain.vo.AlarmQueryVo;

import java.util.List;
import java.util.Map;

/** 报警信息管理服务 */
public interface IAlarmRecordService {

    /** 分页查询报警 */
    IPage<AlarmRecord> selectPage(AlarmQueryVo queryVo);

    /** 查询报警详情 */
    AlarmRecord selectRecordById(Long id);

    /** 手工新增报警（调试/补录） */
    int insertRecord(AlarmRecord record);

    /** 修改报警（仅允许改描述/类型/级别等非来源字段） */
    int updateRecord(AlarmRecord record);

    /** 标记处理/忽略 */
    int handle(AlarmHandleBo bo, String username);

    /** 删除报警 */
    int deleteByIds(Long[] ids);

    /** 按条件清理报警 */
    int clean(AlarmCleanBo bo);

    /** 归一化报警类型字典（来源 sys_dict_data 的 alarm_type，未配置时回退枚举） */
    List<Map<String, Object>> listTypes();
}
