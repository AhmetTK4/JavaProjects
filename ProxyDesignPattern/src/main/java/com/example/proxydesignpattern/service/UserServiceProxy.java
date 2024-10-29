package com.example.proxydesignpattern.service;

import com.example.proxydesignpattern.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceProxy implements UserService {
    private final UserServiceImpl userService;

    public UserServiceProxy(UserServiceImpl userService) {
        this.userService = userService;
    }

    private boolean isAdmin(String role) {
        return "admin".equalsIgnoreCase(role);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public void updateUserEmail(Long id, String newEmail) {
        Optional<User> optionalUser = userService.getUserById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isAdmin(user.getRole())) {
                userService.updateUserEmail(id, newEmail);
            } else {
                throw new RuntimeException("Unauthorized: Only admin can update email.");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public User findByUsername(String username) {
        return userService.findByUsername(username);
    }
}
