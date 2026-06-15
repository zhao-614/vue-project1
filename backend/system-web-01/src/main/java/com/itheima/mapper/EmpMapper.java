package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
//

@Mapper
public interface EmpMapper {

/**
 * 分页查询员工数据
 */
//@Select("select emp.*, dept.name as deptNname from emp left join dept on emp.dept_id = dept.id order by emp.update_time desc")
//    public List<Emp> findByPage(LocalDate begin, LocalDate end,String name,Integer gender);
    public List<Emp> findByPage(EmpQueryParam  param);

    /**
     * options 添加数据成果后主键是自增的，所以添加数据成功后不知道id，因此使用 options返回主键到当前对象中
     * @param emp 新增数据
     */
    @Options(useGeneratedKeys = true,keyColumn = "id")// useGeneratedKeys表示使用数据库生成的主键，keyColumn表示主键的字段
    void save(Emp emp);

    void delete(List<Integer> ids);

    Emp findById(Integer id);

    /**
     * 修改员工
     * @param emp
     */
    void update(Emp emp);
    /**
     * 查询工作总数
     */
    List<Map<String,Object>>  getEmpJobData();

    /**
     * 查询员工性别数据
     */
    List<Map<String,Object>> countEmpGenderData();
    /**
     * 登录
     */
    @Select("select id,name,username,job from emp where username=#{username} and password=#{password}")
    Emp findByUsernameAndPassword(String username, String password);
}
