package com.utry.openticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utry.openticket.model.PermissionDO;
import com.utry.openticket.model.vo.JsonResult;
import com.utry.openticket.service.PermissionService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PermissionController {

	@Autowired
	PermissionService permissionService;
	
    @RequestMapping("/403")
    public String errorPage(){
        return "/403";
    }
    
    /**
     * 去权限管理界面
     * id 是默认值2 最高权限
     * @return
     */
    @RequestMapping("/permission")
    public String goPermissionPage(@RequestParam(defaultValue="2") String id,Model model){
    	PermissionDO permissionDO=permissionService.getPermissionById(Integer.parseInt(id));
    	//获取所有的二级目录
    	List<PermissionDO> permissionList=permissionService.getPermissionByFuncOrder(2);
    	model.addAttribute("permissionList", permissionList);
    	model.addAttribute("type", id);
    	model.addAttribute("name", permissionDO.getName());
    	model.addAttribute("pid", permissionDO.getPid());
    	return "/permission_form";
    }
    
    /**
     * 得到所有的权限
     * @return
     */
    @RequestMapping("/permission/list")
    @ResponseBody
    public List<PermissionDO> getPermissionList(){
    	return permissionService.getPermissionList();
    }
    
    /**
     * 保存或者修改 一个权限
     */
    @RequestMapping("savePermission")
    @ResponseBody
    public JsonResult savePermission(@RequestBody PermissionDO permissionDO,HttpServletRequest request){
    	if(permissionDO.getId()!=null){
    		//不为空是修改
    		permissionService.updatePermission(permissionDO);
    	}else {
			//否则是保存
    		permissionService.savePermission(permissionDO);
		}
        removePermissionList(request);
    	return new JsonResult(1,"保存成功");
    }
    
    /**
     * 通过父权限的id获取所有子权限
     * @param type
     * @return
     */
    @RequestMapping("/getChildPermission")
    @ResponseBody
    public JsonResult getChildPermission(@RequestParam(defaultValue="2") String type,Model model){
    	List<PermissionDO> permissionDOs = permissionService.getChildPermission(type);
    	return new JsonResult(1,permissionDOs);
    }
    /**
     * 用户通过id获取Permission
     * @param id
     * @return
     */
    @RequestMapping("/getPermissionById")
    @ResponseBody
    public JsonResult getPermissionById(@RequestBody String id){
    	PermissionDO permission = permissionService.getPermissionById(Integer.parseInt(id));
    	return new JsonResult(1, permission);
    }
    
    /**
     * 用户通过id删除Permission
     * @param id
     * @return
     */
    @RequestMapping("/delPermissionById")
    @ResponseBody
    public JsonResult delPermissionById(@RequestBody String id,HttpServletRequest request){
    	permissionService.delPermissionById(Integer.parseInt(id));
        removePermissionList(request);
    	return new JsonResult(1, "删除成功");
    }

    @RequestMapping("/permission/getByRoleId")
    @ResponseBody
    public JsonResult getByRoleId(Integer roleId){
        return permissionService.getByRoleId(roleId);
    }

    /**
     * 得到权限树
     * @return
     */
    @RequestMapping("/permission/getTree")
    @ResponseBody
    public JsonResult getPermissionTree(){
        return permissionService.getPermissionTree();
    }

    /**
     * 移除ServletContext保存的权限list
     * @param request
     */
    private void removePermissionList(HttpServletRequest request){
        request.getServletContext().removeAttribute("permissionList");
    }

}
