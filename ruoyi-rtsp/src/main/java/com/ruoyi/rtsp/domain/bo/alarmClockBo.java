package com.ruoyi.rtsp.domain.bo;

import lombok.Data;

import java.util.Date;

/**
 * 历史播放 bo
 *
 * @Author: 陈江灿
 * @CreateTime: 2025-05-24
 */
@Data
public class alarmClockBo {

    /**
     * id
     */
    private Long id;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;


}
