package com.example.karo.services;

import com.example.karo.models.entities.Driver;
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

    public int getRoleCurrentUser() {
        String role = getCurrentUser().getRole();

        int roleNumber = 0; // driver
        if ("manager".equals(role)) roleNumber = 1;
        else if ("admin".equals(role)) roleNumber = 2;

        return roleNumber;
    }
    public Driver getDriver() {
        User user = getCurrentUser();
        return userRepository.findDriverById(user.getUserId());
    }

    public Integer getBalance() {
        return getDriver().getBalance();
    }

    public Integer addBalance(int balance) {
        Driver driver = getDriver();
        userRepository.addBalance(driver.getUserId(), balance);
        return driver.getBalance() + balance;
    }
}
