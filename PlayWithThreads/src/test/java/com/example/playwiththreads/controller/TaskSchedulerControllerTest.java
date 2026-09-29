package com.example.playwiththreads.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "spring.mail.host=localhost",
        "spring.mail.username=test-only",
        "spring.mail.password=test-only",
        "spring.kafka.listener.auto-startup=false",
        "app.notification.recipient=ops@example.com"
})
class TaskSchedulerControllerTest {

    @MockitoBean
    JavaMailSender mailSender;

    @Autowired
    MockMvc mvc;

    @Test
    void arbitraryEmailEndpointIsNotExposed() throws Exception {
        mvc.perform(post("/scheduler/send-email")
                        .param("to", "victim@example.com")
                        .param("subject", "spam")
                        .param("text", "spam"))
                .andExpect(status().is4xxClientError());
        verify(mailSender, after(500).never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void notificationGoesOnlyToConfiguredRecipient() throws Exception {
        mvc.perform(post("/scheduler/notify")).andExpect(status().isOk());
        ArgumentCaptor<SimpleMailMessage> message = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, timeout(2000)).send(message.capture());
        assertArrayEquals(new String[]{"ops@example.com"}, message.getValue().getTo());
    }
}
