package com.fuochuy.spring_boot.springboot.lock;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisDistributedLock implements DistributedLock {

    private static final String LOCK_KEY_PREFIX = "service_name:lock:";
    private static final String LOCK_VALUE = "locked";
    private static final long LOCK_TIMEOUT_MS = 60_000;
    private static final long LOCK_ACQUIRE_TIMEOUT_MS = 10_000;
    private static final long LOCK_ACQUIRE_INTERVAL_MS = 100;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean acquire(String lockKey) throws InterruptedException {
        return acquire(lockKey, LOCK_VALUE, LOCK_TIMEOUT_MS, LOCK_ACQUIRE_TIMEOUT_MS, LOCK_ACQUIRE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean acquire(String lockKey, long clockTimeout) throws InterruptedException {
        return acquire(lockKey, LOCK_VALUE, clockTimeout, LOCK_ACQUIRE_TIMEOUT_MS, LOCK_ACQUIRE_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean acquire(String lockKey, String value, long timeout, long acquireTimeout, long acquireInterval, TimeUnit timeunit) throws InterruptedException {
        String key = LOCK_KEY_PREFIX + lockKey;
        while (acquireTimeout > 0) {
            if (Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeunit))) {
                return true;
            }
            acquireTimeout -= acquireInterval;
            timeunit.sleep(acquireInterval);
        }
        return false;
    }

    @Override
    public void release(String lockKey) {
        String key = LOCK_KEY_PREFIX + lockKey;
        stringRedisTemplate.delete(key);
    }
}
