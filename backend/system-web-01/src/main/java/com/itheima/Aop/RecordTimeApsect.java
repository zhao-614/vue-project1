package com.itheima.Aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.config.UserContext;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect//表示一个切面
@Component
@Slf4j
public class RecordTimeApsect {

    @Autowired 
    private OperateLogMapper operateLogMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
// 配置切点
    @Around("@annotation(com.itheima.Aop.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("========== AOP 切面被触发 ==========");
        log.info("目标类: {}", joinPoint.getTarget().getClass().getName());
        log.info("目标方法: {}", joinPoint.getSignature().getName());
        
        long start = System.currentTimeMillis();
        
        Object result = joinPoint.proceed();
        
        long end = System.currentTimeMillis();
        long costTime = end - start;
        
        log.info("方法执行完成，耗时: {}ms", costTime);
        
        try {
            Integer currentUserId = UserContext.getCurrentUserId();
            log.info("从 UserContext 获取的用户ID: {}", currentUserId);
            //获取当前操作用户ID
            OperateLog operateLog = new OperateLog();
            operateLog.setOperateEmpId(currentUserId);
            //获取当前时间
            operateLog.setOperateTime(LocalDateTime.now());
            //获取当前类名
            String className = joinPoint.getTarget().getClass().getName();
            operateLog.setClassName(className);
            //获取当前方法名
            String methodName = joinPoint.getSignature().getName();
            operateLog.setMethodName(methodName);
            
            Object[] args = joinPoint.getArgs();
            // 将方法参数转为 JSON
            String methodParams = objectMapper.writeValueAsString(Arrays.asList(args));
            operateLog.setMethodParams(methodParams);
            // 将返回值转为 JSON
            String returnValue = objectMapper.writeValueAsString(result);
            operateLog.setReturnValue(returnValue);
            
            operateLog.setCostTime(costTime);
            
            log.info("准备插入操作日志: {}", operateLog);
            operateLogMapper.insert(operateLog);
            log.info("操作日志插入成功！");
            
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
            e.printStackTrace();
        }
        
        return result;
    }
}
