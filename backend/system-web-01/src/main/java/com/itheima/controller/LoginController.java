package com.itheima.controller;

import com.itheima.Service.EmpService;
import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginLnfo;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private EmpService empService;
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp){
        log.info("用户登录");
        LoginLnfo loginLnfo = empService.login(emp);
        if (loginLnfo == null){
            return Result.error("用户名或密码错误");
        }
        if (loginLnfo.getId() == -1) {
            return Result.error("不是管理员");
        }
        return Result.success(loginLnfo);
    }
}
