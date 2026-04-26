package com.ruoyi.rtsp.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


import ai.onnxruntime.OrtException;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.rtsp.constants.StreamTypeConstants;
import com.ruoyi.rtsp.domain.bo.RtspDeviceBo;
import com.ruoyi.rtsp.domain.bo.alarmClockBo;
import com.ruoyi.rtsp.tool.RtspTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.rtsp.mapper.RtspDeviceMapper;
import com.ruoyi.rtsp.domain.RtspDevice;
import com.ruoyi.rtsp.service.IRtspDeviceService;

/**
 * rtsp设备Service业务层处理
 *
 * @author 陈江灿
 * @date 2025-04-22
 */
@Service
public class RtspDeviceServiceImpl implements IRtspDeviceService {
    @Autowired
    private RtspDeviceMapper rtspDeviceMapper;

    /**
     * 查询rtsp设备
     *
     * @param id rtsp设备主键
     * @return rtsp设备
     */
    @Override
    public RtspDevice selectRtspDeviceById(Long id) {
        return rtspDeviceMapper.selectRtspDeviceById(id);
    }

    /**
     * 查询rtsp设备列表
     *
     * @param rtspDevice rtsp设备
     * @return rtsp设备
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<RtspDevice> selectRtspDeviceList(RtspDevice rtspDevice) {
        return rtspDeviceMapper.selectRtspDeviceList(rtspDevice);
    }

    /**
     * 新增rtsp设备
     *
     * @param rtspDevice rtsp设备
     * @return 结果
     */
    @Override
    public int insertRtspDevice(RtspDevice rtspDevice) {
        // 生成rtsp地址
        String rtspUrl = RtspTool.generateRtspUrl(rtspDevice);
        if (rtspDevice != null) {
            rtspDevice.setUrl(rtspUrl);
        }
        rtspDevice.setCreateTime(DateUtils.getNowDate());
        return rtspDeviceMapper.insertRtspDevice(rtspDevice);
    }

    /**
     * 修改rtsp设备
     *
     * @param rtspDevice rtsp设备
     * @return 结果
     */
    @Override
    public int updateRtspDevice(RtspDevice rtspDevice) {
        // 生成rtsp地址
        String rtspUrl = RtspTool.generateRtspUrl(rtspDevice);
        if (rtspDevice != null) {
            rtspDevice.setUrl(rtspUrl);
        }
        rtspDevice.setUpdateTime(DateUtils.getNowDate());
        return rtspDeviceMapper.updateRtspDevice(rtspDevice);
    }

    /**
     * 批量删除rtsp设备
     *
     * @param ids 需要删除的rtsp设备主键
     * @return 结果
     */
    @Override
    public int deleteRtspDeviceByIds(Long[] ids) {
        return rtspDeviceMapper.deleteRtspDeviceByIds(ids);
    }

    /**
     * 删除rtsp设备信息
     *
     * @param id rtsp设备主键
     * @return 结果
     */
    @Override
    public int deleteRtspDeviceById(Long id) {
        return rtspDeviceMapper.deleteRtspDeviceById(id);
    }

    /**
     * 新增目标检测
     *
     * @param bo
     */
    @Override
    public void insertRtspDetection(RtspDeviceBo bo) throws OrtException, IOException {
    }

    @Override
    public void stopRtspDetection(RtspDeviceBo bo) throws OrtException, IOException {
    }

    @Override
    public String alarmClock(alarmClockBo bo) {
        RtspDevice device = rtspDeviceMapper.selectRtspDeviceById(bo.getId());
        String firm = device.getFirm();
        if (firm.equals(StreamTypeConstants.HIKVISION)) {
            String startTime = this.formatAlarmTimeHIKVISION(bo.getStartTime());
            String endTime = this.formatAlarmTimeHIKVISION(bo.getEndTime());
            String url = "rtsp://" + device.getUserName() + ":" + device.getPassword() + "@" + device.getIp() + ":554/Streaming/tracks/" +
                    device.getChannel() + "?starttime=" + startTime + "&endtime=" + endTime;
            return url;
        } else if (firm.equals(StreamTypeConstants.DAHUA)) {
            String startTime = this.formatAlarmTimeDaHua(bo.getStartTime());
            String endTime = this.formatAlarmTimeDaHua(bo.getEndTime());
            String url = "rtsp://" + device.getUserName() + ":" + device.getPassword() + "@" + device.getIp() + ":554/cam/playback?channel=" +
                    device.getChannel() + "&subtype=1&starttime=" + startTime + "&endtime=" + endTime;
            return url;
        }
        throw new RuntimeException("暂不支持该厂商");
    }

    /**
     * 查询地图rtsp设备列表
     *
     * @param rtspDevice
     * @return
     */
    @Override
    public List<RtspDevice> selectRtspDeviceListMap(RtspDevice rtspDevice) {
        return rtspDeviceMapper.selectRtspDeviceListMap(rtspDevice);
    }

    /**
     * 大华-格式化时间
     * @param date
     * @return
     */
    public static String formatAlarmTimeDaHua(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return sdf.format(date);
    }

    /**
     * HIKVISION-格式化时间
     * @param date
     * @return
     */
    public static String formatAlarmTimeHIKVISION(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }


}
