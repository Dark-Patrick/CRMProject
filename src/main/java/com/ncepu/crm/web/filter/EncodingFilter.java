package com.ncepu.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("filter:encoding...");
        //过滤post请求中文参数乱码
        servletRequest.setCharacterEncoding("UTF-8");
        //过滤相应流相应中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
