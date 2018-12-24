package com.utry.openticket.controller;

import com.utry.openticket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProFileController {

    @Autowired
    UserService userService;

    @RequestMapping("profile")
    public String proFile() {
        return "/profile";
    }
}
