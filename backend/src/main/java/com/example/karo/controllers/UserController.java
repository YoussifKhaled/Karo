package com.example.karo.controllers;

import com.example.karo.models.entities.User;
import com.example.karo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

}
