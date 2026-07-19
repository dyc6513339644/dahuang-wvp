package com.ruoyi.system.service;

import com.ruoyi.system.domain.WvpUserApiKey;

import java.util.List;

/**
 * 用户API密钥Service接口
 *
 * @author ruoyi
 */
public interface IWvpUserApiKeyService {

    /**
     * 查询用户的所有API密钥列表
     *
     * @param userId 用户ID
     * @return 密钥列表（不包含secretKey）
     */
    List<WvpUserApiKey> listByUserId(Long userId);

    /**
     * 生成新的API密钥（AK/SK）
     *
     * @param userId  用户ID
     * @param appName 应用名称
     * @param remark  备注
     * @param createBy 创建者
     * @return 生成的密钥信息（创建时返回secretKey明文，之后不可再获取）
     */
    WvpUserApiKey generate(Long userId, String appName, String remark, String createBy);

    /**
     * 切换密钥启用/停用状态
     *
     * @param id       密钥ID
     * @param updateBy 操作者
     * @return 是否成功
     */
    boolean toggleEnable(Long id, String updateBy);

    /**
     * 删除密钥
     *
     * @param id 密钥ID
     * @return 是否成功
     */
    boolean delete(Long id);

    /**
     * 根据AccessKey查询密钥信息（用于API鉴权）
     *
     * @param accessKey Access Key
     * @return 密钥信息（含secretKey）
     */
    WvpUserApiKey getByAccessKey(String accessKey);
}
