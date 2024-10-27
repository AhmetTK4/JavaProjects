package com.example.playwithgenerics.controller;


import com.example.playwithgenerics.model.User;
import com.example.playwithgenerics.service.GenericService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generic")
@Tag(name = "Generic Controller", description = "Generic yapıyı kullanan API")
public class GenericController {

    private final GenericService<User> userService;
    private final GenericService<String> stringService;

    public GenericController(GenericService<User> userService, GenericService<String> stringService) {
        this.userService = userService;
        this.stringService = stringService;
    }

    @PostMapping("/user")
    @Operation(summary = "Yeni bir User ekle", description = "Bir kullanıcıyı listeye ekler")
    public String addUser(@RequestBody User user) {
        userService.addItem(user);
        return "User added: " + user;
    }

    @GetMapping("/users")
    @Operation(summary = "Tüm User'ları listele", description = "Tüm eklenmiş kullanıcıları döndürür")
    public List<User> getUsers() {
        return userService.getItems();
    }

    @PostMapping("/string")
    @Operation(summary = "Yeni bir String ekle", description = "Bir string veriyi listeye ekler")
    public String addString(@RequestBody String value) {
        stringService.addItem(value);
        return "String added: " + value;
    }

    @GetMapping("/strings")
    @Operation(summary = "Tüm String'leri listele", description = "Tüm eklenmiş string verilerini döndürür")
    public List<String> getStrings() {
        return stringService.getItems();
    }
}
