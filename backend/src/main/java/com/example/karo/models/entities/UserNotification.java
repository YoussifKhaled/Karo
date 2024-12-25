package com.example.karo.models.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserNotification {
    private Long userId;
    
    private Long notificationId;
}
