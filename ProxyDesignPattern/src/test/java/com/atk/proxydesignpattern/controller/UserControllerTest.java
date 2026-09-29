package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    private User user(String username) {
        return userRepository.findByUsername(username).orElseThrow();
    }

    @Test
    void adminCanUpdateEmail() throws Exception {
        Long id = user("regularUser").getId();
        mvc.perform(put("/api/users/{id}/email", id).header("X-User", "adminUser").param("newEmail", "new@example.com"))
                .andExpect(status().isOk());
        assertEquals("new@example.com", userRepository.findById(id).orElseThrow().getEmail());
    }

    @Test
    void regularUserGetsForbiddenWhenUpdatingAdmin() throws Exception {
        Long id = user("adminUser").getId();
        mvc.perform(put("/api/users/{id}/email", id).header("X-User", "regularUser").param("newEmail", "x@example.com"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
        assertEquals("admin@example.com", userRepository.findById(id).orElseThrow().getEmail());
    }

    @Test
    void missingCallerGetsUnauthorized() throws Exception {
        mvc.perform(put("/api/users/{id}/email", user("regularUser").getId()).param("newEmail", "x@example.com"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidEmailGetsBadRequest() throws Exception {
        mvc.perform(put("/api/users/{id}/email", user("regularUser").getId())
                        .header("X-User", "adminUser").param("newEmail", "not-an-email"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unknownUserGetsNotFound() throws Exception {
        mvc.perform(get("/api/users/{id}", 9999)).andExpect(status().isNotFound());
        mvc.perform(get("/api/users/name/{name}", "ghost")).andExpect(status().isNotFound());
    }

    @Test
    void existingUserIsReturned() throws Exception {
        mvc.perform(get("/api/users/name/{name}", "adminUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("admin"));
    }
}
