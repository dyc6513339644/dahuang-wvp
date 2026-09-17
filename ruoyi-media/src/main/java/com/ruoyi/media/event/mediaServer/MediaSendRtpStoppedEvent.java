package com.ruoyi.media.event.mediaServer;

/**
 * 发送流停止事件
 * <p>
 * 继承 {@link MediaServerEventAbstract} 复用 mediaServer 字段，
 * 与 MediaServerOnlineEvent / MediaServerOfflineEvent / MediaServerDeleteEvent 保持类型一致。
 */
public class MediaSendRtpStoppedEvent extends MediaServerEventAbstract {

    public MediaSendRtpStoppedEvent(Object source) {
        super(source);
    }

    private String app;

    private String stream;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
