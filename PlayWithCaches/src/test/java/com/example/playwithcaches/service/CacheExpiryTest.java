package com.example.playwithcaches.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "app.cache.ttl=PT0.2S")
class CacheExpiryTest {

    @MockitoBean
    SlowDataSource dataSource;

    @Autowired
    DataService dataService;

    @Test
    void entryIsReloadedAfterTtl() throws InterruptedException {
        when(dataSource.load(anyString())).thenReturn("v");
        dataService.getData("ttl");
        Thread.sleep(400);
        dataService.getData("ttl");
        verify(dataSource, times(2)).load("ttl");
    }
}
