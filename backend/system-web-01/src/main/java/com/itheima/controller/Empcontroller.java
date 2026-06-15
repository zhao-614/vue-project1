package com.itheima.controller;

import com.itheima.Aop.Log;
import com.itheima.Service.impl.EmpServiceImpl;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

//restcontroller 返回json数据
@RestController
//requestmapping 请求映射
@RequestMapping("/emps")
//slf4j是日志的抽象层
@Slf4j

public class Empcontroller {
    //autowired 注解，自动注入
    @Autowired
    private EmpServiceImpl empService;
    /**
     * 分页查询
     * @param empQueryParam 查询条件
     * @return 查询结果
     */
//    @GetMapping
//    //requestParam 的defaultVaule为默认值
//    public Result page(@RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam (defaultValue = "7") Integer pageSize,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end,
//                        String name,
//                       Integer gender
//                       ) {
//        log.info("分页查询,{},{},{},{},{},{}",page,pageSize,begin,end,name, gender);
//        PageResult<Emp> pageResult =empService.page( page, pageSize, begin, end, name, gender);
//        return Result.success( pageResult);
//    }
    @GetMapping
    //requestParam 的defaultVaule为默认值
    public Result page(EmpQueryParam empQueryParam) {
        log.info("分页查询,{}",empQueryParam);
        PageResult<Emp> pageResult =empService.page( empQueryParam);
        return Result.success( pageResult);
    }
    /**
     * 新增教师
     * @param emp
     * @return 新增结果
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增教师,{}",emp);
        empService.save(emp);
        return Result.success();
    }

//    @DeleteMapping()
//    public Result delete(Integer[] ids) {
//        log.info("删除教师,{}", (Object[]) ids);
//        empService.delete(ids);
//        return Result.success();
//    }
    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除教师,{}", ids);
        empService.delete(ids);
        return Result.success();
    }
    /**
     * 修改教师
     * 1. get请求：请求参数：id，通过id查询部门
     * 2. post请求：请求参数：Dept对象，修改部门
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id) {
        log.info("通过id查询部门: {}",id);
        Emp emp = empService.findById(id);
        return Result.success(emp);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改部门: {}",emp);
        empService.update(emp);
        return Result.success();
    }
}
