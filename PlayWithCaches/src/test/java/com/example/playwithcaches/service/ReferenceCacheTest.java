package com.example.playwithcaches.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/** dataCache expires quickly here while referenceCache keeps its own, long TTL. */
@SpringBootTest(properties = {"app.cache.ttl=PT0.2S", "app.cache.reference-ttl=PT1H"})
class ReferenceCacheTest {

    @MockitoBean
    SlowDataSource dataSource;

    @Autowired
    DataService dataService;

    @Test
    void eachCacheUsesItsOwnTtl() throws InterruptedException {
        when(dataSource.load(anyString())).thenReturn("data");
        when(dataSource.loadReference(anyString())).thenReturn("reference");

        dataService.getData("k");
        dataService.getReference("k");
        Thread.sleep(400);
        dataService.getData("k");
        dataService.getReference("k");

        verify(dataSource, times(2)).load("k");          // dataCache entry expired
        verify(dataSource, times(1)).loadReference("k"); // referenceCache entry still valid
    }
}
