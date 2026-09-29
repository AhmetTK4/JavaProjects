package com.atk.proxydesignpattern.service;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import com.atk.proxydesignpattern.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Real subject: performs the work without any access checks.
 */
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
    @Transactional
    public void updateUserEmail(Long id, String newEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found."));
        user.setEmail(newEmail);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + "' not found."));
    }
}
