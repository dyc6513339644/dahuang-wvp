package com.ruoyi.alarms.domain.ref;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/** 通道名称回查的轻量只读映射，国标名优先取值在 Java 侧完成 */
@Data
@TableName("wvp_device_channel")
public class AlarmChannelRef implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 通道编码：本表历史设计把通道编码存在 device_id 列，不是所属设备编码 */
    private String deviceId;

    /** 所属设备编码：对应 gb_device_id 列 */
    private String gbDeviceId;

    /** 通道名称 */
    private String name;

    /** 国标通道名称（优先展示） */
    private String gbName;
}
