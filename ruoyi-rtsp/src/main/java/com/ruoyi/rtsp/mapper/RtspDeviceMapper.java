package com.ruoyi.rtsp.mapper;

import java.util.List;
import com.ruoyi.rtsp.domain.RtspDevice;

/**
 * rtsp设备Mapper接口
 * 
 * @author 陈江灿
 * @date 2025-04-22
 */
public interface RtspDeviceMapper 
{
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
     * 删除rtsp设备
     * 
     * @param id rtsp设备主键
     * @return 结果
     */
    public int deleteRtspDeviceById(Long id);

    /**
     * 批量删除rtsp设备
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRtspDeviceByIds(Long[] ids);

    /**
     * 查询地图rtsp设备列表
     *
     * @param rtspDevice
     * @return
     */
    List<RtspDevice> selectRtspDeviceListMap(RtspDevice rtspDevice);
}
