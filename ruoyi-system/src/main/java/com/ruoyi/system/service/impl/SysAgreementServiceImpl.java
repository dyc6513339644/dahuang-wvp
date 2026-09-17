package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.system.domain.SysAgreement;
import com.ruoyi.system.mapper.SysAgreementMapper;
import com.ruoyi.system.service.ISysAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 协议管理Service实现类
 *
 * @author ruoyi
 */
@Service
public class SysAgreementServiceImpl implements ISysAgreementService {

    @Autowired
    private SysAgreementMapper agreementMapper;

    @Override
    public SysAgreement selectAgreementByType(String agreementType) {
        LambdaQueryWrapper<SysAgreement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysAgreement::getAgreementType, agreementType)
               .last("LIMIT 1");
        return agreementMapper.selectOne(wrapper);
    }

    @Override
    public boolean insertAgreement(SysAgreement agreement) {
        return agreementMapper.insert(agreement) > 0;
    }

    @Override
    public boolean updateAgreement(SysAgreement agreement) {
        return agreementMapper.updateById(agreement) > 0;
    }
}
