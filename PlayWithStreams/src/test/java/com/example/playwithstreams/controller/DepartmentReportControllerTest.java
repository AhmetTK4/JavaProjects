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
class DepartmentReportControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    void returnsDepartmentAndCompanyStatistics() throws Exception {
        mvc.perform(get("/employees/department-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departments.IT.headcount").value(2))
                .andExpect(jsonPath("$.departments.IT.averageSalary").value(6000.00))
                .andExpect(jsonPath("$.departments.Sales.minSalary").value(4000))
                .andExpect(jsonPath("$.departments.Sales.employees[0]").value("Charlie"))
                .andExpect(jsonPath("$.company.headcount").value(5))
                .andExpect(jsonPath("$.company.totalSalary").value(30000))
                .andExpect(jsonPath("$.company.maxSalary").value(8000));
    }
}
