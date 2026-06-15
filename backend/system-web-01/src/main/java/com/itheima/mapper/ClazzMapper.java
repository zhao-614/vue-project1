package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClazzMapper {

    /**
     * 分页查询所有班级信息
     * @return list集合
     */
    List<Clazz> getClazzs(ClazzQueryParam clazzQueryParam);

    /**
     * 删除班级信息
     * @param id
     */
    @Delete("delete from clazz where id = #{id}")
    void delete(@Param("id") Integer id);

    /**
     * 新增班级信息
     * @param clazz 新增数据
     */
    void save(Clazz clazz);
/**
 * 根据id查询班级信息
 * @param id
 * @return
 */

    Clazz findById(Integer id);

    /**
     *  修改班级信息
     * @param clazz 修改数据
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级
     * @return list集合
     */
    @Select("select * from clazz")
    List<Clazz> getClazzList();
}
