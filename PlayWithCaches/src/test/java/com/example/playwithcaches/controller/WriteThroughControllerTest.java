package com.example.playwithcaches.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "app.data.simulated-delay=PT0S")
@AutoConfigureMockMvc
class WriteThroughControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void putUpdatesWhatGetReturns() throws Exception {
        mvc.perform(put("/data").param("param", "greeting").contentType("text/plain").content("Merhaba"))
                .andExpect(status().isOk())
                .andExpect(content().string("Merhaba"));
        mvc.perform(get("/data").param("param", "greeting"))
                .andExpect(content().string("Merhaba"));
    }

    @Test
    void referenceDataHasItsOwnCacheAndStats() throws Exception {
        mvc.perform(get("/reference/{key}", "TR")).andExpect(content().string("Reference value for: TR"));
        mvc.perform(get("/reference/{key}", "TR")).andExpect(status().isOk());
        mvc.perform(get("/data/stats").param("cache", "referenceCache"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hits").value(1))
                .andExpect(jsonPath("$.misses").value(1));
        mvc.perform(get("/data/stats").param("cache", "nope")).andExpect(status().isNotFound());
    }
}
