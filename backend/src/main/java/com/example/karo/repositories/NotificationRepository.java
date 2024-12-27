package com.example.karo.repositories;

import com.example.karo.models.entities.Notification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public NotificationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long getLastInsertedNotificationId() {
        String sql = "SELECT LAST_INSERT_ID()";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<Notification> findNotificationsByUserId(Long userId) {
        String sql = """
            SELECT n.notification_id, n.sent_at, n.type, n.content
            FROM NOTIFICATION n
            INNER JOIN USER_NOTIFICATION un ON n.notification_id = un.notification_id
            WHERE un.user_id = ?
            """;
        return jdbcTemplate.query(sql, this::mapRowToNotification, userId);
    }

    private Notification mapRowToNotification(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getLong("notification_id"));
        notification.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
        notification.setType(rs.getString("type"));
        notification.setContent(rs.getString("content"));
        return notification;
    }
}
