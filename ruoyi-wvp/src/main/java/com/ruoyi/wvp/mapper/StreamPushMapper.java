package com.ruoyi.wvp.mapper;

import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.streamPush.bean.StreamPush;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 推流设备查询（基于 wvp_device + wvp_device_channel）
 */
@Mapper
@Repository
public interface StreamPushMapper {

    @Select("<script>" +
            " SELECT de.id as id, de.device_id as app, de.name as deviceName, de.name as name, de.media_server_id as mediaServerId, " +
            " CASE WHEN de.on_line = 1 THEN true ELSE false END as pushing, de.start_offline_push as startOfflinePush, de.push_time as pushTime, " +
            " de.create_time as createTime, de.update_time as updateTime, " +
            " wdc.device_id as stream, wdc.gb_device_id as gbDeviceId, wdc.gb_name as gbName, " +
            " wdc.gb_longitude as gbLongitude, wdc.gb_latitude as gbLatitude, wdc.id as channelId, " +
            " (SELECT COUNT(0) FROM wvp_device_channel dc WHERE dc.data_type = 2 AND dc.data_device_id = de.id) as channelCount " +
            " FROM wvp_device de " +
            " LEFT JOIN wvp_device_channel wdc ON wdc.data_type = 2 AND de.id = wdc.data_device_id " +
            " WHERE de.protocol_type = 'STREAM_PUSH' " +
            " <if test='query != null'> AND (de.device_id LIKE CONCAT('%',#{query},'%') OR de.name LIKE CONCAT('%',#{query},'%') " +
            " OR wdc.gb_device_id LIKE CONCAT('%',#{query},'%') OR wdc.gb_name LIKE CONCAT('%',#{query},'%'))</if> " +
            " <if test='pushing != null'> AND de.on_line = #{pushing}</if> " +
            " <if test='mediaServerId != null'> AND de.media_server_id = #{mediaServerId}</if> " +
            " ORDER BY de.create_time DESC" +
            "</script>")
    List<StreamPush> selectAll(@Param("query") String query, @Param("pushing") Boolean pushing, @Param("mediaServerId") String mediaServerId);

    @Select("SELECT COUNT(1) FROM wvp_device WHERE protocol_type='STREAM_PUSH'")
    int getAllCount();

    @Select("SELECT COUNT(1) FROM wvp_device WHERE protocol_type='STREAM_PUSH' AND on_line = 1")
    int getAllPushing();

    @Select("SELECT de.id as id, de.device_id as app, de.name as deviceName, de.name as name, de.media_server_id as mediaServerId, " +
            " CASE WHEN de.on_line = 1 THEN true ELSE false END as pushing, de.start_offline_push as startOfflinePush, de.push_time as pushTime, " +
            " de.create_time as createTime, de.update_time as updateTime, " +
            " wdc.device_id as stream, wdc.gb_device_id as gbDeviceId, wdc.gb_name as gbName, " +
            " wdc.gb_longitude as gbLongitude, wdc.gb_latitude as gbLatitude, wdc.id as channelId " +
            " FROM wvp_device de " +
            " LEFT JOIN wvp_device_channel wdc ON wdc.data_type = 2 AND de.id = wdc.data_device_id " +
            " WHERE de.id = #{id}")
    StreamPush queryOne(@Param("id") int id);
}