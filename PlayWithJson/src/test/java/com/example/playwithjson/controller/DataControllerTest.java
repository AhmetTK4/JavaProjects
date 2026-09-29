package com.example.playwithjson.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DataControllerTest {

    @TempDir
    static Path tempDir;

    @DynamicPropertySource
    static void dataFile(DynamicPropertyRegistry registry) {
        registry.add("app.data-file", () -> tempDir.resolve("entries.json").toString());
    }

    @Autowired
    MockMvc mvc;

    @Test
    void addListAndDeleteEntry() throws Exception {
        String location = mvc.perform(post("/api/data").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"  Zeynep  \"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Zeynep"))
                .andExpect(header().string("Location", containsString("/api/data/")))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get("/api/data")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Zeynep")));
        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(delete(location)).andExpect(status().isNotFound());
    }

    @Test
    void rejectsBlankName() throws Exception {
        mvc.perform(post("/api/data").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"  \"}"))
                .andExpect(status().isBadRequest());
    }
}
