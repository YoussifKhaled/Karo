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

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void checkReservations() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime targetTime = currentTime.plusMinutes(15);
        List<Reservation> upcomingReservations = reservationRepository.findReservationsByStartTime(targetTime);

        for (Reservation reservation : upcomingReservations) {
            createNotification(reservation.getDriverId(), "Reservation Reminder", "Your reservation is in 15 minutes.");
        }

//        List<Reservation> overdueReservations = reservationRepository.findReservationsOverdue(currentTime);
//        for (Reservation reservation : overdueReservations) {
//            createNotification(reservation.getDriverId(), "Overdue Alert", "Your reservation has ended, and you are overdue.");
//        }
    }

    public void createReservationConfirmedNotification(Long userId) {
        createNotification(userId, "Reservation Confirmation", "Your reservation has been successfully confirmed.");
    }

    public void createNotification(Long userId, String type, String content) {
        // Add notification entry
        LocalDateTime now = LocalDateTime.now();
        notificationRepository.saveNotification(now, type, content);

        // Link notification to user
        Long notificationId = notificationRepository.getLastInsertedNotificationId();
        userNotificationRepository.saveUserNotification(userId, notificationId);
    }

    public List<Notification> getUserNotifications() {
        long userId = userService.getCurrentUser().getUserId();
        return notificationRepository.findNotificationsByUserId(userId);
    }
}
