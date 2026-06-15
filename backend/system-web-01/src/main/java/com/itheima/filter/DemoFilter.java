package com.itheima.filter;

import com.itheima.config.UserContext;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.IOException;
@Slf4j
@WebFilter("/*")//拦截所有请求
public class DemoFilter implements Filter {
    //dofilter表示过滤器处理逻辑
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //处理跨域
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //请求路径
        String requestURI = request.getRequestURI().toString();
        log.info("拦截请求：{}",requestURI);
        //放行静态资源
        if( requestURI.contains("/login") || 
            requestURI.startsWith("/images/") ||
            requestURI.endsWith(".png") ||
            requestURI.endsWith(".jpg") ||
            requestURI.endsWith(".jpeg") ||
            requestURI.endsWith(".gif") ||
            requestURI.endsWith(".css") ||
            requestURI.endsWith(".js") ||
            requestURI.endsWith(".ico") ||
            requestURI.endsWith(".svg")){
            filterChain.doFilter(request,response);
            return;
        }
        //获取令牌
        String token = request.getHeader("token");
        if(!StringUtils.hasLength( token)){
            log.info("获取到token令牌为空, 返回错误结果");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return;
        }
        //解析令牌
        try {
            Claims claims = JwtUtils.parseJWT(token);
            Integer empId = (Integer) claims.get("id");
            UserContext.setCurrentUserId(empId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析令牌失败, 返回错误结果");
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            return;
        }
        //放行
        try {
            filterChain.doFilter(request,response);
        } finally {
            UserContext.remove();
        }
    }
}
