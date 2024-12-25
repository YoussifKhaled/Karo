package com.example.karo.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Notification {
    private Long notificationId;

    private LocalDateTime sentAt;

    // Values: "Payment Reminder", "Spot Availability", "Reservation Confirmation", "Sensor Alert", "General Update"
    private String type;

    private String content;
}
