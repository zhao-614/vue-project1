package com.itheima.controller;

import com.itheima.Aop.Log;
import com.itheima.Service.StudentService;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 根据页面查询所有学生信息
     * @return result
     */
    @GetMapping
    public Result getStudents(StudentQueryParam studentQueryParam){
        log.info("查询所有学生信息");
        return Result.success(studentService.getStudents( studentQueryParam));
    }
    /**
     * 根据id删除学生信息
     * @param id 前端传递的id
     * @return 删除结果
     */
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        log.info("删除学生信息");
        studentService.delete(id);
        return Result.success();
    }
    /**
     * 新增学生信息
     * @param student 新增数据
     * @return 新增结果
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Student student){
        log.info("保存学生信息: {}",student);
        studentService.save(student);
        return Result.success();
    }
    /**
     *修改学生信息
     * 1. get请求：请求参数：id，通过id查询部门
     * 2. put请求：请求参数：Dept对象，修改部门
     * @param id 修改数据
     *
     *
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        log.info("通过id查询学生: {}",id);
        Student student = studentService.findById(id);
        return Result.success(student);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Student student){
        log.info("修改学生信息");
        studentService.update(student);
        return Result.success();
    }
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result updateViolation(@PathVariable Integer id,@PathVariable Integer score){
        log.info("修改学生违规信息");
        studentService.updateViolation(id,score);
        return Result.success();
    }
}
