-- Indexes
CREATE INDEX idx_driver_user_id ON driver(user_id);
CREATE INDEX idx_parking_lot_manager_id ON parking_lot(manager_id);
CREATE INDEX idx_parking_spot_lot_id ON parking_spot(lot_id);
CREATE INDEX idx_reservation_driver_id ON reservation(driver_id);
CREATE INDEX idx_reservation_spot_id ON reservation(spot_id);
CREATE INDEX idx_reservation_lot_id ON reservation(lot_id);
CREATE INDEX idx_user_notification_user_id ON user_notification(user_id);
CREATE INDEX idx_user_notification_notification_id ON user_notification(notification_id);
CREATE INDEX idx_user_email ON user(email);
CREATE INDEX idx_driver_license_plate ON driver(license_plate_number);
CREATE INDEX idx_parking_spot_status ON parking_spot(spot_status);
CREATE INDEX idx_reservation_time ON reservation(start, end);
CREATE INDEX idx_notification_sent_at ON notification(sent_at);