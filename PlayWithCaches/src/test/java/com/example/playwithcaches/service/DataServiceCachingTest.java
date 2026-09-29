package com.example.playwithcaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class DataServiceCachingTest {

    @MockitoBean
    SlowDataSource dataSource;

    @Autowired
    DataService dataService;

    @BeforeEach
    void setUp() {
        dataService.evictAll();
        when(dataSource.load(anyString())).thenAnswer(inv -> "Data for: " + inv.getArgument(0));
    }

    @Test
    void repeatedCallsHitTheSourceOnlyOnce() {
        assertEquals("Data for: a", dataService.getData("a"));
        assertEquals("Data for: a", dataService.getData("a"));
        assertEquals("Data for: a", dataService.getData("a"));
        verify(dataSource, times(1)).load("a");
    }

    @Test
    void eachParameterHasItsOwnEntry() {
        dataService.getData("a");
        dataService.getData("b");
        verify(dataSource).load("a");
        verify(dataSource).load("b");
    }

    @Test
    void evictForcesReload() {
        dataService.getData("a");
        dataService.evict("a");
        dataService.getData("a");
        verify(dataSource, times(2)).load("a");
    }
}
