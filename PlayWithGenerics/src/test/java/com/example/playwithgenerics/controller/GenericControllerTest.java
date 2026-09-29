package com.example.playwithgenerics.controller;

import com.example.playwithgenerics.model.User;
import com.example.playwithgenerics.service.GenericService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenericControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ApplicationContext context;

    @Test
    void exactlyOneServiceBeanPerElementType() {
        assertEquals(2, context.getBeanNamesForType(GenericService.class).length);
    }

    @Test
    void oldestAndNamesUseGenericOperations() throws Exception {
        mvc.perform(get("/api/generic/users/oldest")).andExpect(status().isNotFound());
        addUser("Ayşe", 30);
        addUser("Mehmet", 45);
        mvc.perform(get("/api/generic/users/oldest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mehmet"));
        mvc.perform(get("/api/generic/users/names"))
                .andExpect(jsonPath("$[0]").value("Ayşe"))
                .andExpect(jsonPath("$[1]").value("Mehmet"));
    }

    @Test
    void invalidUserIsRejected() throws Exception {
        mvc.perform(post("/api/generic/user").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"age\":-1}"))
                .andExpect(status().isBadRequest());
    }

    private void addUser(String name, int age) throws Exception {
        mvc.perform(post("/api/generic/user").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + name + "\",\"age\":" + age + "}"))
                .andExpect(status().isOk());
    }
}
