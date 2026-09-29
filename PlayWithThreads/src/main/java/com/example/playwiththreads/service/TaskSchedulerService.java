package com.example.playwiththreads.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Service
@Slf4j
public class TaskSchedulerService {

    private final EmailService emailService;
    private final String notificationRecipient;
    private final Map<String, AtomicInteger> taskCounts = new ConcurrentHashMap<>();

    public TaskSchedulerService(EmailService emailService,
                                @Value("${app.notification.recipient:}") String notificationRecipient) {
        this.emailService = emailService;
        this.notificationRecipient = notificationRecipient;
        taskCounts.put("task1", new AtomicInteger(0));
        taskCounts.put("task2", new AtomicInteger(0));
    }

    public void executeTask1() {
        int count = taskCounts.get("task1").incrementAndGet();
        log.info("Task 1 çalıştı. Sayı: {}", count);
    }

    /**
     * Sends the notification only to the recipient configured by the operator.
     *
     * @return {@code false} when no recipient is configured and nothing was sent
     */
    public boolean notifyOnTaskCompletion() {
        if (notificationRecipient.isBlank()) {
            log.warn("app.notification.recipient tanımlı değil, bildirim gönderilmedi.");
            return false;
        }
        int count = taskCounts.get("task1").get();
        emailService.sendEmail(
                notificationRecipient,
                "Görev Tamamlandı",
                "Task 1 tamamlandı. Mevcut sayaç: " + count
        );
        return true;
    }

    public void executeTask2() {
        int count = taskCounts.get("task2").incrementAndGet();
        log.info("Task 2 çalıştı. Sayı: {}", count);
    }
}
