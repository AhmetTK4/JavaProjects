package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUserEmail(Long id, String newEmail) {
        userRepository.findById(id).ifPresentOrElse(user -> {
            user.setEmail(newEmail);
            userRepository.save(user);
        }, () -> {
            throw new RuntimeException("User not found");
        });
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
