package com.utry.openticket.service;

import com.utry.openticket.dao.RoleDAO;
import com.utry.openticket.dao.UserDAO;
import com.utry.openticket.dto.UserDTO;
import com.utry.openticket.model.UserDO;
import com.utry.openticket.model.vo.JsonResult;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private RoleDAO roleDAO;

    public UserDO  getUser(UserDTO user) {
        UserDO userDO = new UserDO();
        userDO.setUsername(user.getUsername());
        userDO.setPassword(user.getPassword());
        userDO = userDAO.getUser(userDO);
        return userDO;
    }


    /**
     * 获取所有user，不分页
  * @return
     */
    public List<UserDO> getUserList(){
        return userDAO.getUserList();
    }

    public JsonResult getUserById(Integer id){
        UserDO user = userDAO.getUserById(id);
        return JsonResult.success(user);
    }

    /**
     * 添加保存新用户
     * @param user
     * @return
     */
    public JsonResult saveUser(UserDO user){
        //查询用户名是否存在
        List<UserDO> list = getUserByUsername(user.getUsername());
        if(!list.isEmpty()){//用户名已存在
            return new JsonResult(201,"该用户名已存在");
        }
        user.setCreateTime(getCurrentTime());
        user.setUpdateTime(user.getCreateTime());
        userDAO.saveUser(user);
        return JsonResult.success();
    }

    public List<UserDO> getUserByUsername(String username){
        return userDAO.getUserByUsername(username);
    }

    public JsonResult deleteUser(Integer id) {
        userDAO.deleteUser(id);
        //同时删除用户的角色
        roleDAO.deleteRoleByUserId(id);
        return JsonResult.success();
    }

    public JsonResult updateUser(UserDO user){
        user.setUpdateTime(getCurrentTime());
        userDAO.updateUser(user);
        return JsonResult.success();
    }

    public JsonResult setUserRole(Integer userId,List<String> Roles){
        //先删除用户拥有的所有角色
        roleDAO.deleteRoleByUserId(userId);
        //重新设置用户角色
        for(String roleId : Roles){
            roleDAO.addUserRole(userId,Integer.valueOf(roleId));
        }
        return JsonResult.success();
    }

    private String getCurrentTime(){
        Date today=new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(today);
    }

}
