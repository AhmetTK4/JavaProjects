package com.example.playwithcaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Uses the real SlowDataSource without delay. */
@SpringBootTest(properties = "app.data.simulated-delay=PT0S")
class WriteThroughCacheTest {

    @Autowired
    DataService dataService;

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        dataService.evictAll();
    }

    @Test
    void updatePutsTheNewValueIntoTheCache() {
        assertEquals("Data for: city", dataService.getData("city"));

        assertEquals("Istanbul", dataService.updateData("city", "Istanbul"));

        // Served from the cache: the entry was replaced, not left stale or evicted.
        assertEquals("Istanbul", cacheManager.getCache(DataService.CACHE_NAME).get("city", String.class));
        assertEquals("Istanbul", dataService.getData("city"));
    }

    @Test
    void updateAlsoReachesTheSource() {
        dataService.updateData("country", "Türkiye");
        dataService.evictAll();
        assertEquals("Türkiye", dataService.getData("country"), "value must survive eviction");
    }
}
