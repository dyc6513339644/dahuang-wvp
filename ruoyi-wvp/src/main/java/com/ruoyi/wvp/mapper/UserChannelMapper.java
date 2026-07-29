package com.ruoyi.wvp.mapper;

import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserChannelMapper {

    @Select("SELECT channel_id FROM sys_user_channel WHERE user_id = #{userId}")
    List<String> selectChannelsByUserId(@Param("userId") Long userId);

    @Insert("<script>" +
            "INSERT INTO sys_user_channel (user_id, channel_id, device_id) VALUES " +
            "<foreach collection='batch' item='row' separator=','>(#{userId}, #{row.channelId}, #{row.deviceId})</foreach>" +
            "</script>")
    int insertUserChannelsBatch(@Param("userId") Long userId, @Param("batch") List<Map<String, String>> batch);

    @Delete("DELETE FROM sys_user_channel WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    @Select("SELECT COUNT(0) FROM sys_user_channel WHERE user_id = #{userId}")
    int countByUserId(@Param("userId") Long userId);

    /** 查询用户分配的设备ID列表（去重，过滤空值） */
    @Select("SELECT DISTINCT device_id FROM sys_user_channel WHERE user_id = #{userId} AND device_id IS NOT NULL AND device_id != ''")
    List<String> selectDistinctDeviceIdsByUserId(@Param("userId") Long userId);

    /** 查询用户有权限的设备列表 */
    @Select("SELECT de.*, " +
            "(SELECT COUNT(0) FROM wvp_device_channel dc WHERE dc.data_device_id = de.id) AS channel_count " +
            "FROM wvp_device de " +
            "INNER JOIN sys_user_channel uc ON uc.device_id = de.device_id " +
            "WHERE uc.user_id = #{userId} " +
            "ORDER BY de.name")
    List<Device> selectUserDevices(@Param("userId") Long userId);

    /** 查询用户有权限的通道列表 */
    @Select("<script>" +
            "SELECT c.* FROM wvp_device_channel c " +
            "INNER JOIN wvp_device d ON d.id = c.data_device_id " +
            "INNER JOIN sys_user_channel uc ON uc.device_id = d.device_id " +
            "WHERE uc.user_id = #{userId} " +
            "<if test='deviceId != null and deviceId != \"\"'> AND d.device_id = #{deviceId}</if>" +
            "<if test='query != null and query != \"\"'> AND (c.name LIKE CONCAT('%', #{query}, '%') OR c.device_id LIKE CONCAT('%', #{query}, '%'))</if>" +
            "<if test='online != null'> AND c.status = #{online}</if>" +
            " ORDER BY c.name" +
            "</script>")
    List<DeviceChannel> selectUserChannels(@Param("userId") Long userId, @Param("deviceId") String deviceId, @Param("query") String query, @Param("online") Boolean online);
}
