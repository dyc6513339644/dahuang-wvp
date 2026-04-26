package com.ruoyi.rtsp.service;

import java.io.IOException;
import java.util.List;

import ai.onnxruntime.OrtException;
import com.ruoyi.rtsp.domain.RtspDevice;
import com.ruoyi.rtsp.domain.bo.RtspDeviceBo;
import com.ruoyi.rtsp.domain.bo.alarmClockBo;

/**
 * rtsp设备Service接口
 *
 * @author 陈江灿
 * @date 2025-04-22
 */
public interface IRtspDeviceService {
    /**
     * 查询rtsp设备
     *
     * @param id rtsp设备主键
     * @return rtsp设备
     */
    public RtspDevice selectRtspDeviceById(Long id);

    /**
     * 查询rtsp设备列表
     *
     * @param rtspDevice rtsp设备
     * @return rtsp设备集合
     */
    public List<RtspDevice> selectRtspDeviceList(RtspDevice rtspDevice);

    /**
     * 新增rtsp设备
     *
     * @param rtspDevice rtsp设备
     * @return 结果
     */
    public int insertRtspDevice(RtspDevice rtspDevice);

    /**
     * 修改rtsp设备
     *
     * @param rtspDevice rtsp设备
     * @return 结果
     */
    public int updateRtspDevice(RtspDevice rtspDevice);

    /**
     * 批量删除rtsp设备
     *
     * @param ids 需要删除的rtsp设备主键集合
     * @return 结果
     */
    public int deleteRtspDeviceByIds(Long[] ids);

    /**
     * 删除rtsp设备信息
     *
     * @param id rtsp设备主键
     * @return 结果
     */
    public int deleteRtspDeviceById(Long id);


    /**
     * 新增目标检测新增目标检测
     *
     * @param rtspDevice
     */
    public void insertRtspDetection(RtspDeviceBo rtspDevice) throws OrtException, IOException;

    /**
     * 停止检测/推流
     * @param rtspDevice
     * @throws OrtException
     * @throws IOException
     */
    public void stopRtspDetection(RtspDeviceBo rtspDevice) throws OrtException, IOException;



    /**
     * 历史播放
     * @param bo
     * @return
     */
    String alarmClock(alarmClockBo bo);

    /**
     * 查询地图rtsp设备列表
     *
     * @param rtspDevice
     * @return
     */
    List<RtspDevice> selectRtspDeviceListMap(RtspDevice rtspDevice);
}
