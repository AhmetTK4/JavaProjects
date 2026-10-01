package com.example.playwithcaches.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CacheStampedeTest {

    @MockitoBean
    SlowDataSource dataSource;

    @Autowired
    DataService dataService;

    @BeforeEach
    void setUp() {
        dataService.evictAll();
        when(dataSource.load(anyString())).thenAnswer(inv -> {
            Thread.sleep(300); // slow enough that all threads miss at the same time
            return "Data for: " + inv.getArgument(0);
        });
    }

    @Test
    void concurrentMissesForOneKeyLoadItOnce() throws Exception {
        int threads = 20;
        CountDownLatch start = new CountDownLatch(1);
        try (ExecutorService executor = Executors.newFixedThreadPool(threads)) {
            List<Future<String>> results = IntStream.range(0, threads)
                    .mapToObj(i -> executor.submit(() -> {
                        start.await();
                        return dataService.getData("hot");
                    }))
                    .toList();
            start.countDown();
            for (Future<String> result : results) {
                assertEquals("Data for: hot", result.get());
            }
        }
        verify(dataSource, times(1)).load("hot");
    }

    @Test
    void longKeysBypassTheCache() {
        String longKey = "k".repeat(65);
        dataService.getData(longKey);
        dataService.getData(longKey);
        verify(dataSource, times(2)).load(longKey);

        String maxKey = "k".repeat(64);
        dataService.getData(maxKey);
        dataService.getData(maxKey);
        verify(dataSource, times(1)).load(maxKey);
    }
}
