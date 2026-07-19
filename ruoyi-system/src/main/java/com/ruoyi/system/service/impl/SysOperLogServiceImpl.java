package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysOperLog;
import com.ruoyi.system.mapper.SysOperLogMapper;
import com.ruoyi.system.service.ISysOperLogService;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLog.setOperTime(new Date());
        operLogMapper.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        QueryWrapper<SysOperLog> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(operLog.getOperIp()))
        {
            wrapper.like("oper_ip", operLog.getOperIp());
        }
        if (StringUtils.isNotEmpty(operLog.getTitle()))
        {
            wrapper.like("title", operLog.getTitle());
        }
        if (operLog.getBusinessType() != null)
        {
            wrapper.eq("business_type", operLog.getBusinessType());
        }
        if (operLog.getBusinessTypes() != null && operLog.getBusinessTypes().length > 0)
        {
            wrapper.in("business_type", operLog.getBusinessTypes());
        }
        if (operLog.getStatus() != null)
        {
            wrapper.eq("status", operLog.getStatus());
        }
        if (StringUtils.isNotEmpty(operLog.getOperName()))
        {
            wrapper.like("oper_name", operLog.getOperName());
        }
        if (operLog.getParams() != null)
        {
            if (StringUtils.isNotEmpty((String) operLog.getParams().get("beginTime")))
            {
                wrapper.ge("oper_time", operLog.getParams().get("beginTime"));
            }
            if (StringUtils.isNotEmpty((String) operLog.getParams().get("endTime")))
            {
                wrapper.le("oper_time", operLog.getParams().get("endTime"));
            }
        }
        wrapper.orderByDesc("oper_id");
        return operLogMapper.selectList(wrapper);
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.delete(new QueryWrapper<>());
    }
}
