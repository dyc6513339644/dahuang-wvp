package com.ruoyi.rtsp.domain.bo;

/**
 * ai 播放bo
 *
 * @Author: 陈江灿
 * @CreateTime: 2025-04-28
 */
public class RtspDeviceBo {
    private String type;
    private String url;
    private String mediaId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return "RtspDeviceBo{" +
                "type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", mediaId='" + mediaId + '\'' +
                '}';
    }
}
