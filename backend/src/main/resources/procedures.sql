DELIMITER //

CREATE PROCEDURE create_reservation_transaction(
    IN p_driver_id BIGINT,         -- The driver making the reservation
    IN p_spot_id BIGINT,           -- The spot being reserved
    IN p_lot_id BIGINT,            -- The parking lot where the spot is located
    IN p_start_time DATETIME,      -- Start time of the reservation
    IN p_end_time DATETIME,        -- End time of the reservation
    OUT p_reservation_id BIGINT    -- The reservation ID generated
)
BEGIN
    DECLARE base_price DECIMAL(10, 2);
    DECLARE spot_type ENUM('regular', 'disabled', 'EV charging');
    DECLARE dynamic_price DECIMAL(10, 2);
    DECLARE reservation_duration INT;
    DECLARE driver_balance INT;
    DECLARE lot_safe INT;
    DECLARE start_hour INT;
    DECLARE end_hour INT;
    DECLARE spot_reserved INT;

    -- Step 1: Check if the spot is available during the requested time period
    SELECT COUNT(*) INTO spot_reserved
    FROM reservation
    WHERE spot_id = p_spot_id
    AND lot_id = p_lot_id
    AND (
        (p_start_time BETWEEN start AND end)   -- New start time overlaps
        OR
        (p_end_time BETWEEN start AND end)     -- New end time overlaps
        OR
        (start BETWEEN p_start_time AND p_end_time)  -- Existing start time overlaps
        OR
        (end BETWEEN p_start_time AND p_end_time)    -- Existing end time overlaps
    );

    -- If the spot is reserved, abort the reservation and return an error
    IF spot_reserved > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Spot is already reserved during the requested time';
    ELSE
        -- Step 2: Get the base price of the parking lot
        SELECT price INTO base_price
        FROM parking_lot
        WHERE lot_id = p_lot_id;

        -- Step 3: Get the spot type (regular, disabled, or EV charging)
        SELECT type INTO spot_type
        FROM parking_spot
        WHERE spot_id = p_spot_id;

        -- Step 4: Calculate the dynamic price based on spot type
        CASE
            WHEN spot_type = 'regular' THEN SET dynamic_price = base_price;
            WHEN spot_type = 'disabled' THEN SET dynamic_price = base_price * 0.8;  -- 20% discount for disabled
            WHEN spot_type = 'EV charging' THEN SET dynamic_price = base_price * 1.2; -- 20% surcharge for EV charging
        END CASE;

        -- Step 5: Calculate the reservation duration in hours (for pricing calculation)
        SET reservation_duration = TIMESTAMPDIFF(HOUR, p_start_time, p_end_time);

        SET start_hour = HOUR(p_start_time);
        SET end_hour = HOUR(p_end_time);

        -- Check if the reservation overlaps with rush hours (13:00 to 17:00)
        IF (start_hour BETWEEN 13 AND 17) OR (end_hour BETWEEN 13 AND 17) OR
           (start_hour < 13 AND end_hour >= 13) OR (start_hour < 17 AND end_hour > 17) THEN
            -- Apply surcharge if any part of the reservation is within rush hours
            SET dynamic_price = dynamic_price * 1.25;  -- Example: 25% surcharge during rush hours
        END IF;

        -- Step 6: Calculate the total initial cost (e.g., price per hour multiplied by reservation duration)
        SET dynamic_price = dynamic_price * reservation_duration;

        -- Step 7: Get the driver's current balance
        SELECT balance INTO driver_balance
        FROM driver
        WHERE user_id = p_driver_id;

        -- Step 8: Ensure the driver has sufficient balance for the reservation
        IF driver_balance < dynamic_price THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient balance for reservation';
        ELSE
            -- Step 9: Deduct the initial cost from the driver's balance
            UPDATE driver
            SET balance = balance - dynamic_price
            WHERE user_id = p_driver_id;

            -- Step 10: Add the same amount to the parking lot's safe
            SELECT safe INTO lot_safe
            FROM parking_lot
            WHERE lot_id = p_lot_id;

            UPDATE parking_lot
            SET safe = safe + dynamic_price
            WHERE lot_id = p_lot_id;

            -- Step 11: Insert the reservation record
            INSERT INTO reservation (
                driver_id, spot_id, lot_id, start, end, initial_cost
            ) VALUES (
                p_driver_id, p_spot_id, p_lot_id, p_start_time, p_end_time, dynamic_price
            );

            -- Step 12: Get the last inserted reservation ID
            SET p_reservation_id = LAST_INSERT_ID();
        END IF;
    END IF;
END //

DELIMITER ;
