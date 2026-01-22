package com.jhc.fruit.common;

/**
 * @author ：lcy
 * @description：TODO
 * @date ：2022/5/9 0009 14:23
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getId(){
        return threadLocal.get();
    }

    public static void remove(){
        threadLocal.remove();
    }
}
