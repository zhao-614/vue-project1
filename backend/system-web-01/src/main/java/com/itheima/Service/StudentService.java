package com.itheima.Service;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;

public interface StudentService {
    /**
     * 查询所有学生信息
     * @return
     */
    PageResult<Student> getStudents(StudentQueryParam studentQueryParam);

    /**
     * 删除学生信息
     * @param id 删除的ID
     */
    void delete(Integer id);

    void save(Student student);

    Student findById(Integer id);

    void update(Student student);

    void updateViolation(Integer id, Integer score);

}
