package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysAgreement;

/**
 * 协议管理Service接口
 *
 * @author ruoyi
 */
public interface ISysAgreementService {

    /**
     * 根据协议类型查询协议（agreement用户协议 / privacy隐私政策）
     *
     * @param agreementType 协议类型
     * @return 协议信息，不存在返回 null
     */
    SysAgreement selectAgreementByType(String agreementType);

    /**
     * 新增协议
     *
     * @param agreement 协议信息
     * @return 是否成功
     */
    boolean insertAgreement(SysAgreement agreement);

    /**
     * 修改协议
     *
     * @param agreement 协议信息
     * @return 是否成功
     */
    boolean updateAgreement(SysAgreement agreement);
}
