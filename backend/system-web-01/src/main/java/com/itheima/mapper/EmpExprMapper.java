package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmpExprMapper {
/**
 * 批量插入员工工作经历数据
 */
    public void insertBatch(List<EmpExpr> exprList);
/**
 * 批量删除员工工作经历数据
 */
    void delete(List<Integer> ids);

    /**
     * 查询所有班主任
     *
     * @return list集合
     */
    @Select("select * from emp where job=1;")
    List<Emp> getEmpJobData();
}
