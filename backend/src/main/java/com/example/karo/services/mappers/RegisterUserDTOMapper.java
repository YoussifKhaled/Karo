package com.example.karo.services.mappers;

import com.example.karo.models.dtos.RegisterUserDTO;
import com.example.karo.models.entities.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RegisterUserDTOMapper implements Function<RegisterUserDTO, User> {

    @Override
    public User apply(RegisterUserDTO registerUserDTO) {
        return User.builder()
            .name(registerUserDTO.name())
            .email(registerUserDTO.email())
            .passwordHash(registerUserDTO.password())
            .role("driver") // as default now
            .build();
    }
}
