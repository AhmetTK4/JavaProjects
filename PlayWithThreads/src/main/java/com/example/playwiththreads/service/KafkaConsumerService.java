package com.example.playwiththreads.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    // The consumer group comes from spring.kafka.consumer.group-id.
    @KafkaListener(topics = "task-updates")
    public void listen(String message) {
        log.info("Yeni mesaj alındı: {}", message);
    }
}
