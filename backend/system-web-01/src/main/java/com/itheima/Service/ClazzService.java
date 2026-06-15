package com.itheima.Service;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClazzService {
    /**
     * 分页查询所有班级信息
     * @return list集合
     */
    PageResult<Clazz> getClazzs(ClazzQueryParam clazzQueryParam);

    /**
     * 删除班级信息
     * @param id 删除的ID
     */
    void delete(Integer id);

    /**
     * 新增班级信息
     * @param clazz 新增的班级信息
     */
    void save(Clazz clazz);

    /**
     * 返回班主任数据
     *
     * @return 班主任数据
     */
    List<Emp> getEmpJobData();
    /**
     * 通过ID查询班级信息
     */
    Clazz findById(Integer id);

    /**
     * 修改班级信息
     * @param clazz 修改的班级信息
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级信息
     * @return list集合
     */
    List<Clazz> getClazzList();
}
