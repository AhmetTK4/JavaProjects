package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.AsyncService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@RestController
@RequiredArgsConstructor
public class TaskController {

    /** Must not exceed max pool size + queue capacity in AsyncConfig (10 + 100). */
    static final int MAX_TASKS = 100;

    private final AsyncService asyncService;

    /**
     * Starts the tasks in parallel and returns a future, so the servlet thread is released
     * while the tasks run; Spring MVC completes the response when all tasks finish.
     */
    @GetMapping("/start-tasks")
    public CompletableFuture<List<String>> startTasks(
            @RequestParam(defaultValue = "5") @Min(1) @Max(MAX_TASKS) int taskCount) {
        List<CompletableFuture<String>> futures = IntStream.rangeClosed(1, taskCount)
                .mapToObj(asyncService::processTask)
                .toList();

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(ignored -> futures.stream().map(CompletableFuture::join).toList());
    }
}
