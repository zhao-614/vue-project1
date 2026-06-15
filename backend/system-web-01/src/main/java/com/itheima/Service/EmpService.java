package com.itheima.Service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.LoginLnfo;
import com.itheima.pojo.PageResult;

import java.util.List;

public interface EmpService {
    /**
     * 分页查询员工数据
     */
//    PageResult<Emp> page(Integer page, Integer pageSize, LocalDate startDate, LocalDate endDate, String name, Integer gender);
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    /**
     * 新增员工
     * @param emp 新增数据
     */
    void save(Emp emp);

    /**
     * 删除员工
     * @param ids 删除的ID数组
     */
    void delete(List<Integer> ids);

    /**
     *  修改员工
      *
     * @param id 修改的ID
     * @return 修改的员工数据
     */
    Emp findById(Integer id);

    /**
     *  修改员工
     * @param emp
     */
    void update(Emp emp);
/**
 * 登录
 * @param emp 登录数据
 * @return 登录结果
 */
    LoginLnfo login(Emp emp);
}
