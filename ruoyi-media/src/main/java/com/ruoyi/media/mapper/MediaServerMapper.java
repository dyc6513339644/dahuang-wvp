package com.ruoyi.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.media.domain.MediaServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 媒体服务器节点 Mapper
 * <p>
 * 简单 CRUD 使用 MyBatis-Plus {@link BaseMapper} 内置方法（insert / selectById / selectList / deleteById）；
 * 复杂 SQL（动态更新、按 host+port 查询/删除、默认节点查询等）在 MediaServer.xml 中定义。
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
