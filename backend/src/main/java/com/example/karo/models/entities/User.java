package com.example.karo.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {
    private Long userId;

    private String name;

    private Date dateOfBirth;

    private String email;

    private String passwordHash;

    // Values: "regular", "admin", "driver"
    private String role;
}
