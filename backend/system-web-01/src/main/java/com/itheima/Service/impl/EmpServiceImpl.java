package com.itheima.Service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.Service.EmpService;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {
    /**
     * empMapper 是持久层对象
     */
    @Autowired
    private EmpMapper empMapper;
    /**
     * empExprMapper 是持久层对象
     */
    @Autowired
    private EmpExprMapper empExprMapper;
    @Override
    public PageResult page(EmpQueryParam empQueryParam) {
        //1.获取分页参数，然后使用pageHelper进行分页
        PageHelper.startPage(empQueryParam.getPage(),empQueryParam.getPageSize());
        //2.获取查询参数
        Page<Emp> page = (Page<Emp>) empMapper.findByPage(empQueryParam);
        //3.返回分页结果
        return new PageResult(page.getTotal(),page.getResult());
    }
    //propagation是设置事务的隔离级别，REQUIRED表示当前方法需要事务，如果当前方法没有事务，则创建一个事务，如果当前方法有事务，则加入当前事务，
    // 而REQUIRED_NEW表示当前方法需要事务，如果当前方法没有事务，则创建一个事务，如果当前方法有事务，则创建一个新的事务，而REQUIRED_NEW表示当前方法
    //这个可以用来隔离读写问题，如果某个方法REQUIRED出现异常，则事务会回滚，而REQUIRED_NEW表示不会到影响当前事务，则事务不会回滚
    //rollbackFor表示事务回滚的异常

    /**
     *  增加 员工
     * @param emp 新增数据
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.save(emp);
        List<EmpExpr> empExprs = emp.getExprList();
        if(empExprs != null && !empExprs.isEmpty())
        {
            empExprs.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(empExprs);
        }
    }
    /**
     * 删除员工
     * @param ids 删除的ID数组
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        empMapper.delete(ids);
        empExprMapper.delete(ids);
    }
/**
 *  修改员工
 * @param id 修改的ID
 * @return 修改的员工数据
 */
    @Override
    public Emp findById(Integer id) {
        return empMapper.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        empExprMapper.delete(Arrays.asList(emp.getId()));
        List<EmpExpr> empExprs = emp.getExprList();
        if(empExprs != null && !empExprs.isEmpty())
        {
            empExprs.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(empExprs);
        }
    }


    @Override
    public LoginLnfo login(Emp emp) {
        Emp empDB = empMapper.findByUsernameAndPassword(emp.getUsername(),emp.getPassword());
        if (empDB != null ){
            if ( empDB.getJob() != 5) {
                return new LoginLnfo(-1, null, null, null);
            }
            //生成jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", empDB.getId());
            claims.put("username", empDB.getUsername());
            String token = JwtUtils.generateJwt(claims);
            return new LoginLnfo(empDB.getId(),empDB.getName(),empDB.getUsername(),  token);
        }
        return null;
    }

}
