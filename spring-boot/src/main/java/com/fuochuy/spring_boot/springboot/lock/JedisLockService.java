package com.fuochuy.spring_boot.springboot.lock;

public interface JedisLockService {
    JedisLock getLock(String lockKey);
}
