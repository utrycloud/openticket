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
}
