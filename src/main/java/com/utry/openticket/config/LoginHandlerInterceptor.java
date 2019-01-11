package com.utry.openticket.config;

import com.alibaba.fastjson.JSONObject;
import com.utry.openticket.model.PermissionDO;
import com.utry.openticket.model.UserDO;
import com.utry.openticket.model.vo.JsonResult;
import com.utry.openticket.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDO login = (UserDO)request.getSession().getAttribute("login");
        if(login == null) {
            request.setAttribute("empty","请先登录");
            request.getRequestDispatcher("/login.html").forward(request,response);
            return false;
        } else {
            //url权限认证
            if(checkPermission(request)){//有权限则放行.
                return true;
            }else{//没权限
                //判断是否为ajax请求
                String header = request.getHeader("x-requested-with");
                if(header==null){//普通请求
                    request.getRequestDispatcher("/403").forward(request,response);
                }else{//ajax
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(JSONObject.toJSONString(new JsonResult(403,"您没有此操作权限！")));
                }
                return false;
            }
        }
    }

    /**
     * 检查权限
     * @author enoch 2018/12/16
     * @param request 请求
     * @return true：有权限；false：无权限
     */
    private boolean checkPermission(HttpServletRequest request){
        //获得请求uri
        String requestURI = request.getRequestURI();
        //获得权限list
        ServletContext servletContext = request.getServletContext();
        List<PermissionDO> permissionList =(List<PermissionDO>) servletContext.getAttribute("permissionList");
        if(permissionList==null) {
            permissionList=permissionService.getPermissionList();
            servletContext.setAttribute("permissionList", permissionList);
        }
        //判断是否公共url
        boolean isPublic=true;
        AntPathMatcher matcher=new AntPathMatcher();
        for(PermissionDO permission:permissionList){
            if(requestURI.equals(permission.getUri())){
                isPublic=false;
            }
        }

        //公用url则放行
        if(isPublic){
            return true;
        }

        //判断用户是否具有权限
        List<PermissionDO> userPermissions = (List<PermissionDO>)request.getSession().getAttribute("userPermissions");
        boolean hasPermission=false;
        if(userPermissions==null){
            UserDO user = (UserDO) request.getSession().getAttribute("login");
            userPermissions = permissionService.getUserPermissions(user.getId());
            request.getSession().setAttribute("userPermissions",userPermissions);
        }
        for(PermissionDO permission:userPermissions){
            if(matcher.match(permission.getUri(),requestURI)){
                hasPermission=true;
            }
        }
        return hasPermission;
    }
}
