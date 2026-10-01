package com.example.playwithstreams.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Runs on the seed data from LoadDatabase; each test is rolled back. */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeCrudControllerTest {

    @Autowired
    MockMvc mvc;

    private static String body(String name, String department, String salary) {
        return "{\"name\":\"" + name + "\",\"department\":\"" + department + "\",\"salary\":" + salary + "}";
    }

    @Test
    void createReadUpdateDelete() throws Exception {
        String location = mvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON)
                        .content(body("Frank", "IT", "6500.50")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.salary").value(6500.50))
                .andReturn().getResponse().getHeader("Location");

        mvc.perform(get(location)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Frank"));

        mvc.perform(put(location).contentType(MediaType.APPLICATION_JSON)
                        .content(body("Frank", "HR", "7000")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("HR"));

        mvc.perform(delete(location)).andExpect(status().isNoContent());
        mvc.perform(get(location)).andExpect(status().isNotFound());
        mvc.perform(delete(location)).andExpect(status().isNotFound());
    }

    @Test
    void invalidBodiesAreRejected() throws Exception {
        mvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body("", "IT", "100")))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body("Gina", "IT", "-1")))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(body("Gina", "IT", "1.005")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatingMissingEmployeeIsNotFound() throws Exception {
        mvc.perform(put("/employees/9999").contentType(MediaType.APPLICATION_JSON).content(body("X", "IT", "1")))
                .andExpect(status().isNotFound());
    }

    @Test
    void pagesAndSorts() throws Exception {
        mvc.perform(get("/employees").param("page", "0").param("size", "2").param("sort", "salary,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Eve"))
                .andExpect(jsonPath("$.content[1].name").value("Bob"))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3));

        mvc.perform(get("/employees").param("page", "2").param("size", "2").param("sort", "salary,desc"))
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Charlie"));
    }

    @Test
    void filtersByDepartmentIgnoringCase() throws Exception {
        mvc.perform(get("/employees").param("department", "it"))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.content[*].department").value(org.hamcrest.Matchers.everyItem(
                        org.hamcrest.Matchers.is("IT"))));
    }

    @Test
    void unknownSortPropertyIsBadRequest() throws Exception {
        mvc.perform(get("/employees").param("sort", "password"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail", containsString("password")));
    }

    @Test
    void pageSizeIsCapped() throws Exception {
        mvc.perform(get("/employees").param("size", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size").value(100));
    }

    @Test
    void streamEndpointsStillWin() throws Exception {
        // Literal paths such as /employees/total-salary take precedence over /employees/{id}.
        mvc.perform(get("/employees/total-salary")).andExpect(status().isOk());
    }
}
