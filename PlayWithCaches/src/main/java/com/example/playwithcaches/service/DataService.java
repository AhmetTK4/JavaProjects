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

    /**
     * The first call per {@code param} hits the slow source; later calls are served from the cache.
     * <ul>
     *   <li>{@code sync = true}: when many requests miss the same key at once, only one of them
     *       loads it and the others wait for that result (cache stampede protection).</li>
     *   <li>{@code condition}: keys longer than 64 characters bypass the cache, so arbitrary long
     *       inputs cannot push useful entries out of the size-limited cache.</li>
     * </ul>
     */
    @Cacheable(cacheNames = CACHE_NAME, sync = true, condition = "#param.length() <= 64")
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
