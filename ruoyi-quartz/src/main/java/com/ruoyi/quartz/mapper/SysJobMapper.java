package com.ruoyi.quartz.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.quartz.domain.SysJob;

/**
 * 调度任务信息 数据层
 * 
 * @author ruoyi
 */
public interface SysJobMapper extends BaseMapper<SysJob>
{
    /**
     * 查询调度任务列表（动态条件）
     */
    List<SysJob> selectJobList(SysJob job);
}
