package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.exception.ApiExceptionHandler;
import com.atk.proxydesignpattern.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerSecurityTest {
    @Test
    void unexpectedErrorsDoNotLeakInternalDetails() throws Exception {
        UserService service = mock(UserService.class);
        doThrow(new RuntimeException("database password and internal host")).when(service).updateUserEmail(1L, "test@example.com");
        MockMvc mvc = standaloneSetup(new UserController(service)).setControllerAdvice(new ApiExceptionHandler()).build();
        mvc.perform(put("/api/users/1/email").param("newEmail", "test@example.com"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string(not(containsString("database password"))));
    }
}
