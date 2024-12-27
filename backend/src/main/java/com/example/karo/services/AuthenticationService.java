package com.example.karo.services;

import com.example.karo.models.dtos.RegisterUserDTO;
import com.example.karo.models.entities.Driver;
import com.example.karo.models.entities.User;
import com.example.karo.repositories.DriverRepository;
import com.example.karo.repositories.UserRepository;
import com.example.karo.security.JwtService;
import com.example.karo.services.mappers.RegisterUserDTOMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RegisterUserDTOMapper registerUserDTOMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final DriverRepository driverRepository;
    public AuthenticationService(UserRepository userRepository, RegisterUserDTOMapper registerUserDTOMapper,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService, DriverRepository driverRepository) {
        this.userRepository = userRepository;
        this.registerUserDTOMapper = registerUserDTOMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.driverRepository = driverRepository;
    }

    public ResponseEntity<?> signup(RegisterUserDTO registerUserDTO) {
        User user = registerUserDTOMapper.apply(registerUserDTO);
        String passwordHash = passwordEncoder.encode(user.getPassword());
        user.setPasswordHash(passwordHash);
        try {
            User savedUser = userRepository.insertUser(user);

            ResponseEntity<String> driverResponse = addDriver(registerUserDTO, savedUser);
            return driverResponse.getStatusCode() == HttpStatus.UNAUTHORIZED ? driverResponse :
                ResponseEntity.ok(savedUser);

        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Email already exists!" , HttpStatus.CONFLICT);
        }
    }

    private ResponseEntity<String> addDriver(RegisterUserDTO registerUserDTO, User savedUser) {
        try {
            Driver driver =
                Driver.builder()
                    .userId(savedUser.getUserId())
                    .licensePlateNumber(registerUserDTO.licensePlateNumber())
                    .balance(0)
                    .build();

            Driver savedDriver = driverRepository.insertDriver(driver);
            return ResponseEntity.ok("Driver is added successfully");
        } catch (DataIntegrityViolationException e) {
            userRepository.deleteUserById(savedUser.getUserId());
            return new ResponseEntity<>("License plate number already exists!"
                , HttpStatus.UNAUTHORIZED);
        }
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

    public String getCurrentEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("No authenticated user found");
    }
}
