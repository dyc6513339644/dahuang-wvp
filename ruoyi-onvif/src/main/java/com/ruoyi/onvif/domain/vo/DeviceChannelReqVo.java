package com.ruoyi.onvif.domain.vo;

import lombok.Data;

@Data
public class DeviceChannelReqVo {

    private String parentId;
    private String key;
    private String status;
    private String dataType;
    private Long dataDeviceId;

    /** 是否过滤经纬度（默认 false=不过滤，true=只查经纬度不为空的通道） */
    private Boolean filterGps;
}
