package com.utry.openticket.controller;

import com.utry.openticket.model.RoleDO;
import com.utry.openticket.model.vo.JsonResult;
import com.utry.openticket.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

@Controller
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     * 跳转到角色管理界面
     * @return
     */
    @RequestMapping("role")
    public String rolePage(){
        return "/role_form";
    }

    @RequestMapping("/role/list")
    @ResponseBody
    public JsonResult getRoleList(){
        return roleService.getRoleList();
    }

    @RequestMapping("/role/save")
    @ResponseBody
    public JsonResult addRole(RoleDO roleDO){
        return roleService.addRole(roleDO);
    }

    @RequestMapping("/role/list2")
    @ResponseBody
    public JsonResult getRoleDTOList(){
        return roleService.getRoleDTOList();
    }

    @RequestMapping("/role/delete")
    @ResponseBody
    public JsonResult deleteRoleById(Integer id){
        if(id==null){
            return new JsonResult(201,"id为空");
        }
        return roleService.deleteRoleById(id);
    }

    @RequestMapping("/role/update")
    @ResponseBody
    public JsonResult updateRoleById(RoleDO roleDO){
        if(roleDO==null||roleDO.getId()==null||roleDO.getName()==null||roleDO.getRoleTypeId()==null){
            return new JsonResult(201,"非法参数");
        }
        return roleService.updateRoleById(roleDO);
    }

    /**
     * 更新角色权限
     * @param roleId 角色id
     * @param ids 权限id集合
     * @return
     */
    @RequestMapping("/role/setRolePermission")
    @ResponseBody
    public JsonResult setRolePermission(Integer roleId, String ids){
        List<String> list= Arrays.asList(ids.split(","));
        return roleService.setRolePermission(roleId,list);
    }
}
