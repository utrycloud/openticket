package com.utry.openticket.controller;



import com.utry.openticket.dto.UserDTO;
import com.utry.openticket.model.UserDO;
import com.utry.openticket.model.vo.JsonResult;
import com.utry.openticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProFileController {

    @Autowired
    UserService userService;

    @RequestMapping("profile")
    public String proFile() {
        return "/profile";
    }

    /**
     * 修改个人信息，如果密码不修改那么插入新密码就是原来的密码
     * @param userDTO
     * @param request
     * @return
     */
    @PostMapping("/proFile/update")
    @ResponseBody
    public JsonResult updateProFle(UserDTO userDTO, HttpServletRequest request) {
        System.out.println(userDTO);
        HttpSession session = request.getSession();
        UserDO user = (UserDO)session.getAttribute("login");
        user.setUsername(userDTO.getUsername());
        user.setRealName(userDTO.getRealName());
        user.setUpdateTime(userDTO.getUpdateTime());
        user.setEmail(userDTO.getEmail());
        user.setTel(userDTO.getTel());
        if (userDTO.getPassword() == null || userDTO.getPassword() == "") {
            userDTO.setNewPassword(user.getPassword());
        } else {
            user.setPassword(userDTO.getNewPassword());
        }
        session.setAttribute("login", user);
        System.out.println("password = "+user.getPassword());
        userService.updateUserProfile(userDTO);
        return JsonResult.success();
    }

}
