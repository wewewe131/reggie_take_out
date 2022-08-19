package com.reggie.Common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前线程的变量
 */
public class BaseContext {
    //新建一个ThreadLocal对象，用于保存当前线程的变量
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //设置指定ThreadLocal变量的值
    public static void setCurrentId(Long userId) {
        threadLocal.set(userId);
    }

    //获取指定ThreadLocal变量的值
    public static Long getCurrentId() {
        return threadLocal.get();
    }

}
