package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 协议管理实体类 sys_agreement
 * <p>
 * 用于存储「用户协议」「隐私政策」等需要后台编辑、移动端动态展示的协议内容。
 *
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_agreement")
public class SysAgreement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 协议类型常量：用户协议 */
    public static final String TYPE_AGREEMENT = "agreement";

    /** 协议类型常量：隐私政策 */
    public static final String TYPE_PRIVACY = "privacy";

    /** 协议ID */
    @TableId(value = "agreement_id", type = IdType.AUTO)
    private Long agreementId;

    /** 协议类型（agreement用户协议 privacy隐私政策） */
    private String agreementType;

    /** 协议名称 */
    private String agreementName;

    /** 协议内容（富文本HTML） */
    private String content;

}
