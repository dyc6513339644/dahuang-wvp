package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户API密钥实体类
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wvp_user_api_key")
public class WvpUserApiKey extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 关联用户ID */
    private Long userId;

    /** Access Key (AK) */
    private String accessKey;

    /** Secret Key (SK) - 创建后不再返回，仅用于服务端签名验证 */
    @JsonIgnore
    @TableField("secret_key")
    private String secretKey;

    /** 应用名称 */
    private String appName;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expiredAt;

    /** 启用状态 (1:启用, 0:停用) */
    private Boolean enable;


}
