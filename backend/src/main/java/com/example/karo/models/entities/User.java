package com.example.karo.models.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class User {
    private Long userId;

    private String name;

    private Date dateOfBirth;

    private String email;

    private String passwordHash;

    // Values: "regular", "admin", "driver"
    private String role;
}
