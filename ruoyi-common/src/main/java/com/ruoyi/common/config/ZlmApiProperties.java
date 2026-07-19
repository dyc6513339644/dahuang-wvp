package com.ruoyi.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zlm-api")
public class ZlmApiProperties {

    // 大华端口
    private int dhHttpPort;
    private int dhRtspPort;
    private int dhRtmpPort;

    // 海康端口
    private int hkHttpPort;
    private int hkRtspPort;
    private int hkRtmpPort;

    public ZlmApiProperties() {}
}
