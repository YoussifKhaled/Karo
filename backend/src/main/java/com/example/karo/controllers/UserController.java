package com.example.karo.controllers;

import com.example.karo.models.entities.User;
import com.example.karo.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }


    @GetMapping("/role")
    public int getRoleCurrentUser() {
        return userService.getRoleCurrentUser();
    }
    @GetMapping("/wallet")
    public Integer getWallet() {
        return userService.getBalance();
    }

    @PostMapping("/add-balance")
    public Integer addBalance(@RequestParam Integer balance) {
        return userService.addBalance(balance);

    }
}
