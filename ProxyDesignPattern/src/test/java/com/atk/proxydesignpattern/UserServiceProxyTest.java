package com.atk.proxydesignpattern;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.repository.UserRepository;
import com.atk.proxydesignpattern.service.UserServiceProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({UserServiceProxy.class, com.atk.proxydesignpattern.service.UserServiceImpl.class})
class UserServiceProxyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceProxy service;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(new User("adminUser", "admin", "admin@test.com"));
        userRepository.save(new User("regularUser", "user", "user@test.com"));
    }

    @Test
    void adminCanUpdate() {
        User admin = userRepository.findByUsername("adminUser").orElseThrow();
        service.updateUserEmail(admin.getId(), "new@test.com");
        Assertions.assertEquals("new@test.com", userRepository.findById(admin.getId()).get().getEmail());
    }

    @Test
    void userCannotUpdate() {
        User user = userRepository.findByUsername("regularUser").orElseThrow();
        Assertions.assertThrows(RuntimeException.class, () -> service.updateUserEmail(user.getId(), "new@test.com"));
    }
}
