package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.service.UserServiceProxy;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserServiceProxy userService;

    public UserController(UserServiceProxy userService) {
        this.userService = userService;
    }

    @Operation(summary = "Kullanıcıyı ID ile getir")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.ok("User not found");
        }
    }

    @Operation(summary = "Kullanıcıyı name ile getir")
    @GetMapping("/name/{name}")
    public ResponseEntity<?> findUserByName(@PathVariable String name) {
        try {
            User user = userService.findByUsername(name);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.ok("User not found");
        }
    }

    @Operation(summary = "Kullanıcının e-postasını güncelle")
    @PutMapping("/{id}/email")
    public ResponseEntity<String> updateUserEmail(@PathVariable Long id, @RequestParam String newEmail) {
        try {
            userService.updateUserEmail(id, newEmail);
            return ResponseEntity.ok("Email updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
}
