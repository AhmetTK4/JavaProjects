package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.service.UserServiceProxy;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerSecurityTest {
    @Test
    void protectsHttpResponse() throws Exception {
        String payload = "<script>alert(1)</script>";
        UserServiceProxy service = mock(UserServiceProxy.class);
        doThrow(new RuntimeException("database password and internal host")).when(service).updateUserEmail(1L, "test@example.com");
        MockMvc mvc = standaloneSetup(new UserController(service)).build();
        mvc.perform(put("/api/users/1/email").param("newEmail", "test@example.com"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().string("Unable to update email."));
    }
}

