package com.ruoyi.wvp.media;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.wvp.media.bean.MediaServer;
import com.ruoyi.wvp.media.event.mediaServer.MediaServerChangeEvent;
import com.ruoyi.wvp.media.service.IMediaServerService;
import com.ruoyi.wvp.media.zlm.ZLMRESTfulUtils;
import com.ruoyi.wvp.media.zlm.dto.ZLMServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动时从数据库加载ZLM节点信息，调用ZLM接口获取最新配置，写回数据库，并发送节点状态管理事件
 */
@Slf4j
@Component
@Order(value=12)
public class MediaServerConfig implements CommandLineRunner {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private IMediaServerService mediaServerService;

    @Autowired
    private ZLMRESTfulUtils zlmresTfulUtils;


    @Override
    public void run(String... strings) throws Exception {
        // 清理所有在线节点的缓存信息
        mediaServerService.clearMediaServerForOnline();

        // 从数据库获取所有ZLM节点
        List<MediaServer> allFromDatabase = mediaServerService.getAllFromDatabase();
        log.info("[媒体节点] 从数据库加载节点列表，共{}个节点", allFromDatabase.size());

        if (allFromDatabase.isEmpty()) {
            log.warn("[媒体节点] 数据库中无ZLM节点记录，请通过管理页面添加媒体服务器节点");
        } else {
            // 遍历每个节点，调用ZLM API获取最新配置并更新数据库
            for (MediaServer mediaServerItem : allFromDatabase) {
                try {
                    JSONObject responseJson = zlmresTfulUtils.getMediaServerConfig(mediaServerItem);
                    if (responseJson != null) {
                        JSONArray data = responseJson.getJSONArray("data");
                        if (data != null && !data.isEmpty()) {
                            ZLMServerConfig zlmServerConfig = JSON.parseObject(JSON.toJSONString(data.get(0)), ZLMServerConfig.class);
                            // 用ZLM返回值填充数据库中未配置（值为0）的端口
                            initPort(mediaServerItem, zlmServerConfig);
                            mediaServerItem.setStatus(false);
                            mediaServerService.update(mediaServerItem);
                            log.info("[媒体节点] 从ZLM获取配置成功并更新数据库, ID={}, {}:{}",
                                    mediaServerItem.getId(), mediaServerItem.getIp(), mediaServerItem.getHttpPort());
                        } else {
                            log.warn("[媒体节点] ZLM返回数据为空, ID={}, {}:{}",
                                    mediaServerItem.getId(), mediaServerItem.getIp(), mediaServerItem.getHttpPort());
                            mediaServerItem.setStatus(false);
                            mediaServerService.update(mediaServerItem);
                        }
                    } else {
                        log.warn("[媒体节点] 连接ZLM失败, ID={}, {}:{}",
                                mediaServerItem.getId(), mediaServerItem.getIp(), mediaServerItem.getHttpPort());
                        mediaServerItem.setStatus(false);
                        mediaServerService.update(mediaServerItem);
                    }
                } catch (Exception e) {
                    log.error("[媒体节点] 连接ZLM异常, ID={}, {}:{}, 异常: {}",
                            mediaServerItem.getId(), mediaServerItem.getIp(), mediaServerItem.getHttpPort(), e.getMessage());
                    mediaServerItem.setStatus(false);
                    mediaServerService.update(mediaServerItem);
                }
            }
        }

        // 同步Redis缓存，清除数据库中不存在的孤立缓存
        mediaServerService.syncCatchFromDatabase();

        // 获取所有的zlm，并开启主动连接
        List<MediaServer> all = mediaServerService.getAllFromDatabase();
        log.info("[媒体节点] 加载节点列表，共{}个节点", all.size());
        MediaServerChangeEvent event = new MediaServerChangeEvent(this);
        event.setMediaServerItemList(all);
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * 端口只会从配置中读取一次，一旦自己配置或者读取过了将不再配置
     */
    private void initPort(MediaServer mediaServerItem, ZLMServerConfig zlmServerConfig) {
        if (mediaServerItem.getHttpSSlPort() == 0) {
            mediaServerItem.setHttpSSlPort(zlmServerConfig.getHttpSSLport());
        }
        if (mediaServerItem.getRtmpPort() == 0) {
            mediaServerItem.setRtmpPort(zlmServerConfig.getRtmpPort());
        }
        if (mediaServerItem.getRtmpSSlPort() == 0) {
            mediaServerItem.setRtmpSSlPort(zlmServerConfig.getRtmpSslPort());
        }
        if (mediaServerItem.getRtspPort() == 0) {
            mediaServerItem.setRtspPort(zlmServerConfig.getRtspPort());
        }
        if (mediaServerItem.getRtspSSLPort() == 0) {
            mediaServerItem.setRtspSSLPort(zlmServerConfig.getRtspSSlport());
        }
        if (mediaServerItem.getRtpProxyPort() == 0) {
            mediaServerItem.setRtpProxyPort(zlmServerConfig.getRtpProxyPort());
        }
        if (mediaServerItem.getFlvSSLPort() == 0) {
            mediaServerItem.setFlvSSLPort(zlmServerConfig.getHttpSSLport());
        }
        if (mediaServerItem.getWsFlvSSLPort() == 0) {
            mediaServerItem.setWsFlvSSLPort(zlmServerConfig.getHttpSSLport());
        }
        mediaServerItem.setHookAliveInterval(10F);
    }
}
