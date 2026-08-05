package com.ruoyi.wvp.common;

import com.ruoyi.system.config.SslConfig;
import com.ruoyi.wvp.media.bean.MediaServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流地址 nginx 统一代理处理工具
 */
@Component
public class StreamUrlHelper {

    @Autowired
    private SslConfig sslConfig;

    /**
     * 如果媒体服务器启用了 nginx 代理且配置了域名，则替换流地址的 host 和端口
     * @param streamInfo  流信息对象（会被克隆后替换）
     * @param mediaServer 媒体服务器配置
     * @return 替换后的 StreamInfo（如果未启用则返回原对象）
     */
    public StreamInfo applyNginxProxy(StreamInfo streamInfo, MediaServer mediaServer) {
        if (streamInfo == null || mediaServer == null || !mediaServer.isNginxProxyEnabled()) {
            return streamInfo;
        }
        String domain = sslConfig.getAccessDomain();
        if (StringUtils.isBlank(domain)) {
            return streamInfo;
        }
        streamInfo = streamInfo.clone();
        streamInfo.changeStreamUrlForNginx(domain, mediaServer.getNginxHttpPort(), mediaServer.getNginxHttpsPort());
        return streamInfo;
    }
}
