package com.fuochuy.spring_boot.springboot.lock;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class LockServiceRef {

    @Getter
    private static LockServiceRef ref;
    private final LockService lockService;

    public LockServiceRef(LockService lockService) {
        this.lockService = lockService;
        ref = this;
    }
}