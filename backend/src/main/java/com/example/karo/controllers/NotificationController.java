package com.example.karo.controllers;

import com.example.karo.models.entities.Notification;
import com.example.karo.services.NotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping()
    public List<Notification> getUserNotifications() {
        System.out.println("yaraaabb");
        return notificationService.getUserNotifications();
    }
}
