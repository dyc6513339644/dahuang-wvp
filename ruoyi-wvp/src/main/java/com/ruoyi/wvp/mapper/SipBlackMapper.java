package com.ruoyi.wvp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.wvp.gb28181.bean.SipBlackEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * SIP 黑名单 Mapper 接口
 *
 * @author ruoyi-wvp
 */
@Mapper
@Repository
public interface SipBlackMapper extends BaseMapper<SipBlackEntity> {
}
