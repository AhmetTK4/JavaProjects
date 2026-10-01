package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.service.CachingUserServiceProxy;
import com.atk.proxydesignpattern.service.CachingUserServiceProxy.CacheStats;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheStatsController {

    private final CachingUserServiceProxy cachingProxy;

    public CacheStatsController(CachingUserServiceProxy cachingProxy) {
        this.cachingProxy = cachingProxy;
    }

    @Operation(summary = "Caching proxy istatistikleri (hit, miss, kayıt sayısı)")
    @GetMapping("/api/users/cache/stats")
    public CacheStats stats() {
        return cachingProxy.stats();
    }
}
