package com.example.karo.models.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingLot {
    private Long lotId;

    private Long managerId;

    private Double longitude;

    private Double latitude;

    private Integer capacity;

    private Integer safe;
}
