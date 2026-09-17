package com.ruoyi.wvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.wvp.service.bean.CloudRecordItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CloudRecordServiceMapper extends BaseMapper<CloudRecordItem> {

    /**
     * 动态查询云端录像列表
     */
    List<CloudRecordItem> getList(@Param("query") String query, @Param("app") String app,
                                  @Param("stream") String stream,
                                  @Param("startTimeStamp") Long startTimeStamp,
                                  @Param("endTimeStamp") Long endTimeStamp,
                                  @Param("callId") String callId,
                                  @Param("mediaServerItemList") List<MediaServer> mediaServerItemList,
                                  @Param("ids") List<Integer> ids);

    /**
     * 查询文件路径列表
     */
    List<String> queryRecordFilePathList(@Param("app") String app, @Param("stream") String stream,
                                  @Param("startTimeStamp") Long startTimeStamp,
                                  @Param("endTimeStamp") Long endTimeStamp,
                                  @Param("callId") String callId,
                                  @Param("mediaServerItemList") List<MediaServer> mediaServerItemList);

    /**
     * 批量更新收藏状态
     */
    int updateCollectList(@Param("collect") boolean collect,
                          @Param("cloudRecordItemList") List<CloudRecordItem> cloudRecordItemList);

    /**
     * 按文件路径批量删除
     */
    void deleteByFileList(@Param("filePathList") List<String> filePathList,
                          @Param("mediaServerId") String mediaServerId);

    /**
     * 按ID列表批量删除
     */
    int deleteList(@Param("cloudRecordItemIdList") List<CloudRecordItem> cloudRecordItemIdList);

    /**
     * 查询待清理的过期录像（未收藏 + 到期时间前）
     */
    List<CloudRecordItem> queryRecordListForDelete(@Param("endTimeStamp") Long endTimeStamp,
                                                    @Param("mediaServerId") String mediaServerId);
}
