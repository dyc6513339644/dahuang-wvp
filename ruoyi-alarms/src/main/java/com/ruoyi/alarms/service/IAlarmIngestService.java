package com.ruoyi.alarms.service;

import com.ruoyi.alarms.domain.dto.AlarmMsg;

import java.util.List;

/** 统一报警入库服务，各协议只翻译成 AlarmMsg，归一化与落库在此统一处理 */
public interface IAlarmIngestService {

    /** 报警入库入口，队列开启则投递即返回，投递失败同步直写 */
    Long ingest(AlarmMsg msg);

    /** 直接写入 {@code wvp_alarms}（供原始表消费线程与同步直写路径调用，不经过队列） */
    boolean ingestDirect(AlarmMsg msg);

    /** 批量入库，整批失败回退逐条写，避免单条脏数据拖垮整批 */
    boolean[] ingestBatch(List<AlarmMsg> msgs);
}
