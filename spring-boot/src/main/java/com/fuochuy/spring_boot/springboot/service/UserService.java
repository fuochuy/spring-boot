package com.fuochuy.spring_boot.springboot.service;

import com.fuochuy.spring_boot.springboot.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity create();

    List<UserEntity> list();

    void delete(String id);
}
