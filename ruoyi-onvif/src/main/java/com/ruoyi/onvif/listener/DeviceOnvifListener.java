package com.ruoyi.onvif.listener;

import com.ruoyi.onvif.domain.DeviceOnvif;
import com.ruoyi.onvif.domain.DeviceOnvifChannel;
import com.ruoyi.onvif.event.DeviceOnvifEvent;
import com.ruoyi.onvif.service.IDeviceOnvifChannelService;
import com.ruoyi.onvif.service.IDeviceOnvifService;
import com.ruoyi.onvif.utils.OnvifUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ONVIF设备添加事件监听器
 */
@Component
public class DeviceOnvifListener implements ApplicationListener<DeviceOnvifEvent> {

    private static final Logger log = LoggerFactory.getLogger(DeviceOnvifListener.class);

    @Autowired
    private IDeviceOnvifService deviceOnvifService;

    @Autowired
    private IDeviceOnvifChannelService deviceOnvifChannelService;

    /** ONVIF 数据类型标识 */
    private static final long DATA_TYPE_ONVIF = 4L;

    /** WS-Security UsernameToken 鉴权标识 */
    private static final String AUTH_WS_TOKEN = "1";

    @Override
    @Async
    public void onApplicationEvent(DeviceOnvifEvent event) {
        // 1. 查询设备信息
        DeviceOnvif deviceOnvif = deviceOnvifService.selectDeviceOnvifById(event.getId());
        if (deviceOnvif == null) {
            return;
        }
        try {

            OnvifUtils.DeviceInformation deviceInformation= OnvifUtils.getDeviceInformation(deviceOnvif.getIp(),deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword());

            // 4. 更新设备基本信息
            deviceOnvif.setDbModel(deviceInformation.getDbModel());
            deviceOnvif.setManufacturer(deviceInformation.getManufacturer());
            deviceOnvif.setFirmware(deviceInformation.getFirmwareVersion());
            deviceOnvif.setOnLine(true);
            deviceOnvifService.updateDeviceOnvif(deviceOnvif);
            // 5. 创建/更新通道记录
            List<OnvifUtils.ProfileInfo> profileInfoList=OnvifUtils.getProfiles(deviceOnvif.getIp(),deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword());

            if (profileInfoList != null && !profileInfoList.isEmpty()) {
                boolean isAdd = DeviceOnvifEvent.EventType.ADD == event.getType();

                if (isAdd) {
                    deviceOnvifChannelService.deleteDeviceOnvifChannelByDataDeviceId(deviceOnvif.getId());
                }

                for(int i = 0; i< profileInfoList.size(); i++){
                    String token = profileInfoList.get(i).getToken();
                    String channelName = profileInfoList.get(i).getName();
                    String channelDeviceId = deviceOnvif.getDeviceId() + String.format("%02d", i+1);
                    String uri=OnvifUtils.getStreamUri(deviceOnvif.getIp(),deviceOnvif.getPort().intValue(), deviceOnvif.getUserName(), deviceOnvif.getPassword(), token);
                    // 添加认证信息到 RTSP URL
                    String uriWithAuth = uri.replace("rtsp://",
                            "rtsp://" + deviceOnvif.getUserName() + ":" + deviceOnvif.getPassword() + "@");
                    DeviceOnvifChannel channel = buildChannel(deviceOnvif, channelDeviceId, channelName, token, uriWithAuth);

                    if (isAdd) {
                        deviceOnvifChannelService.insertDeviceOnvifChannel(channel);
                    } else {
                        DeviceOnvifChannel query = new DeviceOnvifChannel();
                        query.setDeviceId(channelDeviceId);
                        query.setDataDeviceId(deviceOnvif.getId());
                        List<DeviceOnvifChannel> existList = deviceOnvifChannelService.selectDeviceOnvifChannelList(query);
                        if (existList != null && !existList.isEmpty()) {
                            channel.setId(existList.get(0).getId());
                            deviceOnvifChannelService.updateDeviceOnvifChannel(channel);
                        } else {
                            deviceOnvifChannelService.insertDeviceOnvifChannel(channel);
                        }
                    }

                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            if (event.getType() != DeviceOnvifEvent.EventType.REFRESH_CHANNELS) {
                try {
                    deviceOnvif.setOnLine(false);
                    deviceOnvifService.updateDeviceOnvif(deviceOnvif);
                } catch (Exception ex) {
                    // ignore
                }
            }
        }
    }



    /**
     * 构建通道对象
     */
    private DeviceOnvifChannel buildChannel(DeviceOnvif device, String channelDeviceId,
                                             String channelName, String channelNo, String srcUrl) {
        DeviceOnvifChannel channel = new DeviceOnvifChannel();
        channel.setDeviceId(channelDeviceId);
        channel.setName(channelName);
        channel.setManufacturer(device.getManufacturer());
        channel.setDbModel(device.getDbModel());
        channel.setStatus("ON");
        channel.setParentId(device.getDeviceId());
        channel.setChannelNo(channelNo);
        channel.setSrcUrl(srcUrl);
        channel.setDataType(DATA_TYPE_ONVIF);
        channel.setDataDeviceId(device.getId());
        channel.setCreateTime(new java.util.Date());
        channel.setUpdateTime(new java.util.Date());
        channel.setSubCount(0L);
        return channel;
    }
}
