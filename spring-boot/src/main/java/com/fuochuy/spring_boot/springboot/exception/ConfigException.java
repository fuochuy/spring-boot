package com.fuochuy.spring_boot.springboot.exception;

public class ConfigException extends CustomException {


    public ConfigException(String message) {
        super(500, message);
    }

    public ConfigException(String message, Throwable throwable) {
        super(500, message, throwable);
    }
}
