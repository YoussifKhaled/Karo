package com.example.karo.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Reservation {
    private Long reservationId;

    private Long userId;

    private String vehicleId;

    private Long spotId;

    private Long lotId;

    private LocalDateTime start;

    private LocalDateTime end;

    // Values: "Not Shown", "Over stayed"
    private String violation;

    private Double initialCost;
}
