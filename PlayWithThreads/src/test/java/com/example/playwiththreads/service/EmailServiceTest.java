package com.example.playwiththreads.service;

import org.junit.jupiter.api.Test;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Test
    void sendFailureIsHandledInsteadOfLostInAsyncThread() {
        JavaMailSender sender = mock(JavaMailSender.class);
        doThrow(new MailSendException("SMTP down")).when(sender).send(any(SimpleMailMessage.class));
        assertDoesNotThrow(() -> new EmailService(sender).sendEmail("ops@example.com", "s", "t"));
        verify(sender).send(any(SimpleMailMessage.class));
    }
}
