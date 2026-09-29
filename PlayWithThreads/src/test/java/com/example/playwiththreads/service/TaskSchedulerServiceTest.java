package com.example.playwiththreads.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskSchedulerServiceTest {

    @Test
    void notificationIsSkippedWhenRecipientIsNotConfigured() {
        EmailService emailService = mock(EmailService.class);
        TaskSchedulerService service = new TaskSchedulerService(emailService, "");
        assertFalse(service.notifyOnTaskCompletion());
        verifyNoInteractions(emailService);
    }

    @Test
    void snapshotReflectsCountersAndIsReadOnly() {
        TaskSchedulerService service = new TaskSchedulerService(mock(EmailService.class), "");
        service.executeTask1();
        service.executeTask1();
        service.executeTask2();
        var snapshot = service.taskCountsSnapshot();
        assertEquals(2, snapshot.get("task1"));
        assertEquals(1, snapshot.get("task2"));
        assertThrows(UnsupportedOperationException.class, () -> snapshot.put("task1", 99));
    }
}
