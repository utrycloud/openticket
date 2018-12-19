package com.utry.openticket.dao;

import com.utry.openticket.model.PermissionDO;
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
}
