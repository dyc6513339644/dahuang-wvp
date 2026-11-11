package com.ruoyi.onvif.domain.vo;

import lombok.Data;

@Data
public class DeviceReqVo {
    private String key;
    /** 在线状态 true 在线 / false 离线，对应 wvp_device.on_line 列 */
    private Boolean onLine;
    private String manufacturer;
    private String protocolType;
}
