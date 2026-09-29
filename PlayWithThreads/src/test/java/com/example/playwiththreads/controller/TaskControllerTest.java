package com.example.playwiththreads.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(properties = {
        "spring.kafka.listener.auto-startup=false",
        "app.scheduler.task1-rate=PT1H",
        "app.scheduler.task2-rate=PT1H"
})
class TaskControllerTest {

    @MockitoBean
    JavaMailSender mailSender;

    @Autowired
    MockMvc mvc;

    @Test
    void runsTasksAsynchronouslyAndReturnsAllResults() throws Exception {
        MvcResult started = mvc.perform(get("/start-tasks").param("taskCount", "3"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mvc.perform(asyncDispatch(started))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[2]").value("Task 3 sonucu"));
    }

    @Test
    void rejectsTaskCountsTheExecutorCannotAccept() throws Exception {
        mvc.perform(get("/start-tasks").param("taskCount", "0")).andExpect(status().isBadRequest());
        mvc.perform(get("/start-tasks").param("taskCount", String.valueOf(TaskController.MAX_TASKS + 1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void countsAreReturnedAsPlainNumbers() throws Exception {
        mvc.perform(get("/tasks/counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task1").isNumber())
                .andExpect(jsonPath("$.task2").isNumber());
    }
}
