package com.fuochuy.spring_boot.springboot.service.impl;

import com.fuochuy.spring_boot.springboot.entity.UserEntity;
import com.fuochuy.spring_boot.springboot.repository.UserRepository;
import com.fuochuy.spring_boot.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity create() {
        return null;
    }

    @Override
    public List<UserEntity> list() {
        return List.of();
    }

    @Override
    public void delete(String id) {

    }
}
