package com.fuochuy.spring_boot.springboot.config;


import com.fuochuy.spring_boot.springboot.entity.ConfigurationEntity;
import com.fuochuy.spring_boot.springboot.exception.ConfigException;
import com.fuochuy.spring_boot.springboot.helper.IpAddressHelper;
import com.fuochuy.spring_boot.springboot.service.ConfigurationService;
import com.fuochuy.spring_boot.springboot.utils.ConfigUtils;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Configuration
public class ApplicationConfig {

    @Getter
    private static ApplicationConfig instance;

    private final ConfigurationService configurationService;

    private static void initInstance(ApplicationConfig ins) {
        instance = ins;
    }

    public ApplicationConfig(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        initInstance(this);
    }

    private String appName;
    private String appId;
    private String region;
    private Integer cacheSecond;

    @PostConstruct
    public void load() throws ConfigException {
        log.info("Load application config");

        appId = String.format("%s:%s", "vbackup_gateway", IpAddressHelper.getIpAddress());
        appName = IpAddressHelper.getApp();

        List<ConfigurationEntity> configEntities = configurationService.list();
        Map<String, String> configs = new HashMap<>();
        for (ConfigurationEntity cfg : configEntities) {
            configs.put(cfg.getKey(), cfg.getValue());
        }
        try {
            region = ConfigUtils.checkAndGet(configs, "region");
            cacheSecond = ConfigUtils.checkAndGetInt(configs, "cache_second");
        } catch (Exception ignore) {

        }
    }
}
