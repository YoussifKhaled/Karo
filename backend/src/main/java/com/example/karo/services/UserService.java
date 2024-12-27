package com.example.karo.services;

import com.example.karo.models.entities.User;
import com.example.karo.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;
    public UserService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public User getCurrentUser() {
        String email = authenticationService.getCurrentEmail();
        return userRepository.findUserByEmail(email);
    }
}
