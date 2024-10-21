package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class SchedulerController {

    private final TaskSchedulerService schedulerService;

    @GetMapping("/counts")
    public Map<String, AtomicInteger> getTaskCounts() {
        return schedulerService.getTaskCounts();
    }
}
