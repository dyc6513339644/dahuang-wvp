package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysBaseConfig;

/**
 * 基础配置Service接口
 *
 * @author ruoyi
 */
public interface ISysBaseConfigService {

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    SysBaseConfig getBaseConfig();

    /**
     * 保存基础配置（新增）
     *
     * @param config 基础配置
     * @return 是否成功
     */
    boolean save(SysBaseConfig config);

    /**
     * 更新基础配置
     *
     * @param config 基础配置
     * @return 是否成功
     */
    boolean update(SysBaseConfig config);

    /**
     * 是否存在配置记录
     *
     * @return 是否存在
     */
    boolean exists();
}
