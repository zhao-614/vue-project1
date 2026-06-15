package com.itheima.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itheima.config.FlexibleLocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    // 主键ID
    private Integer id;
    // 姓名
    private String name;
    // 学号/编号
    private String no;
    // 性别 (通常 1-男, 2-女)
    private Integer gender;
    // 手机号
    private String phone;
    // 部门 ID
    private Integer deptId;
    // 身份证号
    private String idCard;
    // 是否在校生/是否学院内部人员 (0-否, 1-是)
    private Integer isCollege;
    // 家庭住址
    private String address;
    // 毕业日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate graduationDate;
    // 违规次数
    private Integer violationCount;
    // 违规扣分
    private Integer violationScore;
    // 班级ID
    private Integer clazzId;
    // 班级名称 (如果是关联查询带出来的，可以加上)
    private String clazzName;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;

    // 省略 Getter 和 Setter 方法...
    // 在实际开发中，通常会使用 Lombok 的 @Data 注解来自动生成
}