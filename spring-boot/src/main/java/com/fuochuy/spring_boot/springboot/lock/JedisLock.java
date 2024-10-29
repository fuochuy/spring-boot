package com.fuochuy.spring_boot.springboot.lock;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Slf4j
@Data
public class JedisLock {
    public static final int DEFAULT_ACQUIRED_RESOLUTION_MILLIS = 100;
    private static final int ONE_SECOND = 1000;

    public static final int DEFAULT_EXPIRY_TIME_MILLIS = 60 * ONE_SECOND;
    public static final int DEFAULT_ACQUIRE_TIMEOUT_MILLIS = 10 * ONE_SECOND;
    private final RedisTemplate<Object, Object> jedis;

    private final String lockKeyPath;

    private final int lockExpiryInMillis;
    private final int acquiredTimeoutInMillis;
    private final UUID lockUUID;
    private String description;

    private Lock lock = null;


    public JedisLock(RedisTemplate<Object, Object> jedis, String lockKey) {
        this(jedis, lockKey, DEFAULT_ACQUIRE_TIMEOUT_MILLIS, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    public JedisLock(RedisTemplate<Object, Object> jedis, String lockKey, int acquireTimeoutMillis) {
        this(jedis, lockKey, acquireTimeoutMillis, DEFAULT_EXPIRY_TIME_MILLIS);
    }

    public JedisLock(RedisTemplate<Object, Object> jedis, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis) {
        this(jedis, lockKey, acquireTimeoutMillis, expiryTimeMillis, UUID.randomUUID());
    }

    public JedisLock(RedisTemplate<Object, Object> jedis, String lockKey, int acquireTimeoutMillis, int expiryTimeMillis, UUID uuid) {
        this.jedis = jedis;
        this.lockKeyPath = lockKey;
        this.acquiredTimeoutInMillis = acquireTimeoutMillis;
        this.lockExpiryInMillis = expiryTimeMillis + 1;
        this.lockUUID = uuid;
    }

    public UUID getLockUUID() {
        return lockUUID;
    }

    public String getLockKeyPath() {
        return lockKeyPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public synchronized boolean acquire() throws InterruptedException {
        return acquire(jedis);
    }

    protected synchronized boolean acquire(RedisTemplate<Object, Object> jedis) throws InterruptedException {
        int timeout = acquiredTimeoutInMillis;
        Lock currentLock = null;
        while (timeout >= 0) {

            var newLock = asLock(System.currentTimeMillis() + lockExpiryInMillis);
            if (Boolean.TRUE.equals(jedis.opsForValue().setIfAbsent(lockKeyPath, newLock))) {
                this.lock = newLock;
                return true;
            }

            currentLock = (Lock) jedis.opsForValue().get(lockKeyPath);
            if (currentLock != null && currentLock.isExpiredOrMine(lockUUID)) {
                Lock oldValueStr = (Lock) jedis.opsForValue().getAndSet(lockKeyPath, newLock);
                if (oldValueStr != null && oldValueStr.equals(currentLock)) {
                    this.lock = newLock;
                    return true;
                }
            }

            timeout -= DEFAULT_ACQUIRED_RESOLUTION_MILLIS;
            wait(DEFAULT_ACQUIRED_RESOLUTION_MILLIS);
        }

        if (currentLock != null) {
            log.warn("Can't acquire lock: {}. Current Lock is: {}", lockKeyPath, currentLock);
        }
        return false;
    }

    public synchronized void release() {
        release(jedis);
    }

    protected synchronized void release(RedisTemplate<Object, Object> jedis) {
        if (isLocked()) {
            jedis.delete(lockKeyPath);
            this.lock = null;
        }
    }

    public synchronized boolean isLocked() {
        return this.lock != null;
    }

    public synchronized long getLockExpiryTimeInMillis() {
        return this.lock.getExpiryTime();
    }

    private Lock asLock(long expires) {
        var res = new Lock(lockUUID, expires);
        res.setDescription(description);
        return res;
    }

    protected static class Lock implements Serializable {
        /**
         *
         */
        @Serial
        private static final long serialVersionUID = -3822789103072439199L;

        private final UUID uuid;
        private final long expiryTime;
        private String description;

        protected Lock(UUID uuid, long expiryTimeInMillis) {
            this.uuid = uuid;
            this.expiryTime = expiryTimeInMillis;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public UUID getUUID() {
            return uuid;
        }

        public long getExpiryTime() {
            return expiryTime;
        }

        @Override
        public String toString() {
            return "Lock [uuid=" + uuid + ", expiryTime=" + expiryTime + ", description=" + description + "]";
        }

        boolean isExpired() {
            return getExpiryTime() < System.currentTimeMillis();
        }

        boolean isExpiredOrMine(UUID otherUUID) {
            return this.isExpired() || this.getUUID().equals(otherUUID);
        }

        @Override
        public int hashCode() {
            final var prime = 31;
            var result = 1;
            result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Lock other = (Lock) obj;
            if (uuid == null) {
                return other.uuid == null;
            } else return uuid.equals(other.uuid);
        }

    }
}
