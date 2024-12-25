package com.example.karo.models.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Driver extends User {
    private String licensePlateNumber;

    private Integer balance;
}
