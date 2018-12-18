package com.utry.openticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PermissionController {

    @RequestMapping("/403")
    public String errorPage(){
        return "/403";
    }
}
