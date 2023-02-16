package com.dfds.demolyy.utils.otherUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过CacheBuilder构建的Cache实例具有以下特性：
 * 将数据写入缓存时是原子操作。
 * 当缓存的数据达到最大规模时，会使用“最近最少使用(LRU)”算法来清除缓存数据。
 * 每一条数据还可以基于时间回收，未使用时间超过一定时间后，数据会被回收。
 * 当缓存被清除时，会发送通知告知。
 * 提供访问统计功能。
 */
public class MachineCodeLock {
    private MachineCodeLock(){}

    private static final Cache<String, Lock> cache = CacheBuilder.newBuilder().expireAfterAccess(24, TimeUnit.HOURS).build();

    public static Lock getLock(String key){
        Lock lock = cache.getIfPresent(key);
        if (lock==null) {
            Lock l = new ReentrantLock();
            cache.put(key,l);
            lock = l;
        }
        return lock;
    }
}
