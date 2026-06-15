
package com.itheima.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.itheima.pojo.Result;

import java.util.DuplicateFormatFlagsException;

@Slf4j
// 全局异常处理
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理全局异常
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常: ", e);
        return Result.error(e.getMessage());
    }

    /**
     *  处理全局异常
     * @param e 异常
     * @return 错误信息
     */
    @ExceptionHandler
    public Result handleException(DuplicateKeyException e) {
        log.error("系统异常: ", e);
        String message = e.getMessage();
        int index = message.indexOf("Duplicate entry");
        if (index != -1) {
            message = message.substring(index + 17, message.indexOf("for key", index));
            return Result.error(message + "已存在");
        }
        return Result.error(e.getMessage());
    }
}
