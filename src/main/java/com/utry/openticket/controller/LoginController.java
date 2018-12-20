package com.utry.openticket.controller;

import com.utry.openticket.dto.UserDTO;
import com.utry.openticket.model.PermissionDO;
import com.utry.openticket.model.UserDO;
import com.utry.openticket.service.PermissionService;
import com.utry.openticket.service.TicketTypeService;
import com.utry.openticket.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/login")
    public String  login(String username, String password,
                         Model model, HttpSession session, HttpServletRequest request) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            model.addAttribute("empty", "用户名或密码为空");
            return "/login";
        } else {
            UserDTO user = new UserDTO();
            user.setUsername(username);
            user.setPassword(password);
            UserDO userDO = userService.getUser(user);
            if(userDO == null || StringUtils.isEmpty(userDO.getUsername())) {
                model.addAttribute("empty", "用户名不存在");
                return "/login";
            } else {
                return mainTable(session,userDO);
            }
        }
    }

    private String mainTable(HttpSession session,UserDO user) {
        session.setAttribute("login", user);
        return "redirect:/index";
    }
}
