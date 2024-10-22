package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String message) {
        kafkaProducerService.sendMessage("task-updates", message);
        return "Mesaj Kafka'ya gönderildi: " + message;
    }
}
