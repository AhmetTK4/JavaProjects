package com.example.playwithcaches.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "app.data.simulated-delay=PT0S")
@AutoConfigureMockMvc
class DataControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void statsCountHitsAndMisses() throws Exception {
        mvc.perform(delete("/data")).andExpect(status().isNoContent());
        mvc.perform(get("/data").param("param", "stats-key")).andExpect(status().isOk());
        mvc.perform(get("/data").param("param", "stats-key")).andExpect(status().isOk());
        mvc.perform(get("/data/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.hits").value(1))
                .andExpect(jsonPath("$.misses").value(1));
    }
}
