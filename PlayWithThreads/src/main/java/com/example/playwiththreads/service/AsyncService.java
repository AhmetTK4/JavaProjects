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
            log.error("Task {} kesintiye uğradı.", taskNumber);
        }
        log.info("Task {} tamamlandı.", taskNumber);
        return CompletableFuture.completedFuture("Task " + taskNumber + " sonucu");
    }
}
