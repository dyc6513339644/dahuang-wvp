package com.ruoyi.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.media.domain.MediaServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 媒体服务器节点 Mapper
 */
@Mapper
@Repository
public interface MediaServerMapper extends BaseMapper<MediaServer> {

    /**
     * 按 host + http 端口查询媒体服务器
     */
    MediaServer queryOneByHostAndPort(@Param("host") String host, @Param("port") int port);

    /**
     * 查询默认媒体服务器
     */
    MediaServer queryDefault();

    /**
     * 查询所有开启了录像辅助服务的媒体服务器
     */
    List<MediaServer> queryAllWithAssistPort();
}
