package com.ruoyi.alarms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.alarms.constant.AlarmConstants;
import com.ruoyi.alarms.constant.AlarmDictType;
import com.ruoyi.alarms.constant.AlarmType;
import com.ruoyi.alarms.domain.AlarmRecord;
import com.ruoyi.alarms.domain.bo.AlarmCleanBo;
import com.ruoyi.alarms.domain.bo.AlarmHandleBo;
import com.ruoyi.alarms.domain.vo.AlarmQueryVo;
import com.ruoyi.alarms.mapper.AlarmRecordMapper;
import com.ruoyi.alarms.service.IAlarmRecordService;
import com.ruoyi.alarms.support.AlarmDictUtils;
import com.ruoyi.alarms.support.AlarmTypeResolver;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 报警信息管理服务实现（MyBatis-Plus 增删改查） */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmRecordServiceImpl implements IAlarmRecordService {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final AlarmRecordMapper alarmRecordMapper;

    @Override
    public IPage<AlarmRecord> selectPage(AlarmQueryVo queryVo) {
        AlarmQueryVo vo = queryVo == null ? new AlarmQueryVo() : queryVo;
        long pageNum = vo.getPageNum() == null || vo.getPageNum() < 1 ? 1 : vo.getPageNum();
        long pageSize = vo.getPageSize() == null || vo.getPageSize() < 1 ? 15 : vo.getPageSize();

        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(vo.getProtocolType()), AlarmRecord::getProtocolType, vo.getProtocolType());
        wrapper.eq(StringUtils.isNotBlank(vo.getDeviceId()), AlarmRecord::getDeviceId, vo.getDeviceId());
        wrapper.eq(StringUtils.isNotBlank(vo.getChannelId()), AlarmRecord::getChannelId, vo.getChannelId());
        wrapper.eq(StringUtils.isNotBlank(vo.getAlarmType()), AlarmRecord::getAlarmType, vo.getAlarmType());
        wrapper.eq(vo.getAlarmLevel() != null, AlarmRecord::getAlarmLevel, vo.getAlarmLevel());
        wrapper.eq(vo.getHandleStatus() != null, AlarmRecord::getHandleStatus, vo.getHandleStatus());
        wrapper.ge(StringUtils.isNotBlank(vo.getStartTime()), AlarmRecord::getAlarmTime, vo.getStartTime());
        wrapper.le(StringUtils.isNotBlank(vo.getEndTime()), AlarmRecord::getAlarmTime, vo.getEndTime());

        // 关键字：设备编号 / 设备名称 / 通道编号 / 通道名称
        if (StringUtils.isNotBlank(vo.getKeyword())) {
            String keyword = vo.getKeyword().trim();
            wrapper.and(w -> w.like(AlarmRecord::getDeviceId, keyword)
                    .or().like(AlarmRecord::getDeviceName, keyword)
                    .or().like(AlarmRecord::getChannelId, keyword)
                    .or().like(AlarmRecord::getChannelName, keyword));
        }

        // 排序：只允许白名单字段，避免注入
        String orderBy = vo.getOrderBy();
        if ("create_time".equalsIgnoreCase(orderBy)) {
            wrapper.orderByDesc(AlarmRecord::getCreateTime);
        } else if ("alarm_level".equalsIgnoreCase(orderBy)) {
            wrapper.orderByAsc(AlarmRecord::getAlarmLevel);
        } else {
            wrapper.orderByDesc(AlarmRecord::getAlarmTime);
        }
        wrapper.orderByDesc(AlarmRecord::getId);

        return alarmRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public AlarmRecord selectRecordById(Long id) {
        if (id == null) {
            throw new ServiceException("报警ID不能为空");
        }
        AlarmRecord record = alarmRecordMapper.selectById(id);
        return record;
    }

    @Override
    public int insertRecord(AlarmRecord record) {
        if (record == null) {
            return 0;
        }
        if (StringUtils.isBlank(record.getProtocolType())) {
            record.setProtocolType(AlarmConstants.PROTOCOL_OTHER);
        }
        AlarmType type = AlarmTypeResolver.resolve(record.getProtocolType(), record.getAlarmCode(),
                record.getAlarmMethod(), null, record.getAlarmType());
        record.setAlarmType(type.getCode());
        // 名称快照写入字典中的当前名称，字典项后续改名不影响已入库快照
        record.setAlarmTypeName(AlarmDictUtils.label(AlarmDictType.ALARM_TYPE, type.getCode()));
        if (record.getAlarmLevel() == null) {
            record.setAlarmLevel(type.getDefaultLevel());
        }
        if (StringUtils.isBlank(record.getAlarmTime())) {
            record.setAlarmTime(DateUtils.getTime());
        }
        if (record.getHandleStatus() == null) {
            record.setHandleStatus(AlarmConstants.HANDLE_UNHANDLED);
        }
        return alarmRecordMapper.insert(record);
    }

    @Override
    public int updateRecord(AlarmRecord record) {
        if (record == null || record.getId() == null) {
            throw new ServiceException("报警ID不能为空");
        }
        selectRecordById(record.getId());
        // 只允许改人工可维护的字段，来源/设备/时间等由上报侧决定
        LambdaUpdateWrapper<AlarmRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AlarmRecord::getId, record.getId())
                .set(record.getAlarmDesc() != null, AlarmRecord::getAlarmDesc, record.getAlarmDesc())
                .set(record.getAlarmType() != null, AlarmRecord::getAlarmType, record.getAlarmType())
                // 名称快照跟随类型码重算：getAlarmTypeName() 已字典化，不能再用它判断「是否传了名称」
                .set(record.getAlarmType() != null, AlarmRecord::getAlarmTypeName,
                        record.getAlarmType() == null ? null
                                : AlarmDictUtils.label(AlarmDictType.ALARM_TYPE, record.getAlarmType()))
                .set(record.getAlarmLevel() != null, AlarmRecord::getAlarmLevel, record.getAlarmLevel())
                .set(record.getRemark() != null, AlarmRecord::getRemark, record.getRemark());
        return alarmRecordMapper.update(null, wrapper);
    }

    @Override
    public int handle(AlarmHandleBo bo, String username) {
        if (bo == null || bo.getIds() == null || bo.getIds().isEmpty()) {
            throw new ServiceException("请选择要处理的报警");
        }
        Integer status = bo.getHandleStatus();
        if (status == null || (status != AlarmConstants.HANDLE_HANDLED && status != AlarmConstants.HANDLE_IGNORED)) {
            throw new ServiceException("处理状态只能为已处理或已忽略");
        }
        LambdaUpdateWrapper<AlarmRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(AlarmRecord::getId, bo.getIds())
                .set(AlarmRecord::getHandleStatus, status)
                .set(AlarmRecord::getHandleBy, username)
                .set(AlarmRecord::getHandleTime, DateUtils.getTime())
                .set(AlarmRecord::getHandleRemark, StringUtils.substring(bo.getHandleRemark(), 0, 500));
        return alarmRecordMapper.update(null, wrapper);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            throw new ServiceException("请选择要删除的报警");
        }
        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AlarmRecord::getId, java.util.Arrays.asList(ids));
        return alarmRecordMapper.delete(wrapper);
    }

    @Override
    public int clean(AlarmCleanBo bo) {
        if (bo == null || StringUtils.isBlank(bo.getBeforeTime())) {
            throw new ServiceException("请选择截止时间");
        }
        checkTime(bo.getBeforeTime());
        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.le(AlarmRecord::getAlarmTime, bo.getBeforeTime());
        wrapper.eq(StringUtils.isNotBlank(bo.getProtocolType()), AlarmRecord::getProtocolType, bo.getProtocolType());
        wrapper.eq(bo.getHandleStatus() != null, AlarmRecord::getHandleStatus, bo.getHandleStatus());
        wrapper.eq(StringUtils.isNotBlank(bo.getAlarmType()), AlarmRecord::getAlarmType, bo.getAlarmType());
        return alarmRecordMapper.delete(wrapper);
    }

    @Override
    public List<Map<String, Object>> listTypes() {
        // 类型名称、排序、启停都以字典为准：新增类型只需在字典里加一项
        List<SysDictData> dict = AlarmDictUtils.items(AlarmDictType.ALARM_TYPE);
        List<Map<String, Object>> rows = new ArrayList<>(dict.size());
        for (SysDictData item : dict) {
            rows.add(typeOption(item.getDictValue(), item.getDictLabel(), item.getListClass()));
        }
        return rows;
    }

    /** 前端筛选项结构：code/name 保持原字段名，前端无需改动 */
    private Map<String, Object> typeOption(String code, String name, String listClass) {
        Map<String, Object> row = new LinkedHashMap<>(4);
        row.put("code", code);
        row.put("name", name);
        row.put("defaultLevel", AlarmType.ofCode(code).getDefaultLevel());
        row.put("listClass", listClass == null ? "" : listClass);
        return row;
    }

    private void checkTime(String time) {
        try {
            new SimpleDateFormat(TIME_PATTERN).parse(time);
        } catch (Exception e) {
            throw new ServiceException("时间格式为 " + TIME_PATTERN);
        }
    }
}
