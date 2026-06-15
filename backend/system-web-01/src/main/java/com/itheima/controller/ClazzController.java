package com.itheima.controller;

import com.itheima.Aop.Log;
import com.itheima.Service.ClazzService;
import com.itheima.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/clazzs")
public class ClazzController {
@Autowired
    private ClazzService clazzService;


    /**
     * 分页查询所有班级信息
     * @param clazz 查询参数
     * @return list集合
     */
    @GetMapping
    public Result getClazzs(ClazzQueryParam clazz){
    log.info("查询所有班级信息");
        PageResult<Clazz> pageResult = clazzService.getClazzs(clazz);
    return Result.success(pageResult) ;
}

    /**
     * 根据id删除班级信息
     * @param id 前端传递的id
     * @return 删除结果
     */

    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
    log.info("删除班级信息");
    clazzService.delete(id);
    return Result.success();
}

    /**
     * 1. get请求：请求参数：id，通过id查询部门
     * 2. put请求：请求参数：Dept对象，修改部门
     * @param
     * @return 新增结果
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        log.info("通过id查询班级: {}",id);
        Clazz clazz = clazzService.findById(id);
        return Result.success(clazz);
    }
    
    @Log
    @PostMapping
    public Result save(@RequestBody Clazz clazz){
        log.info("保存班级信息");
        clazzService.save(clazz);
        return Result.success();
    }
    
    @Log
    @PutMapping
    public Result update(@RequestBody Clazz clazz){
        log.info("修改班级信息");
        clazzService.update(clazz);
        return Result.success();
    }
/**
 * 请求所有班主任
 * @return list集合
 */
    @GetMapping("/masters")
    public Result getMasters(){
    log.info("查询所有班主任");
    List<Emp> list = clazzService.getEmpJobData();
    return Result.success(list);
    }
    /**
     * 统计班级状态
     * @return list集合
     */
    @GetMapping("/list")
    public Result getClazzStatus(){
    log.info("统计班级状态");
    List<Clazz> list = clazzService.getClazzList();
    return Result.success(list);
    }

}
