package com.example.playwithcaches.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    public static final String CACHE_NAME = "dataCache";
    /** Separate cache with its own, longer TTL (see CacheConfig). */
    public static final String REFERENCE_CACHE = "referenceCache";

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

    /**
     * Write-through: stores the value in the source and puts the returned value into the cache,
     * so the next {@link #getData} is a hit with the new value instead of a stale entry or a miss.
     * Unlike {@code @Cacheable}, {@code @CachePut} always runs the method. Keys over 64 characters
     * are written to the source but not cached, matching the condition on {@link #getData}.
     */
    @CachePut(cacheNames = CACHE_NAME, key = "#param", condition = "#param.length() <= 64")
    public String updateData(String param, String value) {
        dataSource.save(param, value);
        return value;
    }

    /** Rarely changing data lives in its own cache with a longer TTL. */
    @Cacheable(REFERENCE_CACHE)
    public String getReference(String key) {
        return dataSource.loadReference(key);
    }
}
