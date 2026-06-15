package com.itheima.Service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {
    /**
     * 查询所有部门
     *
     * @return
     */
    List<Dept> findAll();

    /**
     * 删除部门
     */
    void deleteById(Integer id);

    /**
     * 新增部门
     */
    void save(Dept dept);

    /**
     * 修改部门
     * 1.查询部门
     * 2.修改部门
     */
    Dept findById(Integer id);

    void update(Dept dept);
}
