package com.ruoyi.wvp.gb28181.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * SIP配置实体类
 *
 * @author ruoyi-wvp
 */
@Data
@TableName("wvp_sip_config")
public class SipConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * SIP服务监听的IP地址，多个IP用逗号分隔
     */
    private String ip;

    /**
     * 对外展示的IP地址
     */
    private String showIp;

    /**
     * SIP服务监听的端口
     */
    private Integer port;

    /**
     * 国标域
     */
    @TableField(value = "gb_domain")
    private String gbDomain;

    /**
     * 国标ID
     */
    private String serverId;

    /**
     * 密码
     */
    @TableField(value = "gb_pwd")
    private String gbPwd;

    /**
     * 云台速度
     */
    private Integer ptzSpeed;

    /**
     * 注册时间间隔（秒）
     */
    private Integer registerTimeInterval;

    /**
     * 是否接收报警信息
     */
    private Boolean alarm;

    /**
     * 超时时间（秒）
     */
    private Long timeout;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
