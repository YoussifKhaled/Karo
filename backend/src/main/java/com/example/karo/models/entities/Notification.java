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
public class Notification {
    private Long notificationId;

    private LocalDateTime sentAt;

    // Values: "Payment Reminder", "Spot Availability", "Reservation Confirmation", "Sensor Alert", "General Update"
    private String type;

    private String content;
}
