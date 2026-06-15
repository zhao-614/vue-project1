package com.itheima.Service;

import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;

import java.util.List;
import java.util.Map;

/**
 * 统计报表
 */
public interface ReportService {
    /**
     * 统计各个职位的员工人数
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别分布
     */
    List<Map<String,Object>> getEmpGenderData();

    /**
     * 统计学生学历分布
     */
    List<Map<String, Object>> getStudentDegreeData();
}
