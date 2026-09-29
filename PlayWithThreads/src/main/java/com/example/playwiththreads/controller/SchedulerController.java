package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class SchedulerController {

    private final TaskSchedulerService schedulerService;

    @GetMapping("/counts")
    public Map<String, Integer> getTaskCounts() {
        return schedulerService.taskCountsSnapshot();
    }
}
