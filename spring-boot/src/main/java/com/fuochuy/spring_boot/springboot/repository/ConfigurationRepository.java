package com.fuochuy.spring_boot.springboot.repository;

import com.fuochuy.spring_boot.springboot.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, String> {
}
