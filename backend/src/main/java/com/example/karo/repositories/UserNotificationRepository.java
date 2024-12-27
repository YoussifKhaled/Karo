package com.example.karo.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserNotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserNotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveUserNotification(Long userId, Long notificationId) {
        String sql = "INSERT INTO USER_NOTIFICATION (user_id, notification_id) VALUES (?, ?)";
        return jdbcTemplate.update(sql, userId, notificationId);
    }
}
