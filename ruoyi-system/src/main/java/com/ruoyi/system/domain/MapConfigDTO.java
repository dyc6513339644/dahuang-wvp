package com.ruoyi.system.domain;

import lombok.Data;

/**
 * 地图配置 DTO
 *
 * @author ruoyi
 */
@Data
public class MapConfigDTO {

    /** 地图类型：amap（高德）| tianditu（天地图） */
    private String mapType = "amap";

    // ===== 高德地图 =====
    /** 高德地图 KEY */
    private String gaodeMapKey;

    /** 高德地图安全秘钥 */
    private String gaodeMapSecurityCode;

    /** 高德地图中心点坐标（纬度,经度） */
    private String gaodeMapCenter;

    // ===== 天地图 =====
    /** 天地图 KEY */
    private String tiandituMapKey;

    /** 天地图安全秘钥 */
    private String tiandituMapSecurityCode;

    /** 天地图中心点坐标（纬度,经度） */
    private String tiandituMapCenter;
}
