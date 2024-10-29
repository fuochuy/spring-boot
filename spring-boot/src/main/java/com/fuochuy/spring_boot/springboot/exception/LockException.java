package com.fuochuy.spring_boot.springboot.exception;

public class LockException extends CustomException {

    public LockException() {
        super(500, "Error lock. Please try again.");
    }
}