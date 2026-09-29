package com.example.playwithcaches.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    public static final String CACHE_NAME = "dataCache";

    private final SlowDataSource dataSource;

    public DataService(SlowDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** The first call per {@code param} hits the slow source; later calls are served from the cache. */
    @Cacheable(CACHE_NAME)
    public String getData(String param) {
        return dataSource.load(param);
    }

    /** Removes one entry, e.g. after the underlying data for {@code param} changed. */
    @CacheEvict(CACHE_NAME)
    public void evict(String param) {
    }

    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void evictAll() {
    }
}
