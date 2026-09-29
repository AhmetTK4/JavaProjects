package com.atk.proxydesignpattern.controller;

import com.atk.proxydesignpattern.entity.User;
import com.atk.proxydesignpattern.exception.UserNotFoundException;
import com.atk.proxydesignpattern.security.HeaderCallerContext;
import com.atk.proxydesignpattern.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // Receives the protection proxy, which is the primary UserService bean.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Kullanıcıyı ID ile getir")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found."));
    }

    @Operation(summary = "Kullanıcıyı name ile getir")
    @GetMapping("/name/{name}")
    public User findUserByName(@PathVariable String name) {
        return userService.findByUsername(name);
    }

    @Operation(summary = "Kullanıcının e-postasını güncelle (yalnızca admin)",
            parameters = @Parameter(in = ParameterIn.HEADER, name = HeaderCallerContext.HEADER,
                    required = true, description = "İşlemi yapan kullanıcının adı, ör. adminUser"))
    @PutMapping("/{id}/email")
    public ResponseEntity<String> updateUserEmail(@PathVariable Long id,
                                                  @RequestParam @NotBlank @Email String newEmail) {
        userService.updateUserEmail(id, newEmail);
        return ResponseEntity.ok("Email updated successfully.");
    }
}
