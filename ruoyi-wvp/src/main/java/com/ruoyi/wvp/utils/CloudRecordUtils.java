package com.ruoyi.wvp.utils;

import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.domain.DownloadFileInfo;
import org.apache.commons.lang3.StringUtils;

public class CloudRecordUtils {

    public static DownloadFileInfo getDownloadFilePath(MediaServer mediaServerItem, String filePath) {
        DownloadFileInfo downloadFileInfo = new DownloadFileInfo();

        String pathTemplate = "%s://%s:%s/index/api/downloadFile?file_path=" + filePath;

        downloadFileInfo.setHttpPath(String.format(pathTemplate, "http", mediaServerItem.getStreamIp(),
                mediaServerItem.getHttpPort()));

        if (mediaServerItem.getHttpSSlPort() > 0) {
            downloadFileInfo.setHttpsPath(String.format(pathTemplate, "https", mediaServerItem.getStreamIp(),
                    mediaServerItem.getHttpSSlPort()));
        }
        return downloadFileInfo;
    }

    /**
     * 对 DownloadFileInfo 填充 nginx 域名代理路径（httpDomainPath / httpsDomainPath）
     * @param info         原始 DownloadFileInfo
     * @param mediaServer  媒体服务器（需含 nginx 代理配置）
     * @param filePath     文件路径（用于拼接 URL）
     * @param domain       对外域名
     */
    public static void fillNginxDomainPaths(DownloadFileInfo info, MediaServer mediaServer,
                                            String filePath, String domain) {
        if (mediaServer == null || !mediaServer.isNginxProxyEnabled() || StringUtils.isBlank(domain)) {
            return;
        }
        String pathTemplate = "%s://%s:%s/index/api/downloadFile?file_path=" + filePath;
        if (mediaServer.getNginxHttpPort() != null) {
            info.setHttpDomainPath(String.format(pathTemplate, "http", domain, mediaServer.getNginxHttpPort()));
        }
        if (mediaServer.getNginxHttpsPort() != null) {
            info.setHttpsDomainPath(String.format(pathTemplate, "https", domain, mediaServer.getNginxHttpsPort()));
        }
    }
}
