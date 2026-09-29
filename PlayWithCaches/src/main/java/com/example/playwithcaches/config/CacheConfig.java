package com.example.playwithcaches.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.example.playwithcaches.service.DataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager(@Value("${app.cache.ttl:PT10S}") Duration ttl,
                                             @Value("${app.cache.maximum-size:100}") long maximumSize) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(DataService.CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(maximumSize)
                .recordStats());
        return cacheManager;
    }
}
