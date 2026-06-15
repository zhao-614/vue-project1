package com.itheima.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itheima.config.FlexibleLocalDateDeserializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Clazz {
    private Integer id;
    private String name;
    private String room;
    // 对应 JSON 中的 beginDate (数据库字段为 begin_date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate beginDate;
    // 对应 JSON 中的 endDate (数据库字段为 end_date)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = FlexibleLocalDateDeserializer.class)
    private LocalDate endDate;
    //班主任 ID
    private Integer masterId;
    //subject
    private String subject;
    // 对应 JSON 中的 createTime (数据库字段为 create_time)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    // 对应 JSON 中的 updateTime (数据库字段为 update_time)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    // 扩展字段：班主任姓名（数据库中没有，通常通过关联查询或连表查询得到）
    private String masterName;
    // 扩展字段：班级状态（数据库中没有，通常根据当前时间和开课/结课时间动态计算得出）
    private String status;
}