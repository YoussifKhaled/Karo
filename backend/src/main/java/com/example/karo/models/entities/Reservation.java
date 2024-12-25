package com.example.karo.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {
    private Long reservationId;

    private Long driverId;

    private Long spotId;

    private Long lotId;

    private LocalDateTime start;

    private LocalDateTime end;

    // Values: "Not Shown", "Over stayed"
    private String violation;

    private Double initialCost;
}
