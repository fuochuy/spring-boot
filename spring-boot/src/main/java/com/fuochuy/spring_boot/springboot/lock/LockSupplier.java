package com.fuochuy.spring_boot.springboot.lock;

import com.fuochuy.spring_boot.springboot.exception.CustomException;

@FunctionalInterface
public interface LockSupplier<T> {

    T apply() throws CustomException;
}

