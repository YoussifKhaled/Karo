package com.example.karo.models.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Driver extends User {
    private String licensePlateNumber;

    private Integer balance;
}
