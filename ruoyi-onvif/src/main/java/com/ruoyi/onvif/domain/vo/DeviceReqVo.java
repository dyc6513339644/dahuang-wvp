package com.ruoyi.onvif.domain.vo;

import lombok.Data;

@Data
public class DeviceReqVo {
    private String key;
    private Boolean status;
    private String manufacturer;
    private String protocolType;
}
