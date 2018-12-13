package com.utry.openticket.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object login = request.getSession().getAttribute("login");
        if(login == null) {
            request.setAttribute("msg","没有权限请先登陆");
            request.getRequestDispatcher("/login.html").forward(request,response);
            return false;
        } else {
            return true;
        }
    }
}
