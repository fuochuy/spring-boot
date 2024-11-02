package com.fuochuy.spring_boot.springboot.service.impl;

import com.fuochuy.spring_boot.springboot.entity.ConfigurationEntity;
import com.fuochuy.spring_boot.springboot.repository.ConfigurationRepository;
import com.fuochuy.spring_boot.springboot.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository repository;
    @Override
    public List<ConfigurationEntity> list() {
        return repository.findAll();
    }
}
