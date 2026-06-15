package com.itheima.mapper;

import com.itheima.Service.impl.DeptServiceImpl;
import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询所有部门
     *
     * @Result 将因为数据库和java实体类名称不一致，导致数据无法映射，所以使用Result
     * @return
     */
//    @Results({
//            @Result(column = "create_time", property = "createTime"),
//            @Result(column = "update_time", property = "updateTime")
//    })
    @Select("SELECT id, name, create_time, update_time FROM dept ORDER BY update_time desc ;")
    List<Dept> findAll();

    @Delete("DELETE FROM dept WHERE id = #{id} ;")
    void deleteById(Integer id);
    /**
     *  添加部门
     * @param dept
     */
    @Insert("INSERT INTO dept(name, create_time, update_time) VALUES (#{name}, #{createTime}, #{updateTime});")
    void save(Dept dept);
    /**
     *  修改部门
     *  1.根据id查询部门
     *  2.修改部门
     * @param id
     */
    @Select("SELECT id, name, create_time, update_time FROM dept WHERE id = #{id} ;")
    Dept findById(Integer id);
    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} WHERE id = #{id} ;")
    void update(Dept dept);
}
