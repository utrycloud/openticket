package com.utry.openticket.service;

import com.utry.openticket.dao.IPermissionDAO;
import com.utry.openticket.model.PermissionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private IPermissionDAO permissionDAO;

    public List<PermissionDO> getPermissionList() {
        return permissionDAO.getPermissionList();
    }

    public List<PermissionDO> getUserPermissions(Integer userId){
        return permissionDAO.getUserPermissions(userId);
    }

    /**
     * 保存权限
     * @param permissionDO
     */
	public void savePermission(PermissionDO permissionDO) {
		// TODO Auto-generated method stub
		permissionDAO.savePermission(permissionDO);
	}

	/**
	 * 得到PermissionDO通过id
	 * @param id
	 * @return
	 */
	public PermissionDO getPermissionById(Integer id) {
		// TODO Auto-generated method stub
		return permissionDAO.getPermissionById(id);
	}

	/**
	 * 通过id删除
	 * @param parseInt
	 */
	public void delPermissionById(int id) {
		// TODO Auto-generated method stub
		permissionDAO.delPermissionById(id);
	}

	/**
	 * 修改permissionDO
	 * @param permissionDO
	 */
	public void updatePermission(PermissionDO permissionDO) {
		// TODO Auto-generated method stub
		permissionDAO.updatePermission(permissionDO);
	}
}
