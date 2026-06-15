package com.itheima.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
public class ClazzQueryParam {

    /**
     * 班级名称（模糊查询）
     */
    private String name;

    /**
     * 结课范围匹配的开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    /**
     * 结课范围匹配的结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    /**
     * 分页查询的页码（默认为1）
     */
    private Integer page ;
    /**
     * 分页查询的每页记录数（默认为5，根据你的备注要求）
     */
    private Integer pageSize ;
}