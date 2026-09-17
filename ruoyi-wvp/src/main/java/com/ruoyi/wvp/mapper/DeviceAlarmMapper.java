package com.ruoyi.wvp.mapper;

import com.ruoyi.wvp.gb28181.bean.DeviceAlarm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于存储设备的报警信息
 */
@Mapper
@Repository
public interface DeviceAlarmMapper {

    @Insert("INSERT INTO wvp_device_alarm (device_id, channel_id, alarm_priority, alarm_method, alarm_time, alarm_description, longitude, latitude, alarm_type , create_time ) " +
            "VALUES (#{deviceId}, #{channelId}, #{alarmPriority}, #{alarmMethod}, #{alarmTime}, #{alarmDescription}, #{longitude}, #{latitude}, #{alarmType}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(DeviceAlarm alarm);

    @Update("UPDATE wvp_device_alarm SET channel_name = #{channelName} WHERE id = #{id}")
    int updateChannelName(@Param("id") Long id, @Param("channelName") String channelName);


    @Select( value = {" <script>" +
            " SELECT a.* FROM wvp_device_alarm a " +
            " WHERE 1=1 " +
            " <if test=\"deviceId != null\" >  AND a.device_id = #{deviceId}</if>" +
            " <if test=\"channelId != null\" >  AND a.channel_id = #{channelId}</if>" +
            " <if test=\"alarmPriority != null\" >  AND a.alarm_priority = #{alarmPriority} </if>" +
            " <if test=\"alarmMethod != null\" >  AND a.alarm_method = #{alarmMethod} </if>" +
            " <if test=\"alarmType != null\" >  AND a.alarm_type = #{alarmType} </if>" +
            " <if test=\"startTime != null\" >  AND a.alarm_time &gt;= #{startTime} </if>" +
            " <if test=\"endTime != null\" >  AND a.alarm_time &lt;= #{endTime} </if>" +
            " ORDER BY a.alarm_time ASC " +
            " </script>"})
    List<DeviceAlarm> query(@Param("deviceId") String deviceId, @Param("channelId") String channelId, @Param("alarmPriority") String alarmPriority, @Param("alarmMethod") String alarmMethod,
                            @Param("alarmType") String alarmType, @Param("startTime") String startTime, @Param("endTime") String endTime);


    @Delete(" <script>" +
            "DELETE FROM wvp_device_alarm WHERE 1=1 " +
            " <if test=\"deviceIdList != null and id == null \" > AND device_id in " +
            "<foreach collection='deviceIdList'  item='item'  open='(' separator=',' close=')' > #{item}</foreach>" +
            "</if>" +
            " <if test=\"time != null and id == null \" > AND alarm_time &lt;= #{time}</if>" +
            " <if test=\"id != null\" > AND id = #{id}</if>" +
            " </script>"
            )
    int clearAlarmBeforeTime(@Param("id") Integer id, @Param("deviceIdList") List<String> deviceIdList, @Param("time") String time);
}
