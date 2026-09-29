package com.example.playwiththreads.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AsyncService {

    @Async("taskExecutor")
    public CompletableFuture<String> processTask(int taskNumber) {
        log.info("Task {} başladı. Thread: {}", taskNumber, Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            // Kesinti bilgisini koru ki havuz ve çağıran taraf iptali görebilsin
            Thread.currentThread().interrupt();
            log.warn("Task {} kesintiye uğradı.", taskNumber);
            return CompletableFuture.failedFuture(e);
        }
        log.info("Task {} tamamlandı.", taskNumber);
        return CompletableFuture.completedFuture("Task " + taskNumber + " sonucu");
    }
}
