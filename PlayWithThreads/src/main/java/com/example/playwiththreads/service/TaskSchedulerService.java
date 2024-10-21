package com.example.playwiththreads.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
@Slf4j
public class TaskSchedulerService {


    private final Map<String, AtomicInteger> taskCounts = new ConcurrentHashMap<>();

    public TaskSchedulerService() {
        taskCounts.put("task1", new AtomicInteger(0));
        taskCounts.put("task2", new AtomicInteger(0));
    }


    @Scheduled(fixedRate = 5000)
    public void scheduledTask1() {
        int count = taskCounts.get("task1").incrementAndGet();
        log.info("Scheduled Task 1 çalıştı. Sayı: {}", count);
    }


    @Scheduled(fixedRate = 10000)
    public void scheduledTask2() {
        int count = taskCounts.get("task2").incrementAndGet();
        log.info("Scheduled Task 2 çalıştı. Sayı: {}", count);
    }

}
