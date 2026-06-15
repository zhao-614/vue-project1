package com.itheima.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.Service.StudentService;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public PageResult<Student> getStudents(StudentQueryParam studentQueryParam) {
        //获取分页参数，然后使用pageHelper进行分页
        PageHelper.startPage(studentQueryParam.getPage(),studentQueryParam.getPageSize());
        //转换参数
        Page<Student> page = (Page<Student>) studentMapper.getStudents(studentQueryParam);
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        log.info("删除学生信息: {}",id);
        studentMapper.delete(id);
    }

    @Override
    public void save(Student student) {
        log.info("保存学生信息: {}",student);
        // 检查学号是否已存在
        Student existingStudent = studentMapper.findByNo(student.getNo());
        if (existingStudent != null) {
            log.warn("学号 {} 已存在，将执行更新操作", student.getNo());
            // 已存在，使用已有ID进行更新
            student.setId(existingStudent.getId());
            update(student);
            return;
        }
        // 不存在，执行新增
        student.setUpdateTime(LocalDateTime.now());
        student.setCreateTime(LocalDateTime.now());
        studentMapper.save(student);
    }

    @Override
    public Student findById(Integer id) {
        log.info("通过id查询班级: {}",id);
        return studentMapper.findById(id);
    }

    @Override
    public void update(Student student) {
        log.info("修改班级信息: {}",student);
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);

    }

    @Override
    public void updateViolation(Integer id, Integer score) {
        log.info("学生ID: {} 扣分: {}", id, score);
        
        // 1. 校验扣分值是否合法
        if (score == null || score <= 0) {
            throw new RuntimeException("扣分值必须大于0");
        }
        
        // 2. 查询学生是否存在
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new RuntimeException("学生不存在，ID: " + id);
        }
        
        // 3. 获取当前违规扣分，如果为null则初始化为0
        Integer currentScore = student.getViolationScore() != null ? student.getViolationScore() : 0;
        Integer currentCount = student.getViolationCount() != null ? student.getViolationCount() : 0;
        
        // 4. 计算扣分后的总分
        Integer newScore = currentScore + score;
        
        // 5. 校验扣分后是否超过上限（假设满分100分）
        if (newScore > 100) {
            throw new RuntimeException("扣分后总分" + newScore + "超过上限100分，当前分数: " + currentScore);
        }
        
        // 6. 执行扣分操作
        studentMapper.updateViolation(id, score);
        log.info("扣分成功 - 学生: {}, 原分数: {}, 本次扣分: {}, 新分数: {}", 
                student.getName(), currentScore, score, newScore);
    }
}
