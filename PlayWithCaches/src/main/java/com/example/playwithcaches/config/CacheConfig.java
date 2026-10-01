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
                                             @Value("${app.cache.maximum-size:100}") long maximumSize,
                                             @Value("${app.cache.reference-ttl:PT1H}") Duration referenceTtl) {
        // Default spec, used by dataCache.
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(DataService.CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ttl)
                .maximumSize(maximumSize)
                .recordStats());
        // A cache can override the default: reference data changes rarely, so it lives longer.
        cacheManager.registerCustomCache(DataService.REFERENCE_CACHE, Caffeine.newBuilder()
                .expireAfterWrite(referenceTtl)
                .maximumSize(maximumSize)
                .recordStats()
                .build());
        return cacheManager;
    }
}
