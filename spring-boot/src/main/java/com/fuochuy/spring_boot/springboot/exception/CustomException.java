package com.fuochuy.spring_boot.springboot.exception;

import lombok.Getter;

@Getter
public class CustomException extends Exception {

    private final int code;

    protected CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    protected CustomException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
