package com.example.playwiththreads.service;

import com.example.playwiththreads.service.VirtualThreadDemoService.Comparison;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class VirtualThreadDemoServiceTest {

    @Test
    void virtualThreadsDoNotQueueBehindAFixedPool() {
        // 200 tasks x 50 ms on 10 platform threads need at least 20 rounds (>= 1 s);
        // on virtual threads they all wait concurrently.
        Comparison result = new VirtualThreadDemoService().compare(200, Duration.ofMillis(50), 10);

        assertTrue(result.platformMillis() >= 1_000, "platform pool must run in rounds: " + result);
        assertTrue(result.virtualMillis() < result.platformMillis() / 2, "virtual threads should be much faster: " + result);
    }
}
