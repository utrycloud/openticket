package com.utry.openticket.dao;

import com.utry.openticket.dto.RoleDTO;
import com.utry.openticket.model.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleDAO {
    List<RoleDO> getRoleList();

    List<RoleDTO> getRoleList2();

    int addRole(RoleDO roleDO);

    int updateRoleById(RoleDO roleDO);

    int deleteRoleById(Integer id);

    List<RoleDO> getRoleByUserId(Integer userId);

    /**
     * 删除用户的所有角色
     * @param userId 用户id
     * @return
     */
    int deleteRoleByUserId(Integer userId);

    /**
     * 设置用户角色
     * @param userId 用户id
     * @param roleId 角色id
     * @return
     */
    int addUserRole(@Param("userId") Integer userId,@Param("roleId") Integer roleId);

    /**
     * 删除角色的所有权限
     * @param roleId 角色id
     * @return
     */
    int deleteRolePermission(Integer roleId);

    /**
     * 删除角色时根据角色id删除用户角色关系
     * @param roleId id
     * @return
     */
    int deleteUserRoleByRoleId(Integer roleId);

    /**
     * 设置角色权限,
     * @param roleId
     * @param permissionId
     * @return
     */
    int insertRolePermission(@Param("roleId")Integer roleId,@Param("permissionId")Integer permissionId);
}
