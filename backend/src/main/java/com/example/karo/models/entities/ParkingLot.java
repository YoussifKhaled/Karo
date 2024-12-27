package com.example.karo.models.entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {
    private Long lotId;

    private Long managerId;

    private Double longitude;

    private Double latitude;

    private Integer capacity;

    private Integer safe;
}
