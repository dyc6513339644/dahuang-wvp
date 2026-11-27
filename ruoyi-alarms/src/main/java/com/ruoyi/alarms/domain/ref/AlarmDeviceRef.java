package com.ruoyi.alarms.domain.ref;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/** 设备名称回查的轻量只读映射，避免本模块依赖 ruoyi-wvp */
@Data
@TableName("wvp_device")
public class AlarmDeviceRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键（仅用于满足 MyBatis-Plus 的实体主键校验，查询时不取该列） */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 设备编码 */
    private String deviceId;

    /** 设备名称 */
    private String name;

    /** 自定义名称（优先展示） */
    private String customName;
}
