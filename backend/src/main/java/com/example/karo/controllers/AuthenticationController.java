package com.example.karo.controllers;

import com.example.karo.models.dtos.RegisterUserDTO;
import com.example.karo.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterUserDTO registerUserDTO) {
        return authenticationService.signup(registerUserDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password) {
        return authenticationService.login(email, password);
    }
}
