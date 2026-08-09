package com.ruoyi.wvp.gb28181.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * SIP 黑名单实体类
 * 对应表 wvp_sip_black
 *
 * @author ruoyi-wvp
 */
@Data
@TableName("wvp_sip_black")
public class SipBlackEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 设备ID（国标设备编码，必填） */
    private String deviceId;

    /** IP地址 */
    private String ip;

    /** 端口 */
    private Integer port;

    /** 备注 */
    private String remark;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;
}
