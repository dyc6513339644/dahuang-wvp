package com.ruoyi.wvp.mapper;

import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.streamProxy.dao.provider.StreamProxyProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface StreamProxyMapper {

    /**
     * 查询所有拉流代理（返回 Device 对象，由 Service 层转为 StreamProxy DTO）
     */
    @SelectProvider(type = StreamProxyProvider.class, method = "selectAll")
    List<Device> selectAll(@Param("query") String query, @Param("pulling") Boolean pulling, @Param("mediaServerId") String mediaServerId);

    /**
     * 根据 app(device_id) 查询拉流代理
     */
    @SelectProvider(type = StreamProxyProvider.class, method = "selectOneByAppAndStream")
    Device selectOneByAppAndStream(@Param("app") String app, @Param("stream") String stream);

    /**
     * 查询指定流媒体节点上正在拉流的代理
     */
    @SelectProvider(type = StreamProxyProvider.class, method = "selectForPushingInMediaServer")
    List<Device> selectForPushingInMediaServer(@Param("mediaServerId") String mediaServerId, @Param("enable") boolean enable);

    /**
     * 根据 id 查询
     */
    @SelectProvider(type = StreamProxyProvider.class, method = "select")
    Device select(@Param("id") int id);

    /**
     * 统计总数
     */
    @Select("SELECT count(1) FROM wvp_device WHERE protocol_type = 'STREAM_PROXY'")
    int getAllCount();

    /**
     * 统计在线数
     */
    @Select("SELECT count(1) FROM wvp_device WHERE protocol_type = 'STREAM_PROXY' AND on_line = 1")
    int getOnline();

    /**
     * 设置在线
     */
    @Update("UPDATE wvp_device SET on_line = 1 WHERE id = #{id}")
    int online(@Param("id") int id);

    /**
     * 设置离线
     */
    @Update("UPDATE wvp_device SET on_line = 0 WHERE id = #{id}")
    int offline(@Param("id") int id);

    /**
     * 删除
     */
    @Delete("DELETE FROM wvp_device WHERE id = #{id}")
    int delete(@Param("id") int id);

    /**
     * 批量删除
     */
    @Delete(value = "<script>" +
            "DELETE FROM wvp_device WHERE id IN (" +
            "<foreach collection='list' index='index' item='item' separator=','> " +
            "#{item.id}" +
            "</foreach>" +
            ")" +
            "</script>")
    void deleteByList(List<Device> list);

    /**
     * 根据 app 和 stream 删除
     */
    @Delete("DELETE FROM wvp_device WHERE device_id = #{app} AND protocol_type = 'STREAM_PROXY'")
    int delByAppAndStream(String app, String stream);
}