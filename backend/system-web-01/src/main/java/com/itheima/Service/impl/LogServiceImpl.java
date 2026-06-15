package com.itheima.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.Service.LogService;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult<OperateLog> log(Integer page, Integer pageSize) {
        //使用pageHelper完成分页
        PageHelper.startPage(page,pageSize);
        //2.获取查询参数
        Page<OperateLog> pa = (Page<OperateLog>) operateLogMapper.log();
        //3.返回分页结果
        return new PageResult( pa.getTotal(), pa.getResult());
    }
}
