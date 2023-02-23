package com.dfds.demolyy.utils.otherUtils;

public class ThreadLocalUtil {

    //创建一个ThreadLocal对象
    private static ThreadLocal<Object> th = new ThreadLocal<Object>();

    // 存放
    public static void set(Object obj) {
        th.set(obj);
    }

    // 获取
    public static Object get() {
        Object obj = th.get();
        if(obj == null){
            throw new RuntimeException("还未保存数据");
        }
        return obj;
    }

    // 移除
    public static void remove() {
        th.remove();
    }
}
