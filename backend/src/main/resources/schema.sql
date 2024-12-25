CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('regular', 'admin', 'driver') NOT NULL
);

CREATE TABLE IF NOT EXISTS driver (
    user_id BIGINT PRIMARY KEY,
    license_plate_number VARCHAR(255) UNIQUE NOT NULL,
    balance INT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parking_lot (
    lot_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    manager_id BIGINT NOT NULL,
    longitude DECIMAL(9, 6) NOT NULL,
    latitude DECIMAL(9, 6) NOT NULL,
    capacity INT NOT NULL,
    safe INT NOT NULL,
    FOREIGN KEY (manager_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS parking_spot (
    spot_id BIGINT PRIMARY KEY,
    lot_id BIGINT NOT NULL,
    type ENUM('regular', 'disabled', 'EV charging'),
    sensor_id BIGINT NOT NULL,
    spot_status ENUM('occupied', 'available', 'reserved'),
    sensor_status ENUM('active', 'inactive', 'faulty'),
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (lot_id) REFERENCES parking_lot(lot_id) ON DELETE CASCADE
);

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

CREATE TABLE IF NOT EXISTS notification (
    notification_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sent_at DATETIME NOT NULL,
    type ENUM('Payment Reminder', 'Spot Availability', 'Reservation Confirmation', 'Sensor Alert', 'General Update'),
    content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS user_notification (
    user_id BIGINT NOT NULL,
    notification_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, notification_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (notification_id) REFERENCES notification(notification_id) ON DELETE CASCADE
);
