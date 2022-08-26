package com.reggie.Filter;

import com.reggie.Common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //Ant风格路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long threadId = Thread.currentThread().getId();
        log.info("线程ID：{}",threadId);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取请求路径
        String requestUrl = request.getRequestURI();
        String[] noCheck = {"/employee/login","/employee/logout","/backend/**","/front/**","/common/**","/user/sendMsg","/user/login"};
        //如果校验通过，则放行
        if (check(requestUrl,noCheck)){
            log.info("无需登录校验的请求:{}",requestUrl);
            filterChain.doFilter(request,response);
            return;
        }

        //尝试从session域中取出用户id，如果存在则已登录
        //检验是否登录，如果已经登录则放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("登录拦截器拦截到请求:{}，用户已登录,用户id为{}",requestUrl,request.getSession().getAttribute("employee"));

            Long employee = (Long)request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employee);//将用户id存入当前线程的共享变量中
            //如果未登录则getSession().getAttribute("employee") == null
            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){
            log.info("登录拦截器拦截到请求:{}，用户已登录,用户id为{}",requestUrl,request.getSession().getAttribute("user"));

            Long userid = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userid);//将用户id存入当前线程的共享变量中
            //如果未登录则getSession().getAttribute("employee") == null
            filterChain.doFilter(request,response);
            return;
        }
        //如果未登录，则跳转到登录页面
        log.info("登录拦截器拦截到请求:{}，用户未登录",requestUrl);
        request.getRequestDispatcher("/notlogin").forward(request,response);
        return;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean check(String path,String[] urls){
        for (String url : urls) {
            //match(需要进行校验的路径，存放需要校验的路径的数组)
            if(PATH_MATCHER.match(url,path)){
                return true;
            }
        }
        return false;
    }
}
