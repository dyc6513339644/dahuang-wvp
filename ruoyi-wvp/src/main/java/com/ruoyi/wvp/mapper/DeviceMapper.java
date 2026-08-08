package com.ruoyi.wvp.mapper;

import com.ruoyi.wvp.gb28181.bean.Device;
import com.ruoyi.wvp.gb28181.bean.DeviceChannel;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用于存储设备信息
 */
@Mapper
@Repository
public interface DeviceMapper {

    Device getDeviceByDeviceId(@Param("deviceId") String deviceId);

    @Insert("INSERT INTO wvp_device (" +
            "device_id, " +
            "name, " +
            "manufacturer, " +
            "db_model, " +
            "firmware, " +
            "transport," +
            "stream_mode," +
            "media_server_id," +
            "ip," +
            "sdp_ip," +
            "local_ip," +
            "port," +
            "host_address," +
            "expires," +
            "register_time," +
            "keepalive_time," +
            "heart_beat_interval," +
            "heart_beat_count," +
            "position_capability," +
            "create_time," +
            "update_time," +
            "charset," +
            "subscribe_cycle_for_catalog," +
            "subscribe_cycle_for_mobile_position," +
            "mobile_position_submission_interval," +
            "subscribe_cycle_for_alarm," +
            "ssrc_check," +
            "as_message_channel," +
            "broadcast_push_after_ack," +
            "geo_coord_sys," +
            "on_line" +
            ") VALUES (" +
            "#{deviceId}," +
            "#{name}," +
            "#{manufacturer}," +
            "#{dbModel}," +
            "#{firmware}," +
            "#{transport}," +
            "#{streamMode}," +
            "#{mediaServerId}," +
            "#{ip}," +
            "#{sdpIp}," +
            "#{localIp}," +
            "#{port}," +
            "#{hostAddress}," +
            "#{expires}," +
            "#{registerTime}," +
            "#{keepaliveTime}," +
            "#{heartBeatInterval}," +
            "#{heartBeatCount}," +
            "#{positionCapability}," +
            "#{createTime}," +
            "#{updateTime}," +
            "#{charset}," +
            "#{subscribeCycleForCatalog}," +
            "#{subscribeCycleForMobilePosition}," +
            "#{mobilePositionSubmissionInterval}," +
            "#{subscribeCycleForAlarm}," +
            "#{ssrcCheck}," +
            "#{asMessageChannel}," +
            "#{broadcastPushAfterAck}," +
            "#{geoCoordSys}," +
            "#{onLine}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int add(Device device);

    @Update(value = {" <script>" +
            "UPDATE wvp_device " +
            "SET update_time=#{updateTime}" +
            "<if test=\"name != null\">, name=#{name}</if>" +
            "<if test=\"manufacturer != null\">, manufacturer=#{manufacturer}</if>" +
            "<if test=\"dbModel != null\">, db_model=#{dbModel}</if>" +
            "<if test=\"firmware != null\">, firmware=#{firmware}</if>" +
            "<if test=\"transport != null\">, transport=#{transport}</if>" +
            "<if test=\"ip != null\">, ip=#{ip}</if>" +
            "<if test=\"localIp != null\">, local_ip=#{localIp}</if>" +
            "<if test=\"port != null\">, port=#{port}</if>" +
            "<if test=\"hostAddress != null\">, host_address=#{hostAddress}</if>" +
            "<if test=\"onLine != null\">, on_line=#{onLine}</if>" +
            "<if test=\"registerTime != null\">, register_time=#{registerTime}</if>" +
            "<if test=\"keepaliveTime != null\">, keepalive_time=#{keepaliveTime}</if>" +
            "<if test=\"heartBeatInterval != null\">, heart_beat_interval=#{heartBeatInterval}</if>" +
            "<if test=\"positionCapability != null\">, position_capability=#{positionCapability}</if>" +
            "<if test=\"heartBeatCount != null\">, heart_beat_count=#{heartBeatCount}</if>" +
            "<if test=\"expires != null\">, expires=#{expires}</if>" +
            "WHERE device_id=#{deviceId}" +
            " </script>"})
    int update(Device device);

    @Select(
            " <script>" +
                    "SELECT " +
                    "id, " +
                    "device_id, " +
                    "coalesce(custom_name, name) as name, " +
                    "password, " +
                    "manufacturer, " +
                    "db_model, " +
                    "firmware, " +
                    "transport," +
                    "stream_mode," +
                    "ip," +
                    "sdp_ip," +
                    "local_ip," +
                    "port," +
                    "host_address," +
                    "expires," +
                    "register_time," +
                    "keepalive_time," +
                    "create_time," +
                    "update_time," +
                    "charset," +
                    "subscribe_cycle_for_catalog," +
                    "subscribe_cycle_for_mobile_position," +
                    "mobile_position_submission_interval," +
                    "subscribe_cycle_for_alarm," +
                    "ssrc_check," +
                    "as_message_channel," +
                    "broadcast_push_after_ack," +
                    "geo_coord_sys," +
                    "on_line," +
                    "media_server_id," +
                    "(SELECT count(0) FROM wvp_device_channel dc WHERE dc.data_type = #{dataType} and dc.data_device_id= de.id) as channel_count " +
                    "FROM wvp_device de" +
                    "<if test='online != null'> where de.on_line=${online}</if>" +
                    " order by de.create_time desc " +
                    " </script>"
    )
    List<Device> getDevices(@Param("dataType") Integer dataType, @Param("online") Boolean online);

    @Delete("DELETE FROM wvp_device WHERE device_id=#{deviceId}")
    int del(String deviceId);

    List<Device> getOnlineDevices();

    @Select("SELECT " +
            "id," +
            "device_id," +
            "coalesce(custom_name,name)as name," +
            "password," +
            "manufacturer," +
            "db_model," +
            "firmware," +
            "transport," +
            "stream_mode," +
            "ip," +
            "sdp_ip," +
            "local_ip," +
            "port," +
            "host_address," +
            "expires," +
            "register_time," +
            "keepalive_time," +
            "create_time," +
            "update_time," +
            "charset," +
            "subscribe_cycle_for_catalog," +
            "subscribe_cycle_for_mobile_position," +
            "mobile_position_submission_interval," +
            "subscribe_cycle_for_alarm," +
            "ssrc_check," +
            "as_message_channel," +
            "broadcast_push_after_ack," +
            "geo_coord_sys," +
            "on_line" +
            " FROM wvp_device WHERE ip = #{host} AND port=#{port}")
    Device getDeviceByHostAndPort(@Param("host") String host, @Param("port") int port);

    /**
     * 修改设备信息
     *
     * @param device
     */
    void updateCustom(Device device);

    @Insert("INSERT INTO wvp_device (" +
            "device_id," +
            "custom_name," +
            "password," +
            "sdp_ip," +
            "create_time," +
            "update_time," +
            "charset," +
            "ssrc_check," +
            "as_message_channel," +
            "broadcast_push_after_ack," +
            "geo_coord_sys," +
            "on_line," +
            "stream_mode," +
            "media_server_id" +
            ") VALUES (" +
            "#{deviceId}," +
            "#{name}," +
            "#{password}," +
            "#{sdpIp}," +
            "#{createTime}," +
            "#{updateTime}," +
            "#{charset}," +
            "#{ssrcCheck}," +
            "#{asMessageChannel}," +
            "#{broadcastPushAfterAck}," +
            "#{geoCoordSys}," +
            "#{onLine}," +
            "#{streamMode}," +
            "#{mediaServerId}" +
            ")")
    void addCustomDevice(Device device);

    @Select("select * FROM wvp_device")
    List<Device> getAll();

    @Select("select * FROM wvp_device where as_message_channel = true AND (protocol_type = 'GB28181' OR protocol_type IS NULL)")
    List<Device> queryDeviceWithAsMessageChannel();

    /**
     * 查询国标设备
     *
     * @param dataType 设备类型
     * @param device   设备
     * @return
     */
    List<Device> getDeviceList(@Param("dataType") Integer dataType, @Param("device") Device device);

    /**
     * 查询所有类型设备（含国标、推流、拉流代理）
     */
    List<Device> getAllDeviceList(@Param("device") Device device);

    @Select("select * from wvp_device_channel where id = #{id}")
    DeviceChannel getRawChannel(@Param("id") int id);

    /**
     * 查询设备
     *
     * @param id 设备id
     * @return
     */
    Device query(@Param("id") Integer id);

    @Select("select wd.* from wvp_device wd left join wvp_device_channel wdc on wdc.data_type = #{dataType} and wd.id = wdc.data_device_id  where wdc.id = #{channelId}")
    Device queryByChannelId(@Param("dataType") Integer dataType, @Param("channelId") Integer channelId);

    @Select("select wd.* from wvp_device wd left join wvp_device_channel wdc on wdc.data_type = #{dataType} and wd.id = wdc.data_device_id  where wdc.device_id = #{channelDeviceId}")
    Device getDeviceBySourceChannelDeviceId(@Param("dataType") Integer dataType, @Param("channelDeviceId") String channelDeviceId);

    /**
     * 修改设备信息
     *
     * @param device
     */
    void updateSubscribeCatalog(Device device);

    /**
     * 更新报警订阅
     *
     * @param device
     */
    void updateSubscribeAlarm(Device device);

    /**
     * 修改设备信息
     *
     * @param device
     */
    void updateSubscribeMobilePosition(Device device);

    // ==================== 拉流代理设备操作 ====================

    @Insert("INSERT INTO wvp_device (" +
            "device_id, name, protocol_type, src_url, stream_type, expires, " +
            "ffmpeg_cmd_key, rtsp_type, enable_audio, enable_mp4, " +
            "enable_remove_none_reader, enable_disable_none_reader, " +
            "media_server_id, create_time, update_time, on_line" +
            ") VALUES (" +
            "#{deviceId}, #{name}, #{protocolType}, #{srcUrl}, #{streamType}, #{expires}, " +
            "#{ffmpegCmdKey}, #{rtspType}, #{enableAudio}, #{enableMp4}, " +
            "#{enableRemoveNoneReader}, #{enableDisableNoneReader}, " +
            "#{mediaServerId}, #{createTime}, #{updateTime}, #{onLine}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addProxyDevice(Device device);

    @Update("UPDATE wvp_device SET " +
            "name=#{name}, src_url=#{srcUrl}, stream_type=#{streamType}, expires=#{expires}, " +
            "ffmpeg_cmd_key=#{ffmpegCmdKey}, rtsp_type=#{rtspType}, " +
            "enable_audio=#{enableAudio}, enable_mp4=#{enableMp4}, " +
            "enable_remove_none_reader=#{enableRemoveNoneReader}, " +
            "enable_disable_none_reader=#{enableDisableNoneReader}, " +
            "update_time=#{updateTime} " +
            "WHERE device_id=#{deviceId} AND protocol_type='STREAM_PROXY'")
    int updateProxyDevice(Device device);

    @Update("UPDATE wvp_device SET stream_key=#{streamKey}, media_server_id=#{mediaServerId}, on_line=#{onLine}, update_time=#{updateTime} " +
            "WHERE device_id=#{deviceId} AND protocol_type='STREAM_PROXY'")
    int updateProxyStream(Device device);

    @Update("UPDATE wvp_device SET stream_key=null, media_server_id=null, on_line=0 " +
            "WHERE device_id=#{deviceId} AND protocol_type='STREAM_PROXY'")
    int removeProxyStream(Device device);

    @Select("SELECT de.*, (SELECT count(0) FROM wvp_device_channel dc WHERE dc.data_type = 3 AND dc.data_device_id = de.id) as channel_count FROM wvp_device de WHERE de.device_id=#{deviceId} AND de.protocol_type='STREAM_PROXY'")
    Device getProxyDeviceByDeviceId(@Param("deviceId") String deviceId);

    @Select("SELECT de.*, (SELECT count(0) FROM wvp_device_channel dc WHERE dc.data_type = 3 AND dc.data_device_id = de.id) as channel_count FROM wvp_device de WHERE de.protocol_type='STREAM_PROXY' ORDER BY de.create_time DESC")
    List<Device> getProxyDeviceList();

    @Select("<script>SELECT de.*, (SELECT count(0) FROM wvp_device_channel dc WHERE dc.data_type = 3 AND dc.data_device_id = de.id) as channel_count FROM wvp_device de WHERE de.protocol_type='STREAM_PROXY'" +
            "<if test='query != null and query != \"\"'> AND (de.device_id LIKE CONCAT('%', #{query}, '%') OR de.name LIKE CONCAT('%', #{query}, '%'))</if>" +
            "<if test='onLine != null'> AND de.on_line = #{onLine}</if>" +
            " ORDER BY de.create_time DESC" +
            "</script>")
    List<Device> getProxyDeviceListByCondition(@Param("query") String query, @Param("onLine") Boolean onLine);

    @Delete("DELETE FROM wvp_device WHERE device_id=#{deviceId} AND protocol_type='STREAM_PROXY'")
    int deleteProxyDevice(@Param("deviceId") String deviceId);

    // ========== 推流设备 (STREAM_PUSH) ==========

    @Insert("INSERT INTO wvp_device (device_id, name, media_server_id, on_line, start_offline_push, protocol_type, create_time, update_time, push_time) " +
            "VALUES (#{deviceId}, #{name}, 'auto', #{onLine}, #{startOfflinePush}, 'STREAM_PUSH', #{createTime}, #{updateTime}, #{pushTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int addPushDevice(Device device);

    @Update("UPDATE wvp_device SET on_line=#{onLine}, media_server_id=#{mediaServerId}, push_time=#{pushTime}, update_time=#{updateTime} WHERE device_id=#{deviceId} AND protocol_type='STREAM_PUSH'")
    int updatePushDevice(Device device);

    @Update("UPDATE wvp_device SET on_line=#{onLine}, update_time=#{updateTime}, start_offline_push=#{startOfflinePush} WHERE device_id=#{deviceId} AND protocol_type='STREAM_PUSH'")
    int updatePushDeviceStatus(Device device);

    @Select("SELECT * FROM wvp_device WHERE device_id=#{deviceId} AND protocol_type='STREAM_PUSH'")
    Device getPushDeviceByDeviceId(@Param("deviceId") String deviceId);

    @Select("SELECT * FROM wvp_device WHERE protocol_type='STREAM_PUSH' ORDER BY create_time DESC")
    List<Device> getPushDeviceList();

    @Select("<script>SELECT * FROM wvp_device WHERE protocol_type='STREAM_PUSH'" +
            "<if test='query != null and query != \"\"'> AND (device_id LIKE CONCAT('%', #{query}, '%') OR name LIKE CONCAT('%', #{query}, '%'))</if>" +
            "<if test='pushing != null'> AND on_line = #{pushing}</if>" +
            "<if test='mediaServerId != null'> AND media_server_id = #{mediaServerId}</if>" +
            " ORDER BY create_time DESC" +
            "</script>")
    List<Device> getPushDeviceListByCondition(@Param("query") String query, @Param("pushing") Boolean pushing, @Param("mediaServerId") String mediaServerId);

    @Select("SELECT * FROM wvp_device WHERE media_server_id=#{mediaServerId} AND protocol_type='STREAM_PUSH'")
    List<Device> getPushDeviceListByMediaServerId(@Param("mediaServerId") String mediaServerId);

    @Delete("DELETE FROM wvp_device WHERE id=#{id} AND protocol_type='STREAM_PUSH'")
    int deletePushDevice(@Param("id") int id);

    @Select("SELECT COUNT(1) FROM wvp_device WHERE protocol_type='STREAM_PUSH'")
    int getPushDeviceCount();

    @Select("SELECT COUNT(1) FROM wvp_device WHERE protocol_type='STREAM_PUSH' AND on_line = 1")
    int getPushDeviceOnlineCount();

    // ========== ONVIF设备 (ONVIF) ==========

    @Insert("INSERT INTO wvp_device (" +
            "device_id, name, protocol_type, ip, user_name, password, " +
            "onvif_auth_type, manufacturer, db_model, firmware, " +
            "media_server_id, heart_beat_interval, heart_beat_count, keepalive_time, " +
            "create_time, update_time" +
            ") VALUES (" +
            "#{deviceId}, #{name}, 'ONVIF', #{ip}, #{userName}, #{password}, " +
            "#{onvifAuthType}, #{manufacturer}, #{dbModel}, #{firmware}, " +
            "'auto', #{heartBeatInterval}, #{heartBeatCount}, #{keepaliveTime}, " +
            "#{createTime}, #{updateTime}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertOnvifDevice(Device device);

    @Update("UPDATE wvp_device SET " +
            "name=#{name}, ip=#{ip}, user_name=#{userName}, password=#{password}, " +
            "onvif_auth_type=#{onvifAuthType}, update_time=#{updateTime}, on_line=#{onLine} " +
            "WHERE id=#{id} AND protocol_type='ONVIF'")
    int updateOnvifDevice(Device device);

    @Update("UPDATE wvp_device SET " +
            "name=#{name}, user_name=#{userName}, password=#{password}, " +
            "onvif_auth_type=#{onvifAuthType}, update_time=#{updateTime} " +
            "WHERE id=#{id} AND protocol_type='ONVIF'")
    int updateOnvifDeviceBasic(Device device);

    @Update("UPDATE wvp_device SET " +
            "manufacturer=#{manufacturer}, db_model=#{dbModel}, firmware=#{firmware}, " +
            "on_line=#{onLine}, keepalive_time=#{keepaliveTime}, update_time=#{updateTime} " +
            "WHERE id=#{id} AND protocol_type='ONVIF'")
    int updateOnvifDeviceInfo(Device device);

    @Select("SELECT * FROM wvp_device WHERE id=#{id} AND protocol_type='ONVIF'")
    Device getOnvifDeviceById(@Param("id") Long id);

    @Select("<script>SELECT de.*, " +
            "(SELECT COUNT(0) FROM wvp_device_channel dc WHERE dc.data_device_id = de.id) AS channel_count " +
            "FROM wvp_device de WHERE de.protocol_type='ONVIF'" +
            "<if test='name != null and name != \"\"'> AND (de.name LIKE CONCAT('%', #{name}, '%') OR de.device_id LIKE CONCAT('%', #{name}, '%'))</if>" +
            "<if test='status != null'> AND de.on_line = #{status}</if>" +
            " ORDER BY de.create_time DESC" +
            "</script>")
    List<Device> selectOnvifDeviceList(Device device);

    /**
     * 查询所有 ONVIF 设备（无参，纯静态SQL，用于定时任务等场景）
     */
    @Select("SELECT de.*, " +
            "(SELECT COUNT(0) FROM wvp_device_channel dc WHERE dc.data_device_id = de.id) AS channel_count " +
            "FROM wvp_device de WHERE de.protocol_type = 'ONVIF' " +
            "ORDER BY de.create_time DESC")
    List<Device> selectAllOnvifDevices();

    /**
     * 查询需要探测的 ONVIF 设备（SQL 层按心跳时间过滤，大设备量场景下避免全量加载到内存）
     */
    @Select("SELECT de.*, " +
            "(SELECT COUNT(0) FROM wvp_device_channel dc WHERE dc.data_device_id = de.id) AS channel_count " +
            "FROM wvp_device de WHERE de.protocol_type = 'ONVIF' " +
            "AND ( " +
            "  (de.keepalive_time IS NULL OR de.keepalive_time = '') " +
            "  OR (de.on_line = 1 AND TIMESTAMPDIFF(SECOND, de.keepalive_time, NOW()) >= COALESCE(de.heart_beat_interval, 30)) " +
            "  OR (de.on_line = 0 AND TIMESTAMPDIFF(SECOND, de.keepalive_time, NOW()) >= 300) " +
            ") " +
            "ORDER BY de.create_time DESC")
    List<Device> selectOnvifDevicesForProbe();

    @Delete("DELETE FROM wvp_device WHERE id=#{id} AND protocol_type='ONVIF'")
    int deleteOnvifDevice(@Param("id") Long id);
}
