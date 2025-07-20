package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(Long id);
    void updateUserEmail(Long id, String newEmail);
    User findByUsername(String username);
}
