package com.itheima.controller;

import com.itheima.Service.LogService;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping
    public Result log(Integer page, Integer pageSize){
        log.info("记录日志");
         PageResult<OperateLog> lkog =logService.log(page, pageSize);
        return Result.success(lkog);
    }
}
