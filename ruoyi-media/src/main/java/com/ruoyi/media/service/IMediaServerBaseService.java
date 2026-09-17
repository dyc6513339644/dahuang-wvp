package com.ruoyi.media.service;

import com.ruoyi.media.domain.MediaServer;

import java.util.List;

/**
 * 流媒体基础服务
 */
public interface IMediaServerBaseService {

    /**
     * 新增媒体服务器
     */
    int add(MediaServer mediaServer);

    /**
     * 按 ID 动态更新媒体服务器（仅更新非空字段）
     */
    int update(MediaServer mediaServer);



    /**
     * 按 ID 删除媒体服务器
     */
    void deleteById(String id);


    /**
     * 按 ID 查询媒体服务器
     */
    MediaServer getById(String id);

    /**
     * 查询全部媒体服务器
     */
    List<MediaServer> queryAll();

    /**
     * 查询默认媒体服务器
     */
    MediaServer queryDefault();

    /**
     * 查询所有开启了录像辅助服务的媒体服务器
     */
    List<MediaServer> queryAllWithAssistPort();

    /**
     * 按 host + http 端口查询媒体服务器
     */
    MediaServer queryOneByHostAndPort(String host, int port);
}
