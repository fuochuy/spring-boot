package com.fuochuy.spring_boot.springboot.utils;

import com.fuochuy.spring_boot.springboot.exception.ConfigException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ConfigUtils {
    private ConfigUtils() {
    }

    public static String checkAndGet(Map<String, String> configs, String key) throws ConfigException {
        String value = configs.get(key);
        if (value == null) {
            throwExceptionMissingParam(key);
        }
        printConfig(key, value);
        return value;
    }

    public static int checkAndGetInt(Map<String, String> configs, String key) throws ConfigException {
        String value = checkAndGet(configs, key);
        return Integer.parseInt(value);
    }

    public static long checkAndGetLong(Map<String, String> configs, String key) throws ConfigException {
        String value = checkAndGet(configs, key);
        return Long.parseLong(value);
    }

    public static boolean checkAndGetBool(Map<String, String> configs, String key) throws ConfigException {
        String value = checkAndGet(configs, key);
        return Boolean.parseBoolean(value);
    }

    private static void throwExceptionMissingParam(String param) throws ConfigException {
        throw new ConfigException(String.format("Missing parameter '%s'.", param));
    }

    private static void printConfig(String key, Object value) {
        log.info("Property '{}' = {}", key, value);
    }
}
