package com.fuochuy.spring_boot.springboot.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    boolean acquire(String lockKey) throws InterruptedException;

    boolean acquire(String lockKey, long lockTimeout) throws InterruptedException;

    boolean acquire(String lockKey, String value, long timeout, long acquireTimeout, long acquireInterval, TimeUnit timeunit) throws InterruptedException;

    void release(String lockKey);
}
