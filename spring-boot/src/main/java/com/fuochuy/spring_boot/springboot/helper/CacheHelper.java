package com.fuochuy.spring_boot.springboot.helper;

import com.fuochuy.spring_boot.springboot.config.ApplicationConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheHelper {

    private static final String CACHE_PREFIX = "spring-boot-%s:v1.0:";

    private final StringRedisTemplate stringRedisTemplate;


    public <T> T get(String key, Class<T> clazz) {
        try {
            return new Gson().fromJson(stringRedisTemplate.opsForValue().get(getFullKey(key)), clazz);
        } catch (Exception e) {
            log.error(String.format("Get key %s is failed.", key), e);
            return null;
        }
    }

    public void put(String key, Object value) {
        put(key, value, ApplicationConfig.getInstance().getCacheSecond());
    }

    public void put(String key, Object value, Integer timeout) {
        try {
            String valueStr = new Gson().toJson(value);
            stringRedisTemplate.opsForValue().set(getFullKey(key), valueStr, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(String.format("Put key %s is failed.", key), e);
        }
    }

    private String getFullKey(String key) {
        String cachePrefix = String.format(CACHE_PREFIX, ApplicationConfig.getInstance().getRegion());
        return cachePrefix + key;
    }
}
