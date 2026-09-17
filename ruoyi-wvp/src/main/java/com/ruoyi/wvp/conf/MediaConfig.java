package com.ruoyi.wvp.conf;

import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.wvp.media.service.IMediaServerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * 流媒体配置（从数据库 wvp_media_server 表加载默认节点）
 */
@Slf4j
@Service("mediaConfig")
public class MediaConfig {

    @Autowired
    private IMediaServerService mediaServerService;

    private MediaServer getDefault() {
        return mediaServerService.getDefaultMediaServer();
    }

    public String getId() {
        MediaServer def = getDefault();
        return def != null ? def.getId() : null;
    }

    public String getIp() {
        MediaServer def = getDefault();
        return def != null ? def.getIp() : null;
    }

    public String getWanIp() {
        return null;
    }

    public String getHookIp() {
        MediaServer def = getDefault();
        return def != null ? def.getHookIp() : "127.0.0.1";
    }

    public int getHttpPort() {
        MediaServer def = getDefault();
        return def != null ? def.getHttpPort() : 0;
    }

    public int getHttpSSlPort() {
        MediaServer def = getDefault();
        return def != null ? def.getHttpSSlPort() : 0;
    }

    public int getRtmpPort() {
        MediaServer def = getDefault();
        return def != null ? def.getRtmpPort() : 0;
    }

    public int getRtmpSSlPort() {
        MediaServer def = getDefault();
        return def != null ? def.getRtmpSSlPort() : 0;
    }

    public int getRtpProxyPort() {
        MediaServer def = getDefault();
        if (def == null) {
            return 0;
        }
        return def.getRtpProxyPort();
    }

    public int getRtspPort() {
        MediaServer def = getDefault();
        return def != null ? def.getRtspPort() : 0;
    }

    public int getRtspSSLPort() {
        MediaServer def = getDefault();
        return def != null ? def.getRtspSSLPort() : 0;
    }

    public boolean isAutoConfig() {
        MediaServer def = getDefault();
        return def != null && def.isAutoConfig();
    }

    public String getSecret() {
        MediaServer def = getDefault();
        return def != null ? def.getSecret() : null;
    }

    public boolean isRtpEnable() {
        MediaServer def = getDefault();
        return def != null && def.isRtpEnable();
    }

    public String getRtpPortRange() {
        MediaServer def = getDefault();
        return def != null ? def.getRtpPortRange() : null;
    }

    public String getRtpSendPortRange() {
        MediaServer def = getDefault();
        return def != null ? def.getSendRtpPortRange() : null;
    }

    public int getRecordAssistPort() {
        MediaServer def = getDefault();
        return def != null ? def.getRecordAssistPort() : 0;
    }

    public Integer getRecordDay() {
        MediaServer def = getDefault();
        return def != null ? def.getRecordDay() : null;
    }

    public String getRecordPath() {
        MediaServer def = getDefault();
        return def != null ? def.getRecordPath() : null;
    }

    public String getSdpIp() {
        MediaServer def = getDefault();
        if (def == null) {
            return null;
        }
        String sdpIp = def.getSdpIp();
        if (ObjectUtils.isEmpty(sdpIp)) {
            return def.getIp();
        }
        if (isValidIPAddress(sdpIp)) {
            return sdpIp;
        }
        // 按照域名解析
        try {
            return InetAddress.getByName(sdpIp).getHostAddress();
        } catch (UnknownHostException e) {
            log.error("[获取SDP IP]: 域名解析失败");
            return null;
        }
    }

    public String getStreamIp() {
        MediaServer def = getDefault();
        if (def == null) {
            return null;
        }
        String streamIp = def.getStreamIp();
        if (ObjectUtils.isEmpty(streamIp)) {
            return def.getIp();
        }
        return streamIp;
    }

    /**
     * 返回数据库中的默认流媒体节点
     */
    public MediaServer getMediaSerItem() {
        return getDefault();
    }

    private boolean isValidIPAddress(String ipAddress) {
        if ((ipAddress != null) && (!ipAddress.isEmpty())) {
            return Pattern.matches("^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$", ipAddress);
        }
        return false;
    }
}
