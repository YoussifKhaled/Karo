package com.example.karo.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot {
    private Long spotId;

    private Long lotId;

    // Values: "regular", "disabled", "EV charging"
    private String type;

    private Long sensorId;

    private String spotStatus;

    private String sensorStatus;
}
