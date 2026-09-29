package com.example.playwithstreams.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Uses the seed data from LoadDatabase. */
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void thresholdIsAQueryParameter() throws Exception {
        mvc.perform(get("/employees/high-salary")).andExpect(jsonPath("$.length()").value(3));
        mvc.perform(get("/employees/high-salary").param("minSalary", "7000"))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Eve"));
    }

    @Test
    void aggregateEndpoints() throws Exception {
        mvc.perform(get("/employees/total-salary")).andExpect(content().string("30000.00"));
        mvc.perform(get("/employees/average-salary-by-department"))
                .andExpect(jsonPath("$.IT").value(6000.00));
        mvc.perform(get("/employees/top-earner-by-department"))
                .andExpect(jsonPath("$.Sales.name").value("Eve"));
        mvc.perform(get("/employees/salary-range"))
                .andExpect(jsonPath("$.lowest.name").value("Charlie"))
                .andExpect(jsonPath("$.highest.name").value("Eve"));
        mvc.perform(get("/employees/partition-by-salary"))
                .andExpect(jsonPath("$.true.length()").value(3))
                .andExpect(jsonPath("$.false.length()").value(2));
    }
}
