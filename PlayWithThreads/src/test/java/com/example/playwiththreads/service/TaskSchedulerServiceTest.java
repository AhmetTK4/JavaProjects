package com.example.playwiththreads.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class TaskSchedulerServiceTest {

    @Test
    void notificationIsSkippedWhenRecipientIsNotConfigured() {
        EmailService emailService = mock(EmailService.class);
        TaskSchedulerService service = new TaskSchedulerService(emailService, "");
        assertFalse(service.notifyOnTaskCompletion());
        verifyNoInteractions(emailService);
    }
}
