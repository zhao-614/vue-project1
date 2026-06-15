package com.itheima;

import jakarta.websocket.server.ServerEndpoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan//开启Servlet组件扫描
@EnableAsync//使用aop需要启用异步处理
public class SystemWeb01Application {

    public static void main(String[] args) {
        SpringApplication.run(SystemWeb01Application.class, args);
    }

}
