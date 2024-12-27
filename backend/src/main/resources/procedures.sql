DELIMITER //

CREATE PROCEDURE make_reservation(
    IN p_driver_id BIGINT,         -- The driver making the reservation
    IN p_spot_id BIGINT,           -- The spot being reserved
    IN p_lot_id BIGINT,            -- The parking lot where the spot is located
    IN p_start_time DATETIME,      -- Start time of the reservation
    IN p_end_time DATETIME,        -- End time of the reservation
    OUT p_reservation_id BIGINT    -- The reservation ID generated
)
BEGIN
    -- Variable declarations
    DECLARE base_price DECIMAL(10, 2);
    DECLARE spot_type ENUM('regular', 'disabled', 'EV charging');
    DECLARE dynamic_price DECIMAL(10, 2);
    DECLARE reservation_duration DECIMAL(10, 2);
    DECLARE driver_balance DECIMAL(10, 2);
    DECLARE start_hour INT;
    DECLARE end_hour INT;
    DECLARE lot_safe DECIMAL(10, 2);

    -- Error handling for transactions
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        -- Rollback the transaction if an error occurs
        ROLLBACK;
        -- Re-throw the error to indicate failure
        RESIGNAL;
    END;

    -- Set the transaction isolation level before starting the transaction
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

    -- Start the transaction
    START TRANSACTION;

    -- Step 1: Check for overlapping reservations directly with a lock
    SELECT COUNT(*)
    INTO @spot_reserved
    FROM reservation
    WHERE spot_id = p_spot_id
      AND lot_id = p_lot_id
      AND (
          (p_start_time BETWEEN start AND end) OR
          (p_end_time BETWEEN start AND end) OR
          (start BETWEEN p_start_time AND p_end_time) OR
          (end BETWEEN p_start_time AND p_end_time)
      )
    FOR UPDATE;

    IF @spot_reserved > 0 THEN
        -- If the spot is reserved, abort the reservation
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Spot is already reserved during the requested time';
    ELSE
        -- Step 2: Get the base price and lock the parking lot row
        SELECT price, safe
        INTO base_price, lot_safe
        FROM parking_lot
        WHERE lot_id = p_lot_id
        FOR UPDATE;

        -- Step 3: Get the spot type and lock the parking spot row
        SELECT type
        INTO spot_type
        FROM parking_spot
        WHERE spot_id = p_spot_id
        FOR UPDATE;

        -- Step 4: Calculate the dynamic price based on the spot type
        IF spot_type = 'regular' THEN
            SET dynamic_price = base_price;
        ELSEIF spot_type = 'disabled' THEN
            SET dynamic_price = base_price * 0.8;  -- 20% discount for disabled spots
        ELSEIF spot_type = 'EV charging' THEN
            SET dynamic_price = base_price * 1.2;  -- 20% surcharge for EV charging
        END IF;

        -- Step 5: Calculate the reservation duration in hours
        SET reservation_duration = TIMESTAMPDIFF(MINUTE, p_start_time, p_end_time) / 60;

        SET start_hour = HOUR(p_start_time);
        SET end_hour = HOUR(p_end_time);

        -- Check if the reservation overlaps with rush hours (13:00 to 17:00)
        IF (start_hour BETWEEN 13 AND 17) OR (end_hour BETWEEN 13 AND 17) OR
           (start_hour < 13 AND end_hour >= 13) OR (start_hour < 17 AND end_hour > 17) THEN
            -- Apply surcharge if any part of the reservation is within rush hours
            SET dynamic_price = dynamic_price * 1.25;  -- Example: 25% surcharge during rush hours
        END IF;

        -- Step 6: Calculate the total price
        SET dynamic_price = dynamic_price * reservation_duration;

        -- Step 7: Get the driver's balance and lock the driver record
        SELECT balance
        INTO driver_balance
        FROM driver
        WHERE user_id = p_driver_id
        FOR UPDATE;

        -- Step 8: Ensure the driver has sufficient balance
        IF driver_balance < dynamic_price THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Insufficient balance for reservation';
        ELSE
            -- Step 9: Deduct the cost from the driver's balance
            UPDATE driver
            SET balance = balance - dynamic_price
            WHERE user_id = p_driver_id;

            -- Step 10: Add the amount to the parking lot's safe
            UPDATE parking_lot
            SET safe = lot_safe + dynamic_price
            WHERE lot_id = p_lot_id;

            -- Step 11: Insert the reservation record
            INSERT INTO reservation (
                driver_id, spot_id, lot_id, start, end, initial_cost
            ) VALUES (
                p_driver_id, p_spot_id, p_lot_id, p_start_time, p_end_time, dynamic_price
            );

            -- Step 12: Retrieve the last inserted reservation ID
            SET p_reservation_id = LAST_INSERT_ID();
        END IF;
    END IF;

    -- Commit the transaction
    COMMIT;
END //

DELIMITER ;
