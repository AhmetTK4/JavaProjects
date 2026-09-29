package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.TaskSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scheduler")
@RequiredArgsConstructor
public class TaskSchedulerController {

    private final TaskSchedulerService schedulerService;

    @PostMapping("/task1")
    public String triggerTask1() {
        schedulerService.executeTask1();
        return "Task 1 tetiklendi.";
    }

    @PostMapping("/task2")
    public String triggerTask2() {
        schedulerService.executeTask2();
        return "Task 2 tetiklendi.";
    }

    @PostMapping("/notify")
    public String triggerNotification() {
        return schedulerService.notifyOnTaskCompletion()
                ? "Bildirim tetiklendi."
                : "Bildirim alıcısı yapılandırılmadığı için e-posta gönderilmedi.";
    }
}
