package com.ruoyi.wvp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.wvp.gb28181.bean.SipBlackEntity;

import java.util.List;

/**
 * SIP 黑名单服务接口
 *
 * @author ruoyi-wvp
 */
public interface ISipBlackService {

    /**
     * 查询黑名单列表
     */
    List<SipBlackEntity> list();

    /**
     * 根据ID查询
     */
    SipBlackEntity getById(Long id);

    /**
     * 根据设备ID查询黑名单
     */
    SipBlackEntity getByDeviceId(String deviceId);

    /**
     * 新增
     */
    boolean add(SipBlackEntity entity);

    /**
     * 更新
     */
    boolean update(SipBlackEntity entity);

    /**
     * 删除
     */
    boolean delete(Long id);

    /**
     * 检查设备是否在黑名单中（支持IP和端口匹配）
     * <p>
     * 匹配规则：
     * <ul>
     *   <li>先按 deviceId 查找黑名单条目</li>
     *   <li>若条目中填了 IP，则必须与请求 IP 一致才拦截</li>
     *   <li>若条目中填了 port，则必须与请求端口一致才拦截</li>
     *   <li>IP 和 port 都未填的条目，仅凭 deviceId 即拦截</li>
     * </ul>
     *
     * @param deviceId 设备ID
     * @param ip       请求来源IP（可为 null）
     * @param port     请求来源端口（可为 null）
     * @return true=在黑名单中, false=不在
     */
    boolean isBlacklisted(String deviceId, String ip, Integer port);
}
