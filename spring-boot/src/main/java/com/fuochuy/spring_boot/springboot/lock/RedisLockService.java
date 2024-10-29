package com.fuochuy.spring_boot.springboot.lock;


import com.fuochuy.spring_boot.springboot.exception.CustomException;
import com.fuochuy.spring_boot.springboot.exception.LockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisLockService implements LockService {

    private final DistributedLock lock;

    @Override
    public <T> T run(LockSupplier<T> supplier, Object... keys) throws CustomException {
        return execute(supplier, toList(keys));
    }

    @Override
    public void run(LockRunnable runnable, Object... keys) throws CustomException {
        execute(runnable, toList(keys));
    }

    @Override
    public <T> T run(LockSupplier<T> supplier, long lockTimeout, Object... keys) throws CustomException {
        return execute(supplier, lockTimeout, toList(keys));
    }

    @Override
    public void runWithTimeout(LockRunnable runnable, long lockTimeout, Object... keys) throws CustomException {
        execute(runnable, lockTimeout, toList(keys));
    }

    private <T> T execute(LockSupplier<T> function, List<String> lockKeys) throws CustomException {
        if (lockKeys == null) {
            lockKeys = new ArrayList<>();
        }
        if (lockKeys.isEmpty()) {
            return function.apply();
        }
        boolean locked = false;
        try {
            if (lock.acquire(lockKeys.get(0))) {
                locked = true;
                return execute(function, lockKeys.subList(1, lockKeys.size()));
            } else {
                throw new LockException();
            }
        } catch (InterruptedException e) {
            throw new LockException();
        } finally {
            if (locked) {
                lock.release(lockKeys.get(0));
            }
        }
    }

    private void execute(LockRunnable runnable, List<String> lockKeys) throws CustomException {
        if (lockKeys == null) {
            lockKeys = new ArrayList<>();
        }
        if (lockKeys.isEmpty()) {
            runnable.apply();
            return;
        }
        boolean locked = false;
        try {
            if (lock.acquire(lockKeys.get(0))) {
                locked = true;
                execute(runnable, lockKeys.subList(1, lockKeys.size()));
            } else {
                throw new LockException();
            }
        } catch (InterruptedException e) {
            throw new LockException();
        } finally {
            if (locked) {
                lock.release(lockKeys.get(0));
            }
        }
    }

    private <T> T execute(LockSupplier<T> function, long lockTimeout, List<String> lockKeys) throws CustomException {
        if (lockKeys == null) {
            lockKeys = new ArrayList<>();
        }
        if (lockKeys.isEmpty()) {
            return function.apply();
        }
        boolean locked = false;
        try {
            if (lock.acquire(lockKeys.get(0), lockTimeout)) {
                locked = true;
                return execute(function, lockKeys.subList(1, lockKeys.size()));
            } else {
                throw new LockException();
            }
        } catch (InterruptedException e) {
            throw new LockException();
        } finally {
            if (locked) {
                lock.release(lockKeys.get(0));
            }
        }
    }

    private void execute(LockRunnable runnable, long lockTimeout, List<String> lockKeys) throws CustomException {
        if (lockKeys == null) {
            lockKeys = new ArrayList<>();
        }
        if (lockKeys.isEmpty()) {
            runnable.apply();
            return;
        }
        boolean locked = false;
        try {
            if (lock.acquire(lockKeys.get(0), lockTimeout)) {
                locked = true;
                execute(runnable, lockKeys.subList(1, lockKeys.size()));
            } else {
                throw new LockException();
            }
        } catch (InterruptedException e) {
            throw new LockException();
        } finally {
            if (locked) {
                lock.release(lockKeys.get(0));
            }
        }
    }

    private List<String> toList(Object... objects) {
        return objects == null
                ? new ArrayList<>()
                : Arrays.stream(objects).map(i -> Objects.toString(i, "null")).toList();
    }
}

