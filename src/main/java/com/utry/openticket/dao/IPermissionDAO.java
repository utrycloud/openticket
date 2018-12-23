package com.utry.openticket.dao;

import com.utry.openticket.model.PermissionDO;
import com.utry.openticket.model.vo.JsonResult;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPermissionDAO {
    /**
     * 获得所有权限
     * @return
     */
    List<PermissionDO> getPermissionList();

    /**
     * 获得用户权限
     * @return
     */
    List<PermissionDO> getUserPermissions(Integer userId);

    /**
     * 保存权限
     * @param permissionDO
     */
	void savePermission(PermissionDO permissionDO);

	/**
	 * 获得PermissionDO 对象
 	 * @param id
	 * @return
	 */
	PermissionDO getPermissionById(Integer id);

	/**
	 * 通过id删除
	 * @param id
	 */
	void delPermissionById(int id);

	/**
	 * 更新permissionDO
	 * @param permissionDO
	 */
	void updatePermission(PermissionDO permissionDO);

	/**
	 * 获得角色拥有的权限
	 * @param roleId 角色id
	 * @return
	 */
	List<PermissionDO> getByRoleId(Integer roleId);

	/**
	 * 删除权限关联的角色权限关系表中的数据
	 * @param permissionId
	 * @return
	 */
	int delRolePermissionByPermissionId(int permissionId);

	/**
	 * 
	 * @param pareId
	 * @return
	 */
	List<PermissionDO> getChildPermission(String pareId);
}
