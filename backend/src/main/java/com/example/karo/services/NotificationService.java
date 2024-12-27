package com.example.karo.services;

import com.example.karo.models.entities.Notification;
import com.example.karo.models.entities.Reservation;
import com.example.karo.repositories.NotificationRepository;
import com.example.karo.repositories.ReservationRepository;
import com.example.karo.repositories.UserNotificationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final ReservationRepository reservationRepository;
    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;

    private final UserService userService;

    public NotificationService(ReservationRepository reservationRepository,
                               NotificationRepository notificationRepository,
                               UserNotificationRepository userNotificationRepository,
                               UserService userService) {
        this.reservationRepository = reservationRepository;
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.userService = userService;
    }

    public List<Notification> getUserNotifications() {
        long userId = userService.getCurrentUser().getUserId();
        return notificationRepository.findNotificationsByUserId(userId);
    }
}
