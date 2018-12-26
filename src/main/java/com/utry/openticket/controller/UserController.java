package com.utry.openticket.controller;

import com.alibaba.fastjson.JSONObject;
import com.utry.openticket.model.UserDO;
import com.utry.openticket.model.vo.JsonResult;
import com.utry.openticket.model.vo.UserVO;
import com.utry.openticket.service.RoleService;
import com.utry.openticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 */

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    /**
     * 跳转到用户列表界面
     * @return
     */
    @RequestMapping("user")
    public String getUserPage(){
        return "/user_form";
    }

    @RequestMapping("/user/list")
    public @ResponseBody List<UserVO> getUserList(){
        return userService.getUserList();
    }

    @RequestMapping(value="/user/save",produces = "application/json;charset=utf-8")
    @ResponseBody
    public JsonResult saveUser(UserDO user){
        if(user.getUsername()==null||"".equals(user.getUsername())||user.getPassword()==null||"".equals(user.getPassword())){
            return new JsonResult(201,"用户名或密码不能为空");
        }
        return userService.saveUser(user);
    }

    @RequestMapping("/user/{id}")
    @ResponseBody
    public JsonResult getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @RequestMapping("/user/update")
    @ResponseBody
    public JsonResult updateUser(UserDO user){
        if(user.getUsername()==null||"".equals(user.getUsername())||user.getPassword()==null||"".equals(user.getPassword())){
            return new JsonResult(201,"用户名或密码不能为空");
        }
        return userService.updateUser(user);
    }

    @RequestMapping("/user/delete")
    @ResponseBody
    public JsonResult deleteUser(Integer id){
        if(id==null){
            return new JsonResult(201,"请求参数id为null");
        }
        return userService.deleteUser(id);
    }

    /**
     * 根据用户id获取用户角色
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/user/getRole")
    @ResponseBody
    public JsonResult getUserRole(@RequestParam("id") Integer userId){
        if(userId==null){
            return new JsonResult(201,"用户id为空");
        }
        return roleService.getRoleByUserId(userId);
    }

    @RequestMapping("/user/setRole")
    @ResponseBody
    public JsonResult setRoles(Integer userId,String roles){
        List<String> list= Arrays.asList(roles.split(","));
        return userService.setUserRole(userId,list);
    }
}
