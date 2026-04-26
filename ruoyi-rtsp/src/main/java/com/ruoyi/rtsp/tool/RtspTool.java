package com.ruoyi.rtsp.tool;

import com.ruoyi.rtsp.constants.StreamTypeConstants;
import com.ruoyi.rtsp.domain.RtspDevice;

/**
 * @Description: rtsp工具类
 * @Author: 陈江灿
 * @CreateTime: 2025-04-22
 */
public class RtspTool {


    /**
     * 生成rtsp地址
     * @return
     */
    public static String generateRtspUrl(RtspDevice rtspDevice) {
        String username = rtspDevice.getUserName();
        String password = rtspDevice.getPassword();
        String ip = rtspDevice.getIp();
        String channel = rtspDevice.getChannel();
        String firm = rtspDevice.getFirm();
        String port = "554";
        if (firm.equals(StreamTypeConstants.HIKVISION)) {
            return String.format("rtsp://%s:%s@%s:%s/Streaming/Channels/%s", username, password, ip, port, channel);
        } else if (firm.equals(StreamTypeConstants.DAHUA)) {
            return String.format("rtsp://%s:%s@%s:%s/cam/realmonitor?channel=%s&subtype=0", username, password, ip, port, channel);
        } else if (firm.equals(StreamTypeConstants.UNIVIEW)) {
            return String.format("rtsp://%s:%s@%s:%s/media/video%s", username, password, ip, port, channel);
        } else if (firm.equals(StreamTypeConstants.MERCURY)) {
            return String.format("rtsp://%s:%s@%s:%s/stream%s", username, password, ip, port, channel);
        }
        return null;
    }
}
