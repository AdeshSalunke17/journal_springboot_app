package com.developer.journal.service;

import com.developer.journal.entity.AppCache;
import com.developer.journal.repository.AppCacheRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AppCacheService {
    @Autowired
    private AppCacheRepository appCacheRepository;

    public static Map<String, String> appCache;
    private Logger LOGGER = LoggerFactory.getLogger(AppCacheService.class);
    public static AppCache addCache(AppCache appCache1) {
        appCache.put(appCache1.getKey(), appCache1.getValue());
        return  appCache1;
    }
    @PostConstruct
    public void setAppCache() {
        appCache = new HashMap<>();
        List<AppCache> list = appCacheRepository.findAll();
        appCacheRepository.findAll().forEach(AppCacheService::addCache);
        LOGGER.info("cache set successfully {}", appCache);
    }
}
