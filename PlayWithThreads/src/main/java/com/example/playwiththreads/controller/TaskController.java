package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final AsyncService asyncService;

    @GetMapping("/start-tasks")
    public List<String> startTasks(@RequestParam(defaultValue = "5") int taskCount) {
        List<CompletableFuture<String>> futures = new ArrayList<>();

        // Belirtilen sayıda asenkron işlem başlatılıyor
        for (int i = 1; i <= taskCount; i++) {
            futures.add(asyncService.processTask(i));
        }

        // Tüm işlemler tamamlanana kadar bekleniyor
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Sonuçlar toplanıp döndürülüyor
        List<String> results = new ArrayList<>();
        futures.forEach(f -> results.add(f.join()));
        return results;
    }
}
