package com.ruoyi.media.service;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.media.domain.MediaServer;
import com.ruoyi.media.domain.SendRtpInfo;
import com.ruoyi.media.zlm.ZLMRESTfulUtils;

import java.util.Map;

/**
 * ZLM 服务操作接口。封装对 ZLMediaKit RESTful API 的调用，
 * 包括 RTP 服务器创建/关闭、推流、SSRC 更新、流就绪检查等。
 */
public interface IZLMServerService {

    /**
     * 开启 rtpServer
     *
     * @param mediaServerItem zlm服务实例
     * @param streamId        流Id
     * @param ssrc            ssrc
     * @param port            端口， 0/null为使用随机
     * @param onlyAuto        是否只拉取音频
     * @param reUsePort       是否重用端口
     * @param tcpMode         0/null udp 模式，1 tcp 被动模式, 2 tcp 主动模式
     * @return 本地端口
     */
    int createRTPServer(MediaServer mediaServerItem, String streamId, long ssrc, Integer port, Boolean onlyAuto, Boolean reUsePort, Integer tcpMode);

    /**
     * 关闭 rtpServer
     */
    boolean closeRtpServer(MediaServer serverItem, String streamId);

    /**
     * 调用 zlm RESTFUL API —— startSendRtp
     */
    JSONObject startSendRtpStream(MediaServer mediaServerItem, Map<String, Object> param);

    /**
     * 调用 zlm RESTFUL API —— startSendRtpPassive
     */
    JSONObject startSendRtpPassive(MediaServer mediaServerItem, Map<String, Object> param);

    /**
     * 调用 zlm RESTFUL API —— startSendRtpPassive（带回调）
     */
    JSONObject startSendRtpPassive(MediaServer mediaServerItem, Map<String, Object> param, ZLMRESTfulUtils.RequestCallback callback);

    /**
     * 查询待转推的流是否就绪
     */
    Boolean isStreamReady(MediaServer mediaServerItem, String app, String streamId);

    /**
     * 查询转推的流是否有其它观看者
     */
    int totalReaderCount(MediaServer mediaServerItem, String app, String streamId);

    /**
     * 调用 zlm RESTFUL API —— startSendRtp（按 SendRtpInfo 构造参数推流）
     */
    JSONObject startSendRtp(MediaServer mediaInfo, SendRtpInfo sendRtpItem);

    /**
     * 更新 RtpServer 的 SSRC
     */
    Boolean updateRtpServerSSRC(MediaServer mediaServerItem, String streamId, String ssrc);

    /**
     * 停止发送 rtp 流
     */
    JSONObject stopSendRtpStream(MediaServer mediaServerItem, SendRtpInfo sendRtpItem);
}
