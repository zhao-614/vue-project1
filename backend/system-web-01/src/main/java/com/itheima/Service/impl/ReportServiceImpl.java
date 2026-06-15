package com.itheima.Service.impl;

import com.itheima.Service.ReportService;
import com.itheima.mapper.EmpMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.JobOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper StudentMapper;
    @Override
    public JobOption getEmpJobData() {
        List<Map<String,Object>> jobList = empMapper.getEmpJobData();
        //获取map的key
        List<Object> keys = jobList.stream()
                .map(map ->  map.get("pos"))
                .toList();
        //获取map的value
        List<Object> values = jobList.stream()
                .map(map ->  map.get("sum"))
                .toList();
        return new JobOption(keys, values);
    }

    @Override
    public List<Map<String,Object>> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        return StudentMapper.getStudentDegreeData();
    }
}
