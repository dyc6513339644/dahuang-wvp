package com.ruoyi.common.core.domain;

import lombok.Data;

/**
 * RTP 推流参数（兼容海康 ISUP 模块）
 */
@Data
public class RtpServerParam {

    private Long id;

    private String ip;

    private Integer port;

    private String ssrc;

    private String gbDeviceId;

    private String gbChannelId;

    private String streamMode;

    private String mediaServerId;

    private String app;

    private String stream;

    private String mobileNo;

    private String type;

    /** 通道号 */
    private Integer channel;

    /** 是否回放 */
    private Boolean playback;

    /** 回放开始时间 */
    private String startTime;

    /** 回放结束时间 */
    private String endTime;
}
