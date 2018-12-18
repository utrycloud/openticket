package com.utry.openticket.controller;

import com.utry.openticket.model.UserDO;
import com.utry.openticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 */

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 跳转到用户列表界面
     * @return
     */
    @RequestMapping("user")
    public String getUserPage(){
        return "/user_form";
    }

    @RequestMapping("/user/list")
    public @ResponseBody List<UserDO> getUserList(){
        return userService.getUserList();
    }
}
