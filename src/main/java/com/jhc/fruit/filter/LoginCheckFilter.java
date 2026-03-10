package com.jhc.fruit.filter;

import com.alibaba.fastjson.JSON;
import com.jhc.fruit.common.BaseContext;
import com.jhc.fruit.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @author ：lcy
 * @description：检查用户是否已经登录
 * @date ：2022/4/28 0028 16:10
 */
@Slf4j
@WebFilter(filterName="loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求URI
        String requestURI = request.getRequestURI();
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                //放行登录页面请求
                "/static/**",
                "/index.html",
                //放行用户登录登出方法
                "/employee/getOpenId",
                "/employee/login",
                "/employee/logout",
                //放行扫描二维码
                "/fruitTree/showTree",
                //放行游客浏览新闻方法
                "/news/**",
                "/api/employee/getOpenId",
                "/api/employee/login",    
                "/api/employee/logout",   
                "/api/news/**",
                //放行Swagger
                "/swagger-ui.html/**",
                "/webjars/**",
                "/v2/**",
                "/swagger-resources/**",
                "/null/swagger-resources/**"
        };

        //2.判断本次请求是否需要处理:true 不处理；false 处理
        boolean flag = check(requestURI,urls);
        flag = true;
        //3.如果不要处理，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            System.out.println("用户登录的request:" + request);
            log.info("用户已登录,用户id为：" + request.getSession().getAttribute("employee"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            return;
        }
        if(flag){
//            log.info("本次请求不需要处理" + requestURI);
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        //4.判断登录状态,如果已登录，则放行
        if(request.getSession().getAttribute("employee") != null){
            System.out.println("用户登录的request:" + request);
            log.info("用户已登录,用户id为：" + request.getSession().getAttribute("employee"));
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录拦截到请求" + request.getRequestURI());
        //5.如果未登录，则为前端页面响应数据
        // response.getWriter().write(JSON.toJSONString(R.error("用户未登录")));
        
        // 修改后的代码：
        R<String> result = R.error("用户未登录");
        String jsonString = JSON.toJSONString(result);
        // 重定向到首页
        response.sendRedirect("/index.html");

        // 设置响应头，指定编码
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.write(jsonString);
        out.flush();
        return;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String requestURI,String[] urls){
        for(String url: urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
