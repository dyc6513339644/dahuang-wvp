package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.large.CountDeviceNum;
import com.ruoyi.system.domain.large.CountGbNum;
import com.ruoyi.system.domain.large.LeftGbDevice;
import com.ruoyi.system.mapper.SysLargeServiceMapper;
import com.ruoyi.system.service.ISysLargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @FileName SysLargeServiceImpl
 * @Description
 * @Author fengcheng
 * @date 2025-05-01
 **/
@Service
public class SysLargeServiceImpl implements ISysLargeService {

    private static final Logger log = LoggerFactory.getLogger(SysLargeServiceImpl.class);

    @Autowired
    private SysLargeServiceMapper sysLargeServiceMapper;


    /**
     * 获取总设备数量
     *
     * @return
     */
    @Override
    public CountDeviceNum countDeviceNum() {
        CountDeviceNum result = new CountDeviceNum();




        result.setTotalGbNum(safeCount(() -> sysLargeServiceMapper.countWvpDevice(), "wvp_device"));
        result.setTotalRtspNum(safeCount(() -> sysLargeServiceMapper.countRtspDevice(), "rtsp_device"));
        result.setTotalOnvifNum(safeCount(() -> sysLargeServiceMapper.countOnvifDevice(), "onvif_device"));
        return result;
    }

    /**
     * 安全查询，表不存在时返回0
     */
    private Integer safeCount(java.util.function.Supplier<Integer> supplier, String tableName) {
        try {
            Integer count = supplier.get();
            return count != null ? count : 0;
        } catch (Exception e) {
            log.warn("查询表 {} 失败: {}", tableName, e.getMessage());
            return 0;
        }
    }

    /**
     * 获取国标总览
     *
     * @return
     */
    @Override
    public CountGbNum countGbNum() {
        return sysLargeServiceMapper.countGbNum();
    }

    /**
     * 获取国标设备提醒
     *
     * @return
     */
    @Override
    public List<LeftGbDevice> leftGbDevice() {
        return sysLargeServiceMapper.leftGbDevice();
    }
}
