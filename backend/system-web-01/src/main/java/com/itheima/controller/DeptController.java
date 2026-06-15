package com.itheima.controller;

import com.itheima.Aop.Log;
import com.itheima.Service.impl.DeptServiceImpl;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {
    //private static final Logger log = LoggerFactory.getLogger(DeptController.class);
    @Autowired
    private DeptServiceImpl deptService;
//    @RequestMapping(value = "/depts", method = RequestMethod.GET)
    @GetMapping
    public Result findAll(){
        log.info("查询所有部门");
        List<Dept> list = deptService.findAll();
        return Result.success(list);
    }
    /**
     * 删除部门
     */
//    @DeleteMapping("/depts")
//    public Result delete(HttpServletRequest  request){
//        System.out.println("删除部门");
//        String id = request.getParameter("id");
//        Integer id1 = Integer.parseInt(id);
//        deptService.deleteById( id1);
//        return Result.success();
//    }

    /**
     *  删除部门
     * @param id 部门id
     * @return 删除结果
     */
    @Log
    @DeleteMapping
    public Result delete(Integer id){
        log.info("删除部门: {}",id);
        deptService.deleteById( id);
        return Result.success();
    }
    /**
     * 添加部门
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Dept dept){
        log.info("添加部门: {}",dept);
        System.out.println(dept);
        deptService.save(dept);
        return Result.success();
    }
    /**
     * 修改部门
     * 1. get请求：请求参数：id，通过id查询部门
     * 2. post请求：请求参数：Dept对象，修改部门
     * 3. 修改部门时，需要提交部门id，因此需要将部门id封装到Dept对象中
     * pathVariable：路径参数. 路径参数必须和@PathVariable指定参数名一致
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        log.info("通过id查询部门: {}",id);
        Dept dept = deptService.findById(id);
        return Result.success(dept);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("修改部门: {}",dept);
        System.out.println(dept);
        deptService.update(dept);
        return Result.success();
    }
}
