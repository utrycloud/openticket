package com.utry.openticket.service;

import com.utry.openticket.dao.IPermissionDAO;
import com.utry.openticket.dao.RoleDAO;
import com.utry.openticket.dto.RoleDTO;
import com.utry.openticket.model.RoleDO;
import com.utry.openticket.model.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleDAO roleDAO;

    public JsonResult getRoleList(){
        List<RoleDO> list = roleDAO.getRoleList();
        return JsonResult.success(list);
    }

    public JsonResult addRole(RoleDO roleDO){
        roleDAO.addRole(roleDO);
        return JsonResult.success();
    }

    public JsonResult updateRoleById(RoleDO roleDO){
        roleDAO.updateRoleById(roleDO);
        return JsonResult.success();
    }

    public JsonResult deleteRoleById(Integer id){
        roleDAO.deleteRoleById(id);
        //删除用户角色
        roleDAO.deleteUserRoleByRoleId(id);
        //删除角色对应的权限
        roleDAO.deleteRolePermission(id);
        return JsonResult.success();
    }

    public JsonResult getRoleDTOList(){
        List<RoleDTO> list = roleDAO.getRoleList2();
        return JsonResult.success(list);
    }

    public JsonResult getRoleByUserId(Integer userId){
        List<RoleDO> result = roleDAO.getRoleByUserId(userId);
        return JsonResult.success(result);
    }
}
