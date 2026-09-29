package com.example.playwiththreads.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        // send() is asynchronous; log the broker's answer instead of ignoring failures.
        kafkaTemplate.send(topic, message).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Mesaj {} topic'ine gönderilemedi.", topic, ex);
            } else {
                log.info("Mesaj gönderildi: topic={}, partition={}, offset={}",
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            }
        });
    }
}
