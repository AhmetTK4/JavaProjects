package com.example.playwiththreads.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

/**
 * Runs the same blocking workload on a fixed pool of platform threads and on virtual threads.
 * Blocking calls (sleep, I/O) park a virtual thread without holding an OS thread, so thousands
 * of them can wait at the same time; a fixed pool can only run {@code poolSize} tasks at once.
 */
@Service
public class VirtualThreadDemoService {

    public record Comparison(int tasks, long blockingMillis, int platformPoolSize,
                             long platformMillis, long virtualMillis) {
    }

    public Comparison compare(int tasks, Duration blockingTime, int platformPoolSize) {
        long platformMillis;
        try (ExecutorService platform = Executors.newFixedThreadPool(platformPoolSize)) {
            platformMillis = run(platform, tasks, blockingTime);
        }
        long virtualMillis;
        try (ExecutorService virtual = Executors.newVirtualThreadPerTaskExecutor()) {
            virtualMillis = run(virtual, tasks, blockingTime);
        }
        return new Comparison(tasks, blockingTime.toMillis(), platformPoolSize, platformMillis, virtualMillis);
    }

    private long run(ExecutorService executor, int tasks, Duration blockingTime) {
        long start = System.nanoTime();
        List<Future<?>> futures = IntStream.range(0, tasks)
                .<Future<?>>mapToObj(i -> executor.submit(() -> block(blockingTime)))
                .toList();
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for tasks", e);
            } catch (Exception e) {
                throw new IllegalStateException("Task failed", e);
            }
        }
        return (System.nanoTime() - start) / 1_000_000;
    }

    private static void block(Duration blockingTime) {
        try {
            Thread.sleep(blockingTime); // stands in for a blocking I/O call
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
