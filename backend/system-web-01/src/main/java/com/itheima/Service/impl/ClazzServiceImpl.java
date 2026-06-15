package com.itheima.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.Service.ClazzService;
import com.itheima.mapper.ClazzMapper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.pojo.Clazz;
import com.itheima.pojo.ClazzQueryParam;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

    @Override
    public PageResult<Clazz> getClazzs(ClazzQueryParam clazzQueryParam) {
        //使用pageHelper进行分页
        PageHelper.startPage(clazzQueryParam.getPage(),clazzQueryParam.getPageSize());
        //传递查询参数
        Page<Clazz> page = (Page<Clazz>) clazzMapper.getClazzs(clazzQueryParam);
        //返回分页结果
        return new PageResult<>(page.getTotal(),page.getResult());
    }

    @Override
    public void delete(Integer id) {
        clazzMapper.delete(id);
    }

    @Override
    public void save(Clazz clazz) {
        //设置时间
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        //保存
        clazzMapper.save(clazz);
    }

    @Override
    public List<Emp> getEmpJobData() {
        return empExprMapper.getEmpJobData();
    }

    @Override
    public Clazz findById(Integer id) {
        return clazzMapper.findById(id);
    }

    @Override
    public void update(Clazz clazz) {
        //设置时间
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    @Override
    public List<Clazz> getClazzList() {
        return clazzMapper.getClazzList();
    }
}
