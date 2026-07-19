package com.ruoyi.system.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.common.core.domain.entity.SysRole;

/**
 * 角色表 数据层
 * 
 * @author ruoyi
 */
public interface SysRoleMapper extends BaseMapper<SysRole>
{
    /**
     * 根据条件分页查询角色数据（多表JOIN + DataScope）
     */
    List<SysRole> selectRoleList(SysRole role);

    /**
     * 根据用户ID查询角色（多表JOIN）
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查询所有角色（多表JOIN）
     */
    List<SysRole> selectRoleAll();

    /**
     * 根据用户ID获取角色选择框列表（多表JOIN）
     */
    List<Long> selectRoleListByUserId(Long userId);

    /**
     * 通过角色ID查询角色（多表JOIN）
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 根据用户ID查询角色（多表JOIN）
     */
    List<SysRole> selectRolesByUserName(String userName);
}
