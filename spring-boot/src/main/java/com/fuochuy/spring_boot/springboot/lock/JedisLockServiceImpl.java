package com.fuochuy.spring_boot.springboot.lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class JedisLockServiceImpl implements JedisLockService {
    private final RedisTemplate<Object, Object> redisTemplate;

    public JedisLockServiceImpl(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public JedisLock getLock(String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        return new JedisLock(redisTemplate, lockKey, acquireTimeoutMillis, expiryTimeMillis);
    }

    public JedisLock getLock(String lockKey, int acquireTimeoutMillis) {
        return new JedisLock(redisTemplate, lockKey, acquireTimeoutMillis);
    }

    public JedisLock getLock(String lockKey) {
        return new JedisLock(redisTemplate, lockKey);
    }
}
