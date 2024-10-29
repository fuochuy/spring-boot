package com.fuochuy.spring_boot.springboot.exception;

public class InternalException extends CustomException {
    public InternalException() {
        super(500, "An unknown error has occurred");
    }

    public InternalException(Throwable e) {
        super(500, "An unknown error has occurred", e);
    }

    public InternalException(String message, Throwable e) {
        super(500, message, e);
    }

    public InternalException(String message) {
        super(500, message);
    }

    public InternalException(int code, String message) {
        super(code, message);
    }
}
