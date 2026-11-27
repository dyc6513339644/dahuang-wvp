package com.ruoyi.alarms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.alarms.domain.AlarmRaw;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 报警原始表 Mapper，能用 MP 的全走 Wrapper，XML 只留三条 MP 表达不了的语句 */
public interface AlarmRawMapper extends BaseMapper<AlarmRaw> {

    /** 批量插入（多值 VALUES，MySQL / PostgreSQL / 达梦 / SQLite 均支持） */
    int insertBatch(@Param("list") List<AlarmRaw> list);

    /** 处理失败：重试次数 +1，未达上限退回待处理，达到上限置失败态 */
    int releaseFailed(@Param("id") Long id, @Param("error") String error, @Param("maxRetry") int maxRetry);

    /** 解锁超时未完成的行（消费进程崩溃/重启场景） */
    int unlockExpired(@Param("before") String before);
}
