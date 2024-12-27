-- User Table
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('regular', 'admin', 'driver') NOT NULL
);

-- Driver Table
CREATE TABLE IF NOT EXISTS driver (
    user_id BIGINT PRIMARY KEY,
    license_plate_number VARCHAR(255) UNIQUE NOT NULL,
    balance INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Parking Lot Table
CREATE TABLE IF NOT EXISTS parking_lot (
    lot_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    manager_id BIGINT NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    capacity INT NOT NULL,
    safe INT NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES user(user_id) ON DELETE CASCADE
);

-- Parking Spot Table
CREATE TABLE IF NOT EXISTS parking_spot (
    spot_id BIGINT NOT NULL,
    lot_id BIGINT NOT NULL,
    type ENUM('regular', 'disabled', 'EV charging'),
    sensor_id BIGINT NOT NULL,
    spot_status ENUM('occupied', 'available', 'reserved'),
    sensor_status ENUM('active', 'inactive', 'faulty'),
    price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (spot_id, lot_id),
    FOREIGN KEY (lot_id) REFERENCES parking_lot(lot_id) ON DELETE CASCADE
);

-- Reservation Table
CREATE TABLE IF NOT EXISTS reservation (
    reservation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id BIGINT NOT NULL,
    spot_id BIGINT NOT NULL,
    lot_id BIGINT NOT NULL,
    start DATETIME NOT NULL,
    end DATETIME NOT NULL,
    violation ENUM('Not Shown', 'Over Stayed'),
    initial_cost DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (driver_id) REFERENCES driver(user_id) ON DELETE CASCADE,
    FOREIGN KEY (spot_id) REFERENCES parking_spot(spot_id) ON DELETE CASCADE,
    FOREIGN KEY (lot_id) REFERENCES parking_lot(lot_id) ON DELETE CASCADE
);

-- Notification Table
CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sent_at DATETIME NOT NULL,
    type ENUM('Payment Reminder', 'Spot Availability', 'Reservation Confirmation', 'Sensor Alert', 'General Update'),
    content TEXT NOT NULL
);

-- User Notification Table
CREATE TABLE IF NOT EXISTS user_notification (
    user_id BIGINT NOT NULL,
    notification_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES notification(notification_id) ON DELETE CASCADE
);

-- Indexes
--CREATE INDEX idx_driver_user_id ON driver(user_id);
--CREATE INDEX idx_parking_lot_manager_id ON parking_lot(manager_id);
--CREATE INDEX idx_parking_spot_lot_id ON parking_spot(lot_id);
--CREATE INDEX idx_reservation_driver_id ON reservation(driver_id);
--CREATE INDEX idx_reservation_spot_id ON reservation(spot_id);
--CREATE INDEX idx_reservation_lot_id ON reservation(lot_id);
--CREATE INDEX idx_user_notification_user_id ON user_notification(user_id);
--CREATE INDEX idx_user_notification_notification_id ON user_notification(notification_id);
--CREATE INDEX idx_user_email ON user(email);
--CREATE INDEX idx_driver_license_plate ON driver(license_plate_number);
--CREATE INDEX idx_parking_spot_status ON parking_spot(spot_status);
--CREATE INDEX idx_reservation_time ON reservation(start, end);
--CREATE INDEX idx_notification_sent_at ON notification(sent_at);
--
---- Stored Procedures
--DELIMITER //
--CREATE PROCEDURE MakeReservation(
--    IN p_driver_id BIGINT,
--    IN p_spot_id BIGINT,
--    IN p_lot_id BIGINT,
--    IN p_start DATETIME,
--    IN p_end DATETIME,
--    IN p_initial_cost DECIMAL(10, 2)
--)
--BEGIN
--    DECLARE existing_reservations INT;
--    SELECT COUNT(*) INTO existing_reservations
--    FROM reservation
--    WHERE spot_id = p_spot_id
--      AND lot_id = p_lot_id
--      AND p_start < end
--      AND p_end > start;
--
--    IF existing_reservations > 0 THEN
--        SIGNAL SQLSTATE '45000'
--        SET MESSAGE_TEXT = 'Conflicting reservation exists';
--    ELSE
--        INSERT INTO reservation(driver_id, spot_id, lot_id, start, end, violation, initial_cost)
--        VALUES (p_driver_id, p_spot_id, p_lot_id, p_start, p_end, NULL, p_initial_cost);
--    END IF;
--END //
--DELIMITER ;
--
--DELIMITER //
--CREATE PROCEDURE DeductBalance(
--    IN p_driver_id BIGINT,
--    IN p_amount DECIMAL(10, 2)
--)
--BEGIN
--    UPDATE driver
--    SET balance = balance - p_amount
--    WHERE user_id = p_driver_id;
--
--    IF ROW_COUNT() = 0 THEN
--        SIGNAL SQLSTATE '45000'
--        SET MESSAGE_TEXT = 'Driver not found or insufficient balance';
--    END IF;
--END //
--DELIMITER ;
--
---- Triggers
--CREATE TRIGGER AfterReservationInsert
--AFTER INSERT ON reservation
--FOR EACH ROW
--BEGIN
--    UPDATE parking_spot
--    SET spot_status = 'reserved'
--    WHERE spot_id = NEW.spot_id
--      AND lot_id = NEW.lot_id;
--END;
--
--CREATE TRIGGER AfterReservationDelete
--AFTER DELETE ON reservation
--FOR EACH ROW
--BEGIN
--    UPDATE parking_spot
--    SET spot_status = 'available'
--    WHERE spot_id = OLD.spot_id
--      AND lot_id = OLD.lot_id;
--END;
--
--CREATE TRIGGER AfterReservationNotification
--AFTER INSERT ON reservation
--FOR EACH ROW
--BEGIN
--    INSERT INTO notification (sent_at, type, content)
--    VALUES (NOW(), 'Reservation Confirmation', CONCAT('Reservation ID ', NEW.reservation_id, ' confirmed.'));
--END;

---- Sample Transactions
--START TRANSACTION;
--CALL MakeReservation(1, 101, 10, '2024-12-26 10:00:00', '2024-12-26 12:00:00', 20.00);
--CALL DeductBalance(1, 20.00);
--COMMIT;
--
--START TRANSACTION;
--UPDATE parking_lot SET capacity = capacity + 10 WHERE lot_id = 5;
--INSERT INTO parking_spot (spot_id, lot_id, type, sensor_id, spot_status, sensor_status, price)
--VALUES (201, 5, 'regular', 201, 'available', 'active', 100.0);
--COMMIT;
