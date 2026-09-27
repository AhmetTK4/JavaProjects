package com.example.playwithgenerics.controller;

import com.example.playwithgenerics.service.GenericService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GenericControllerSecurityTest {
    @Test
    void protectsHttpResponse() throws Exception {
        String payload = "<script>alert(1)</script>";
        MockMvc mvc = standaloneSetup(new GenericController(new GenericService<>(), new GenericService<>())).build();
        mvc.perform(post("/api/generic/string").contentType("text/plain").content(payload).accept("*/*"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("text/plain"));
        mvc.perform(post("/api/generic/user").contentType("application/json")
                .content("{\"name\":\"" + payload + "\",\"age\":21}").accept("*/*"))
            .andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith("text/plain"));
        mvc.perform(post("/api/generic/string").contentType("text/plain").content(payload).accept("text/html"))
            .andExpect(status().isNotAcceptable());
        mvc.perform(post("/api/generic/user").contentType("application/json")
                .content("{\"name\":\"test\",\"age\":21}").accept("text/html"))
            .andExpect(status().isNotAcceptable());
    }
}

