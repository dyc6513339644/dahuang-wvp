package com.ruoyi.wvp.service;

import com.github.pagehelper.PageInfo;
import com.ruoyi.wvp.gb28181.bean.CommonGBChannel;
import com.ruoyi.wvp.service.bean.RecordPlan;

import java.util.List;

public interface IRecordPlanService {


    RecordPlan get(Long planId);

    void update(RecordPlan plan);

    void delete(Long planId);

    List<RecordPlan> query(Integer pageNum, Integer pageSize, String query);

    void add(RecordPlan plan);

    void link(List<Integer> channelIds, Long planId);

    List<CommonGBChannel> queryChannelList(int pageNum, int pageSize, String query, Integer channelType, Boolean online, Long planId, Boolean hasLink);

    void linkAll(Long planId);

    void cleanAll(Long planId);

    Integer recording(String app, String stream);
}
