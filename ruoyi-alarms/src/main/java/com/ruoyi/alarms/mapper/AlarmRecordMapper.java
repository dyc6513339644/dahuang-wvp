package com.ruoyi.alarms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.alarms.domain.AlarmRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 统一报警 Mapper，SQL 在 AlarmRecordMapper.xml，接口只声明方法 */
public interface AlarmRecordMapper extends BaseMapper<AlarmRecord> {

    /** 批量插入，一批报警合并成一次多值 VALUES，是应对报警风暴的关键 */
    int insertBatch(@Param("list") List<AlarmRecord> list);
}
