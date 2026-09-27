package com.example.playwiththreads;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(properties = {
        "spring.mail.host=localhost",
        "spring.mail.username=test-only",
        "spring.mail.password=test-only",
        "spring.kafka.listener.auto-startup=false"
})
class PlayWithThreadsApplicationTests {

    @MockBean
    JavaMailSender mailSender;

    @Test
    void contextLoads() {
    }

}
