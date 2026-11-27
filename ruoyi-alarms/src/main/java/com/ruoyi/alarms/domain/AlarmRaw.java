package com.ruoyi.alarms.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/** 报警原始数据中间表，先落原始报文再由消费线程异步归一化入 wvp_alarms */
@Data
@TableName("wvp_alarms_raw")
public class AlarmRaw implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 处理状态：待处理 */
    public static final int STATUS_PENDING = 0;
    /** 处理状态：处理中（已被某消费线程领取） */
    public static final int STATUS_PROCESSING = 1;
    /** 处理状态：处理失败（超过重试次数，等待人工/清理） */
    public static final int STATUS_FAILED = 2;

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 接入协议：GB28181/ISUP/ONVIF/PUSH/PULL/OTHER */
    private String protocolType;

    /** 设备ID（排障与按设备清理用，可为空） */
    private String deviceId;

    /** 通道ID */
    private String channelId;

    /** 统一报警报文 JSON（AlarmMsg 序列化结果） */
    private String msgJson;

    /** 处理状态：0待处理 1处理中 2失败 */
    private Integer status;

    /** 领取者标识（实例 + 线程），用于防重复消费与超时解锁 */
    private String lockOwner;

    /** 领取时间 */
    private String lockTime;

    /** 已重试次数 */
    private Integer retryCount;

    /** 最近一次失败原因 */
    private String errorMsg;

    /** 接收时间 */
    private String receiveTime;

    /** 创建时间 */
    private String createTime;
}
