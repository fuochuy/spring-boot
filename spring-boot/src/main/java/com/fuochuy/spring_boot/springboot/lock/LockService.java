package com.fuochuy.spring_boot.springboot.lock;

import com.fuochuy.spring_boot.springboot.exception.CustomException;

public interface LockService {
    <T> T run(LockSupplier<T> supplier, Object... keys) throws CustomException;

    void run(LockRunnable runnable, Object... keys) throws CustomException;

    <T> T run(LockSupplier<T> supplier, long lockTimeout, Object... keys) throws CustomException;

    void runWithTimeout(LockRunnable runnable, long lockTimeout, Object... keys) throws CustomException;
}
