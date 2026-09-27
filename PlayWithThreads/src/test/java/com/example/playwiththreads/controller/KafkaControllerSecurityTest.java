package com.example.playwiththreads.controller;

import com.example.playwiththreads.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class KafkaControllerSecurityTest {
    @Test
    void protectsHttpResponse() throws Exception {
        String payload = "<script>alert(1)</script>";
        KafkaProducerService service = mock(KafkaProducerService.class);
        MockMvc mvc = standaloneSetup(new KafkaController(service)).build();
        mvc.perform(post("/send-message").param("message", payload).accept("*/*"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("text/plain"));
        verify(service).sendMessage("task-updates", payload);
        mvc.perform(post("/send-message").param("message", payload).accept("text/html"))
            .andExpect(status().isNotAcceptable());
    }
}

