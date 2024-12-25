package com.example.karo.services;

import com.example.karo.models.dtos.RegisterUserDTO;
import com.example.karo.models.entities.User;
import com.example.karo.repositories.UserRepository;
import com.example.karo.security.JwtService;
import com.example.karo.services.mappers.RegisterUserDTOMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RegisterUserDTOMapper registerUserDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, RegisterUserDTOMapper registerUserDTOMapper,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService) {
        this.userRepository = userRepository;
        this.registerUserDTOMapper = registerUserDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User signup(RegisterUserDTO registerUserDTO) {
        User user = registerUserDTOMapper.apply(registerUserDTO);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPasswordHash(passwordHash);

        return userRepository.insertUser(user);
    }

    public ResponseEntity<String> login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                email, password
            ));
        ResponseEntity<String> response;
        if (authentication.isAuthenticated()) {
            User user = userRepository.findUserByEmail(email);
            String token = jwtService.generateToken(user);
            response = new ResponseEntity<>(token, HttpStatus.OK);
        }
        else {
            response = new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
