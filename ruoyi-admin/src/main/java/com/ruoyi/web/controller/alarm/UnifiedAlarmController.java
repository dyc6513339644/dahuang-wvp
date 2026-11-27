package com.ruoyi.web.controller.alarm;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.alarms.domain.AlarmRecord;
import com.ruoyi.alarms.domain.bo.AlarmCleanBo;
import com.ruoyi.alarms.domain.bo.AlarmHandleBo;
import com.ruoyi.alarms.domain.ref.AlarmFilterItem;
import com.ruoyi.alarms.domain.vo.AlarmQueryVo;
import com.ruoyi.alarms.service.IAlarmFilterService;
import com.ruoyi.alarms.service.IAlarmQueueService;
import com.ruoyi.alarms.service.IAlarmRawService;
import com.ruoyi.alarms.service.IAlarmRecordService;
import com.ruoyi.alarms.support.AlarmFilterMatcher;
import com.ruoyi.alarms.task.AlarmStreamConsumer;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一报警接口：对外只暴露一套查询/处理/清理/过滤，国标与海康 ISUP 报警同入库、同结构。
 * <p>
 * 与统一录像（{@code /api/unified/record}）、统一设备（{@code /api/unified/device}）保持同一前缀，
 * 前端不再区分协议调用不同接口：
 * <ul>
 *   <li>国标 GB28181 —— Notify 报警经 {@code GbAlarmIngestBridge} 归一后入库</li>
 *   <li>海康 ISUP —— SDK 报警回调经 {@code EHomeMsgCallBack} 归一后入库</li>
 * </ul>
 * 协议类型、报警类型、级别、处理状态的中文名由字典下发（{@code protocol_type} / {@code alarm_type} /
 * {@code alarm_level} / {@code alarm_handle_status}），实体 getter 直接带出，无需前端再翻译。
 * <p>
 * 分页列表返回平铺结构（{@code total / pageNum / pageSize / records[]}），与统一录像查询的
 * {@code records[]} 保持一致。
 * <p>
 * 过滤规则（{@code /filter/*}）也在本类内：规则落在字典表，{@code alarm_filter} 为国标、
 * {@code isup_alarm_filter} 为海康 ISUP，命中原始码的报警不入库、不取图。
 */
@RestController
@RequestMapping("/api/unified/alarm")
@RequiredArgsConstructor
public class UnifiedAlarmController extends BaseController {

    private final IAlarmRecordService alarmRecordService;
    private final IAlarmRawService alarmRawService;
    private final IAlarmQueueService alarmQueueService;
    private final AlarmStreamConsumer alarmStreamConsumer;
    private final IAlarmFilterService alarmFilterService;
    private final AlarmFilterMatcher alarmFilterMatcher;

    // ==================== 队列与链路监控 ====================

    /** 报警队列监控：Stream 长度、pending 积压与投递/消费计数 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:list')")
    @GetMapping("/queue/stat")
    public AjaxResult queueStat() {
        Map<String, Object> stat = new LinkedHashMap<>(alarmQueueService.stats());
        stat.put("consumer", alarmStreamConsumer.counters());
        return success(stat);
    }

    /** 原始数据中间表监控：表内待处理/处理中/失败行数（死信与遗留数据） */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:list')")
    @GetMapping("/raw/stat")
    public AjaxResult rawStat() {
        return success(alarmRawService.stats());
    }

    // ==================== 查询 ====================

    /**
     * 分页查询报警。
     *
     * @param queryVo 支持 protocolType / alarmType / alarmLevel / handleStatus / deviceId /
     *                channelId / keyword / startTime / endTime / orderBy 等条件
     * @return 平铺结构：total / pageNum / pageSize / records[]，
     *         records 内每条带 protocolName / alarmTypeName / alarmLevelName / handleStatusName
     */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:list')")
    @GetMapping("/list")
    public AjaxResult list(AlarmQueryVo queryVo) {
        IPage<AlarmRecord> page = alarmRecordService.selectPage(queryVo);
        AjaxResult result = success();
        result.put("total", page.getTotal());
        result.put("pageNum", page.getCurrent());
        result.put("pageSize", page.getSize());
        result.put("records", page.getRecords());
        return result;
    }

    /** 报警类型筛选项，数据来自字典 alarm_type（新增类型只需维护字典） */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:list')")
    @GetMapping("/types")
    public AjaxResult types() {
        List<Map<String, Object>> types = alarmRecordService.listTypes();
        return success(types);
    }

    /** 报警详情 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return success(alarmRecordService.selectRecordById(id));
    }

    // ==================== 维护 ====================

    /** 手工新增报警（调试/补录），协议与类型不传时按代码规则归一 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:add')")
    @Log(title = "统一报警", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmRecord record) {
        return toAjax(alarmRecordService.insertRecord(record));
    }

    /** 修改报警（描述/类型/级别/备注） */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:handle')")
    @Log(title = "统一报警", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AlarmRecord record) {
        return toAjax(alarmRecordService.updateRecord(record));
    }

    /** 标记处理/忽略（支持批量） */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:handle')")
    @Log(title = "统一报警", businessType = BusinessType.UPDATE)
    @PutMapping("/handle")
    public AjaxResult handle(@Validated @RequestBody AlarmHandleBo bo) {
        return toAjax(alarmRecordService.handle(bo, getUsername()));
    }

    /** 按时间清理报警（可限定协议与处理状态） */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:clean')")
    @Log(title = "统一报警", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean(@Validated @RequestBody AlarmCleanBo bo) {
        return success(alarmRecordService.clean(bo));
    }

    /** 批量删除报警 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:remove')")
    @Log(title = "统一报警", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(alarmRecordService.deleteByIds(ids));
    }

    // ==================== 过滤规则 ====================
    // 规则存字典表：alarm_filter 管国标，isup_alarm_filter 管海康 ISUP，两套互不影响。
    // 命中原始码的报警在入库前就被丢弃，既不落库也不触发抓图。

    /** 过滤规则列表，默认返回国标规则 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:filter')")
    @GetMapping("/filter/list")
    public AjaxResult filterList(@RequestParam(value = "dictType", required = false,
            defaultValue = AlarmFilterMatcher.DICT_TYPE_GB) String dictType) {
        return success(alarmFilterService.listRules(dictType));
    }

    /** 新增过滤规则，字典类型可由查询参数或请求体指定，缺省为国标 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:filter')")
    @Log(title = "报警过滤", businessType = BusinessType.INSERT)
    @PostMapping("/filter")
    public AjaxResult filterAdd(@RequestParam(value = "dictType", required = false) String dictType,
                                @RequestBody AlarmFilterItem item) {
        item.setDictType(dictType != null ? dictType : item.getDictType());
        return toAjax(alarmFilterService.addRule(item));
    }

    /** 删除过滤规则，带类型条件，不会跨协议误删 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:filter')")
    @Log(title = "报警过滤", businessType = BusinessType.DELETE)
    @DeleteMapping("/filter/{dictCodes}")
    public AjaxResult filterRemove(@PathVariable Long[] dictCodes,
                                   @RequestParam(value = "dictType", required = false,
                                           defaultValue = AlarmFilterMatcher.DICT_TYPE_GB) String dictType) {
        return toAjax(alarmFilterService.deleteRules(dictType, dictCodes));
    }

    /** 过滤运行状态：各类型生效规则数与累计拦截条数 */
    @PreAuthorize("@ss.hasPermi('wvp:alarms:list')")
    @GetMapping("/filter/stat")
    public AjaxResult filterStat() {
        return success(alarmFilterMatcher.stats());
    }
}
