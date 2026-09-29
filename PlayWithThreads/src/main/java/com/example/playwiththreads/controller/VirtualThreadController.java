package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.VirtualThreadDemoService;
import com.example.playwiththreads.service.VirtualThreadDemoService.Comparison;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class VirtualThreadController {

    private final VirtualThreadDemoService demoService;

    /**
     * Example: {@code /virtual-threads/compare?tasks=1000&blockingMillis=100&platformPoolSize=20}
     * takes about 5 s on the platform pool (1000 / 20 * 100 ms) and about 0.1 s on virtual threads.
     */
    @GetMapping("/virtual-threads/compare")
    public Comparison compare(@RequestParam(defaultValue = "1000") @Min(1) @Max(10_000) int tasks,
                              @RequestParam(defaultValue = "100") @Min(1) @Max(1_000) long blockingMillis,
                              @RequestParam(defaultValue = "20") @Min(1) @Max(200) int platformPoolSize) {
        return demoService.compare(tasks, Duration.ofMillis(blockingMillis), platformPoolSize);
    }
}
