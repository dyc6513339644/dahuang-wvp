package com.ruoyi.system.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
public interface SysUserMapper extends BaseMapper<SysUser>
{
    /**
     * 根据条件分页查询用户列表
     */
    List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 根据条件分页查询已配用户角色列表
     */
    List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     */
    List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     */
    SysUser selectUserById(Long userId);

    /**
     * 通过用户ID删除用户（逻辑删除）
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息（逻辑删除）
     */
    int deleteUserByIds(Long[] userIds);
}
