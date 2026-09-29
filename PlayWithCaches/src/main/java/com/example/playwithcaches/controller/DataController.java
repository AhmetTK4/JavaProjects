package com.example.playwithcaches.controller;

import com.example.playwithcaches.service.DataService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DataController {

    private final DataService dataService;
    private final CacheManager cacheManager;

    public DataController(DataService dataService, CacheManager cacheManager) {
        this.dataService = dataService;
        this.cacheManager = cacheManager;
    }

    @GetMapping(value = "/data", produces = "text/plain")
    public String getData(@RequestParam String param) {
        return dataService.getData(param);
    }

    @DeleteMapping("/data")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void evict(@RequestParam(required = false) String param) {
        if (param == null) {
            dataService.evictAll();
        } else {
            dataService.evict(param);
        }
    }

    /** Hit/miss counters recorded by Caffeine ({@code recordStats()} in CacheConfig). */
    @GetMapping("/data/stats")
    public Map<String, Object> stats() {
        Cache<Object, Object> cache = ((CaffeineCache) cacheManager.getCache(DataService.CACHE_NAME)).getNativeCache();
        CacheStats stats = cache.stats();
        return Map.of(
                "size", cache.estimatedSize(),
                "hits", stats.hitCount(),
                "misses", stats.missCount(),
                "hitRate", stats.hitRate(),
                "evictions", stats.evictionCount());
    }
}
