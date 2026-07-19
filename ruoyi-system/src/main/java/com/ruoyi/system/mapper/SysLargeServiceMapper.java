package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.large.CountDeviceNum;
import com.ruoyi.system.domain.large.CountGbNum;
import com.ruoyi.system.domain.large.LeftGbDevice;

import java.util.List;

/**
 * @FileName SysLargeServiceMapper
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
public interface SysLargeServiceMapper {

    /**
     * 获取总设备数量
     * @return
     */
    CountDeviceNum countDeviceNum();

    /**
     * 获取国标总览
     *
     * @return
     */
    CountGbNum countGbNum();

    /**
     * 获取国标设备提醒
     *
     * @return
     */
    List<LeftGbDevice> leftGbDevice();

    /**
     * 单独查询各表数量
     */
    Integer countWvpDevice();
    Integer countRtspDevice();
    Integer countOnvifDevice();
}
