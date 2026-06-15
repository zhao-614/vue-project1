package com.itheima.Service;

import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;

public interface LogService {
    //查询日志
    PageResult<OperateLog> log(Integer page, Integer pageSize);
}
