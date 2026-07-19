package com.ruoyi.wvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wvp.service.bean.RecordPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RecordPlanMapper extends BaseMapper<RecordPlan> {

    /**
     * 查询当前时间点正在执行的录像计划通道
     */
    List<Integer> queryRecordIng(@Param("week") int week, @Param("index") int index);
}
