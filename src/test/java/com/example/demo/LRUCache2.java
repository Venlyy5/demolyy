package com.example.demo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU实现2: 通过继承LinkedHashMap方式
 */
public class LRUCache2 extends LinkedHashMap<String, Object> {

    private int limit;

    public LRUCache2(int limit){
        // 初始容量, 负载因子, 是否按访问次序调整顺序
        super(limit*4/3, 0.75f, true);
        this.limit = limit;
    }

    // 移除最老的键值对
    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {

        // 当超过limit大小时返回true, 进行移除
        if(this.size() > this.limit){
            return true;
        }

        return false;
    }


    public static void main(String[] args) {
        // 缓存大小为5, 加入5个数据
        LRUCache2 cache2 = new LRUCache2(5);
        cache2.put("1", 1);
        cache2.put("2", 2);
        cache2.put("3", 3);
        cache2.put("4", 4);
        cache2.put("5", 5);
        System.out.println(cache2);

        // 查询key=3时, 会移动到链表最前
        cache2.get("3");
        System.out.println(cache2);

        // 新增节点时超过上限, 移除最老数据key=1
        cache2.put("6", 6);
        System.out.println(cache2);
    }
}