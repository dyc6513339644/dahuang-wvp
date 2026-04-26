package com.ruoyi.rtsp.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * rtsp设备对象 rtsp_device
 *
 * @author 陈江灿
 * @date 2025-04-22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RtspDevice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * ip
     */
    @Excel(name = "ip")
    private String ip;

    /**
     * 摄像头名称
     */
    @Excel(name = "摄像头名称")
    private String name;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    /**
     * 通道号
     */
    @Excel(name = "通道号")
    private String channel;

    /**
     * 播放类型（1=本地，2=推流，3=EasyNTS）
     */
    @Excel(name = "播放类型")
    private String playType;

    /**
     * 直播流播放地址
     */
    @Excel(name = "直播流播放地址")
    private String url;

    /**
     * 流id
     */
    @Excel(name = "流id")
    private String streamId;

    /**
     * 直播流播放地址
     */
    @Excel(name = "EasyNTS播放地址")
    private String easyNTSUrl;

    /**
     * 设备厂商
     */
    @Excel(name = "设备厂商")
    private String firm;

    /**
     * 部门名称
     */
    private String deptName;

    /** 纬度 */
    @Excel(name = "纬度")
    private String lat;

    /** 经度 */
    @Excel(name = "经度")
    private String lng;

    /** 地图定位地址 */
    @Excel(name = "地图定位地址")
    private String addressMap;
}
