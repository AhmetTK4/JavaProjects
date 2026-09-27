package com.example.playwithcaches.controller;

import com.example.playwithcaches.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DataControllerSecurityTest {
    @Test
    void protectsHttpResponse() throws Exception {
        String payload = "<script>alert(1)</script>";
        DataService service = mock(DataService.class);
        when(service.getData(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        MockMvc mvc = standaloneSetup(new DataController(service)).build();
        mvc.perform(get("/data").param("param", payload).accept("*/*"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("text/plain"))
            .andExpect(content().string(payload));
        mvc.perform(get("/data").param("param", payload).accept("text/html"))
            .andExpect(status().isNotAcceptable());
    }
}

