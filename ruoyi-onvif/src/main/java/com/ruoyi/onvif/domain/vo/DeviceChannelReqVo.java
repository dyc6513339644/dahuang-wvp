package com.ruoyi.onvif.domain.vo;

import lombok.Data;

@Data
public class DeviceChannelReqVo {

    private String parentId;
    private String key;
    /** 在线状态过滤（true 在线 / false 离线），推荐外部按此字段过滤 */
    private Boolean onLine;
    private String dataType;
    private Long dataDeviceId;

    /** 是否过滤经纬度（默认 false=不过滤，true=只查经纬度不为空的通道） */
    private Boolean filterGps;
}
