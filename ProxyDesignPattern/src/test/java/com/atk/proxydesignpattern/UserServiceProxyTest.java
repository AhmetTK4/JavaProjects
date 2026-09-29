package com.atk.proxydesignpattern;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.AuthenticationRequiredException;
import com.atk.proxydesignpattern.exception.ForbiddenOperationException;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import com.atk.proxydesignpattern.repository.UserRepository;
import com.atk.proxydesignpattern.security.CallerContext;
import com.atk.proxydesignpattern.service.UserServiceImpl;
import com.atk.proxydesignpattern.service.UserServiceProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserServiceProxyTest {

    @Autowired
    private UserRepository userRepository;

    private String caller;
    private UserServiceProxy service;
    private User admin;
    private User regular;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        admin = userRepository.save(new User("adminUser", "admin", "admin@test.com"));
        regular = userRepository.save(new User("regularUser", "user", "user@test.com"));
        CallerContext callerContext = () -> Optional.ofNullable(caller);
        service = new UserServiceProxy(new UserServiceImpl(userRepository), callerContext);
    }

    @Test
    void adminCallerCanUpdateAnotherUsersEmail() {
        caller = "adminUser";
        service.updateUserEmail(regular.getId(), "new@test.com");
        assertEquals("new@test.com", userRepository.findById(regular.getId()).orElseThrow().getEmail());
    }

    @Test
    void regularCallerCannotUpdateAdminEmail() {
        caller = "regularUser";
        assertThrows(ForbiddenOperationException.class,
                () -> service.updateUserEmail(admin.getId(), "hijack@test.com"));
        assertEquals("admin@test.com", userRepository.findById(admin.getId()).orElseThrow().getEmail());
    }

    @Test
    void regularCallerCannotUpdateAnyEmail() {
        caller = "regularUser";
        assertThrows(ForbiddenOperationException.class,
                () -> service.updateUserEmail(regular.getId(), "new@test.com"));
    }

    @Test
    void missingOrUnknownCallerIsRejected() {
        caller = null;
        assertThrows(AuthenticationRequiredException.class,
                () -> service.updateUserEmail(regular.getId(), "new@test.com"));
        caller = "ghost";
        assertThrows(AuthenticationRequiredException.class,
                () -> service.updateUserEmail(regular.getId(), "new@test.com"));
    }

    @Test
    void adminUpdatingMissingUserGetsNotFound() {
        caller = "adminUser";
        assertThrows(UserNotFoundException.class, () -> service.updateUserEmail(9999L, "new@test.com"));
    }
}
