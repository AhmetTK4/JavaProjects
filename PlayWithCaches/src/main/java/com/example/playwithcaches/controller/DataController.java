package com.example.playwithcaches.controller;

import com.example.playwithcaches.service.DataService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    /** Updates the value for {@code param} in the source and the cache (write-through). */
    @PutMapping(value = "/data", consumes = "text/plain", produces = "text/plain")
    public String updateData(@RequestParam String param, @RequestBody String value) {
        return dataService.updateData(param, value);
    }

    @GetMapping(value = "/reference/{key}", produces = "text/plain")
    public String getReference(@PathVariable String key) {
        return dataService.getReference(key);
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
    public Map<String, Object> stats(@RequestParam(defaultValue = DataService.CACHE_NAME) String cache) {
        if (!(cacheManager.getCache(cache) instanceof CaffeineCache caffeineCache)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown cache: " + cache);
        }
        Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        CacheStats stats = nativeCache.stats();
        return Map.of(
                "size", nativeCache.estimatedSize(),
                "hits", stats.hitCount(),
                "misses", stats.missCount(),
                "hitRate", stats.hitRate(),
                "evictions", stats.evictionCount());
    }
}
