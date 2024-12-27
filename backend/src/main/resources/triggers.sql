DELIMITER //

CREATE TRIGGER after_reservation_insert
AFTER INSERT ON RESERVATION
FOR EACH ROW
BEGIN
    -- Insert a new notification for reservation confirmation
    INSERT INTO NOTIFICATION (sent_at, type, content)
    VALUES (NOW(), 'Reservation Confirmation', 'Your reservation has been successfully confirmed.');

    -- Get the last inserted notification ID
    SET @last_notification_id = LAST_INSERT_ID();

    -- Link the notification to the user
    INSERT INTO USER_NOTIFICATION (user_id, notification_id)
    VALUES (NEW.driver_id, @last_notification_id);
END;
//

DELIMITER ;

DELIMITER //

CREATE EVENT check_upcoming_reservations
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    -- Insert notifications for upcoming reservations
    INSERT INTO NOTIFICATION (sent_at, type, content)
    SELECT 
        NOW(),
        'Reservation Reminder',
        'Your reservation is in 15 minutes.'
    FROM RESERVATION r
    WHERE r.start BETWEEN DATE_ADD(NOW(), INTERVAL 14 MINUTE) AND DATE_ADD(NOW(), INTERVAL 16 MINUTE)
      AND NOT EXISTS (
          SELECT 1 
          FROM USER_NOTIFICATION un
          INNER JOIN NOTIFICATION n ON un.notification_id = n.notification_id
          WHERE un.user_id = r.driver_id
            AND n.type = 'Reservation Reminder'
            AND n.sent_at BETWEEN DATE_ADD(NOW(), INTERVAL -1 MINUTE) AND NOW()
      );

    -- Link the notifications to the respective users
    INSERT INTO USER_NOTIFICATION (user_id, notification_id)
    SELECT 
        r.driver_id, 
        n.notification_id
    FROM RESERVATION r
    JOIN NOTIFICATION n ON n.content = 'Your reservation is in 15 minutes.'
    WHERE r.start BETWEEN DATE_ADD(NOW(), INTERVAL 14 MINUTE) AND DATE_ADD(NOW(), INTERVAL 16 MINUTE)
      AND NOT EXISTS (
          SELECT 1 
          FROM USER_NOTIFICATION un
          WHERE un.user_id = r.driver_id
            AND un.notification_id = n.notification_id
      );
END;
//

DELIMITER ;

DELIMITER //

CREATE EVENT clean_old_notifications
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
    DELETE FROM NOTIFICATION
    WHERE sent_at < DATE_SUB(NOW(), INTERVAL 30 DAY);
END;
//

DELIMITER ;
