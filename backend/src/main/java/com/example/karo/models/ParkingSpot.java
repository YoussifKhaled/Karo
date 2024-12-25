package com.example.karo.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingSpot {
    private Long spotId;

    private Long lotId;

    // Values: "regular", "disabled", "EV charging"
    private String type;

    private Long sensorId;

    private String spotStatus;

    private String sensorStatus;

    private Double price;
}
