package com.ruoyi.media.event.mediaServer;

import com.ruoyi.media.domain.MediaServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 媒体服务器节点列表变更事件
 * <p>
 * 继承 {@link MediaServerEventAbstract} 以统一「媒体服务器事件」的类型体系。
 * 本事件携带的是节点列表，基类的单个 mediaServer 字段对本事件不适用，不使用。
 */
public class MediaServerChangeEvent extends MediaServerEventAbstract {

    public MediaServerChangeEvent(Object source) {
        super(source);
    }

    private List<MediaServer> mediaServerItemList;

    public List<MediaServer> getMediaServerItemList() {
        return mediaServerItemList;
    }

    public void setMediaServerItemList(List<MediaServer> mediaServerItemList) {
        this.mediaServerItemList = mediaServerItemList;
    }

    public void setMediaServerItemList(MediaServer... mediaServerItemArray) {
        this.mediaServerItemList = new ArrayList<>();
        this.mediaServerItemList.addAll(Arrays.asList(mediaServerItemArray));
    }

    public void setMediaServerItem(List<MediaServer> mediaServerItemList) {
        this.mediaServerItemList = mediaServerItemList;
    }
}
