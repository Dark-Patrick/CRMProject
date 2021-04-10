package com.ncepu.crm.settings.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncepu.crm.settings.domain.User;
import com.ncepu.crm.settings.service.UserService;
import com.ncepu.crm.settings.service.impl.UserServiceImpl;
import com.ncepu.crm.utils.MD5Util;
import com.ncepu.crm.utils.PrintJson;
import com.ncepu.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("进入到用户控制器");

        String path = req.getServletPath();

        if("/settings/user/login.do".equals(path)){
            login(req, resp);
        }else if("/settings/user/session.do".equals(path)){
            getUserSession(req, resp);
        }

    }
    private void login(HttpServletRequest req, HttpServletResponse resp){
        System.out.println("servlet：login...");
        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");

        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = req.getRemoteAddr();
        System.out.println("------IP------:" + ip);
        System.out.println(loginPwd);

        //业务层开发，统一使用代理类形态的接口对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            //login失败抛出异常
            User user = us.login(loginAct, loginPwd, ip);
            //login失败停止继续执行
            req.getSession().setAttribute("user", user);
            PrintJson.printJsonFlag(resp, true);
        }catch (Exception e){
            String msg = e.getMessage();
            /**
             * json传值的两种手段
             *      1.将多项信息打包成map，将map解析为json串
             *      2.创建一个Vo
             * 如果对于展现的信息将来还会大量使用，建立一个Vo类，使用方便
             * 如果对于展现的信息只有在这个需求中能够使用，使用map就足够
             */
            Map<String, Object> map = new HashMap<>();
            map.put("success", false);
            map.put("msg",msg);
            PrintJson.printJsonObj(resp, map);
        }
    }

    private void getUserSession(HttpServletRequest req, HttpServletResponse resp){
        User user = (User)req.getSession().getAttribute("user");
        System.out.println("servlet:getSession...");
        PrintJson.printJsonObj(resp,user);
    }
}
