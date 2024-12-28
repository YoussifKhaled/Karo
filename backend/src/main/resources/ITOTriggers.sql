DELIMITER $$

CREATE TRIGGER after_parking_spot_update
AFTER UPDATE ON parking_spot
FOR EACH ROW
BEGIN
    DECLARE overstay_hours INT;
    DECLARE fine_amount DECIMAL(10, 2);
    
    IF NEW.spot_status = 'available' THEN
        
        UPDATE reservation
        SET violation = 'Not Shown' 
        WHERE spot_id = NEW.spot_id
          AND lot_id = NEW.lot_id
          AND NOW() > end
          AND violation IS NULL;
    ELSEIF NEW.spot_status = 'occupied' THEN

        UPDATE reservation
        SET violation = 'Over Stayed'
        WHERE spot_id = NEW.spot_id
          AND lot_id = NEW.lot_id
          AND NOW() > end
          AND violation IS NULL;
          
          
        SELECT TIMESTAMPDIFF(HOUR, end, NOW()), initial_cost * 1.5 * TIMESTAMPDIFF(HOUR, end, NOW())
         INTO overstay_hours, fine_amount
         FROM reservation
         WHERE reservation.spot_id = NEW.spot_id
           AND reservation.lot_id = NEW.lot_id
           AND violation = 'Over Stayed';

         -- minus fine from driver balance
         UPDATE driver 
         JOIN reservation ON driver.user_id = reservation.driver_id
         SET driver.balance = driver.balance - fine_amount
         WHERE reservation.spot_id = NEW.spot_id
           AND reservation.lot_id = NEW.lot_id
           AND violation = 'Over Stayed';
    END IF;
END$$

DELIMITER ;
