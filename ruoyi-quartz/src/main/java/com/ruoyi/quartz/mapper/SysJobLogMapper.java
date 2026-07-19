package com.ruoyi.quartz.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.quartz.domain.SysJobLog;

/**
 * 调度任务日志信息 数据层
 * 
 * @author ruoyi
 */
public interface SysJobLogMapper extends BaseMapper<SysJobLog>
{
    /**
     * 获取调度日志列表（动态条件）
     */
    List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    /**
     * 清空任务日志（TRUNCATE TABLE）
     */
    void cleanJobLog();
}
