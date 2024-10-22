package com.example.playwiththreads.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "task-updates", groupId = "group_id")
    public void listen(String message) {
        System.out.println("Yeni mesaj alındı: " + message);
    }
}

