package com.itheima.Aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//表示只对方法进行拦截
@Retention(RetentionPolicy.RUNTIME)//表示运行时生效
public @interface Log {
}
