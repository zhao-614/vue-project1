package com.itheima.mapper;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;
import com.itheima.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

   List<Map<String, Object>> getStudentDegreeData();

   List<Student> getStudents(StudentQueryParam studentQueryParam);

   @Delete("delete from student where id=#{id}")
   void delete(Integer id);

   void save(Student student);

   @Select(" select * from student where id=#{id}")
   Student findById(Integer id);

   @Select("select * from student where no=#{no}")
   Student findByNo(String no);

   void update(Student student);
   
   @Update("UPDATE student SET violation_score = violation_score + #{score}, violation_count = violation_count + 1, update_time = NOW() WHERE id = #{id}")
   void updateViolation(Integer id, Integer score);
}
