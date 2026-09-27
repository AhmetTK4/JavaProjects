package com.example.playwiththreads;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "spring.mail.host=localhost",
        "spring.mail.username=test-only",
        "spring.mail.password=test-only",
        "spring.kafka.listener.auto-startup=false"
})
class PlayWithThreadsApplicationTests {

    @MockBean
    JavaMailSender mailSender;

    @Autowired
    MockMvc mvc;

    @Test
    void sensitiveActuatorEndpointsAreNotExposed() throws Exception {
        for (String endpoint : new String[]{"prometheus", "info", "env", "configprops", "heapdump"}) {
            mvc.perform(get("/actuator/" + endpoint)).andExpect(status().isNotFound());
        }
    }

    @Test
    void contextLoads() {
    }

}
