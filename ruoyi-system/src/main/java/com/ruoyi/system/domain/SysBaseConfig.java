package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础配置实体类（SSL/HTTPS等系统级基础配置）
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysBaseConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 配置主键 */
    @TableId(value = "config_id", type = IdType.AUTO)
    private Long configId;

    /** 是否开启HTTPS（0: 关闭, 1: 开启） */
    private String sslEnabled;

    /** 证书路径（classpath: 或绝对路径） */
    private String sslKeyStore;

    /** 证书密码 */
    private String sslKeyStorePassword;

    /** HTTPS端口（默认443） */
    private Integer sslPort;

    /** HTTP端口 */
    private Integer httpPort;

    /** 网站域名 */
    private String accessDomain;

}
