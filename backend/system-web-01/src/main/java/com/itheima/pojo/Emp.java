package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itheima.config.FlexibleLocalDateDeserializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Emp {
    private Integer id; //ID,主键
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private Integer gender; //性别, 1:男, 2:女
    private String phone; //手机号
    private Integer job; //职位, 1:班主任,2:辅导员,3:学院主任,4:院长,5:校长
    private Integer salary; //薪资
    private String image; //头像

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate entryDate; //入职日期
    private Integer deptId; //关联的部门ID
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime; //创建时间
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime; //修改时间

    //封装部门名称数
    private String deptName; //部门名称
    //封装工作经历
    private List<EmpExpr> exprList;
}
