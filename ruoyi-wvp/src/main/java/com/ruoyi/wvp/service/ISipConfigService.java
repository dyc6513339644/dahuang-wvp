package com.ruoyi.wvp.service;

import com.ruoyi.wvp.gb28181.bean.SipConfigEntity;

import java.util.List;

/**
 * SIP配置服务接口
 *
 * @author ruoyi-wvp
 */
public interface ISipConfigService {

    /**
     * 获取SIP配置
     *
     * @return SIP配置
     */
    SipConfigEntity getSipConfig();

    /**
     * 根据ID获取SIP配置
     *
     * @param id 配置ID
     * @return SIP配置
     */
    SipConfigEntity getSipConfigById(Long id);

    /**
     * 保存SIP配置
     *
     * @param sipConfig SIP配置
     * @return 是否成功
     */
    boolean save(SipConfigEntity sipConfig);

    /**
     * 更新SIP配置
     *
     * @param sipConfig SIP配置
     * @return 是否成功
     */
    boolean update(SipConfigEntity sipConfig);

    /**
     * 获取所有SIP配置列表
     *
     * @return SIP配置列表
     */
    List<SipConfigEntity> getAll();

    /**
     * 删除SIP配置
     *
     * @param id 配置ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 检查是否存在配置
     *
     * @return 是否存在
     */
    boolean exists();
}
