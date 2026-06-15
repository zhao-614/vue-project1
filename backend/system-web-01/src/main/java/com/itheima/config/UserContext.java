package com.itheima.config;

public class UserContext {
    private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void setCurrentUserId(Integer id) {
        threadLocal.set(id);
    }

    public static Integer getCurrentUserId() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
