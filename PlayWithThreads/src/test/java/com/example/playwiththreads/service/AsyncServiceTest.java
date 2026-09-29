package com.example.playwiththreads.service;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncServiceTest {

    @Test
    void interruptionFailsTheTaskAndKeepsInterruptFlag() {
        Thread.currentThread().interrupt();
        CompletableFuture<String> result = new AsyncService().processTask(1);
        // Thread.interrupted() also clears the flag so later tests are unaffected.
        assertTrue(Thread.interrupted(), "interrupt flag must be restored");
        assertTrue(result.isCompletedExceptionally());
    }
}
