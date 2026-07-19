package com.ruoyi.wvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * SIP配置Mapper接口
 *
 * @author ruoyi-wvp
 */
@Mapper
@Repository
public interface SipConfigMapper  extends BaseMapper<SipConfigEntity> {

//    /**
//     * 获取SIP配置（只取第一条记录，系统中只应有一条配置）
//     *
//     * @return SIP配置
//     */
//    @Select("SELECT " +
//            "id, " +
//            "ip, " +
//            "show_ip, " +
//            "port, " +
//            "domain, " +
//            "server_id, " +
//            "password, " +
//            "ptz_speed, " +
//            "register_time_interval, " +
//            "alarm, " +
//            "timeout, " +
//            "create_time, " +
//            "update_time " +
//            "FROM wvp_sip_config " +
//            "LIMIT 1")
//    SipConfigEntity getSipConfig();
//
//    /**
//     * 根据ID获取SIP配置
//     *
//     * @param id 配置ID
//     * @return SIP配置
//     */
//    @Select("SELECT " +
//            "id, " +
//            "ip, " +
//            "show_ip, " +
//            "port, " +
//            "domain, " +
//            "server_id, " +
//            "password, " +
//            "ptz_speed, " +
//            "register_time_interval, " +
//            "alarm, " +
//            "timeout, " +
//            "create_time, " +
//            "update_time " +
//            "FROM wvp_sip_config " +
//            "WHERE id = #{id}")
//    SipConfigEntity getSipConfigById(@Param("id") Long id);
//
//    /**
//     * 保存SIP配置
//     *
//     * @param sipConfig SIP配置
//     * @return 影响行数
//     */
//    @Insert("INSERT INTO wvp_sip_config (" +
//            "ip, " +
//            "show_ip, " +
//            "port, " +
//            "domain, " +
//            "server_id, " +
//            "password, " +
//            "ptz_speed, " +
//            "register_time_interval, " +
//            "alarm, " +
//            "timeout, " +
//            "create_time, " +
//            "update_time" +
//            ") VALUES (" +
//            "#{ip}, " +
//            "#{showIp}, " +
//            "#{port}, " +
//            "#{domain}, " +
//            "#{serverId}, " +
//            "#{password}, " +
//            "#{ptzSpeed}, " +
//            "#{registerTimeInterval}, " +
//            "#{alarm}, " +
//            "#{timeout}, " +
//            "#{createTime}, " +
//            "#{updateTime}" +
//            ")")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    int add(SipConfigEntity sipConfig);
//
//    /**
//     * 更新SIP配置
//     *
//     * @param sipConfig SIP配置
//     * @return 影响行数
//     */
//    @Update("UPDATE wvp_sip_config SET " +
//            "ip = #{ip}, " +
//            "show_ip = #{showIp}, " +
//            "port = #{port}, " +
//            "domain = #{domain}, " +
//            "server_id = #{serverId}, " +
//            "password = #{password}, " +
//            "ptz_speed = #{ptzSpeed}, " +
//            "register_time_interval = #{registerTimeInterval}, " +
//            "alarm = #{alarm}, " +
//            "timeout = #{timeout}, " +
//            "update_time = #{updateTime} " +
//            "WHERE id = #{id}")
//    int update(SipConfigEntity sipConfig);
//
//    /**
//     * 获取所有SIP配置列表
//     *
//     * @return SIP配置列表
//     */
//    @Select("SELECT " +
//            "id, " +
//            "ip, " +
//            "show_ip, " +
//            "port, " +
//            "domain, " +
//            "server_id, " +
//            "password, " +
//            "ptz_speed, " +
//            "register_time_interval, " +
//            "alarm, " +
//            "timeout, " +
//            "create_time, " +
//            "update_time " +
//            "FROM wvp_sip_config")
//    List<SipConfigEntity> getAll();
//
//    /**
//     * 删除SIP配置
//     *
//     * @param id 配置ID
//     * @return 影响行数
//     */
//    @Delete("DELETE FROM wvp_sip_config WHERE id = #{id}")
//    int delete(@Param("id") Long id);
//
//    /**
//     * 获取配置记录数量
//     *
//     * @return 记录数量
//     */
//    @Select("SELECT COUNT(*) FROM wvp_sip_config")
//    int getCount();
}
