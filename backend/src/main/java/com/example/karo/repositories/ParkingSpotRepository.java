package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingSpotRepository {
    private static final String SQL_INSERT_SPOT = """
            INSERT INTO parking_spot
            	(spot_id, lot_id, type, sensor_id, spot_status, sensor_status, price)
            VALUES
            	(?, ?, ?, ?, ?, ?, ?);
            """;
    private static final String SQL_FIND_SPOT_BY_ID = """
            SELECT * FROM parking_spot
            WHERE spot_id = ?;
            """;
    private static final String SQL_FIND_SPOTS_BY_LOT_ID = """
            SELECT * FROM parking_spot
            WHERE lot_id = ?;
            """;
    private static final String SQL_UPDATE_SPOT_TYPE = """
            UPDATE parking_spot
            SET type = ?
            WHERE spot_id = ?;
            """;
    private static final String SQL_UPDATE_SPOT_STATUS = """
            UPDATE parking_spot
            SET spot_status = ?
            WHERE spot_id = ?;
            """;
    private static final String SQL_UPDATE_SENSOR_STATUS = """
            UPDATE parking_spot
            SET sensor_status = ?
            WHERE spot_id = ?;
            """;
    private static final String SQL_UPDATE_SPOT_PRICE = """
            UPDATE parking_spot
            SET price = ?
            WHERE spot_id = ?;
            """;
    private static final String SQL_DELETE_SPOT_BY_ID = """
            DELETE FROM parking_spot
            WHERE spot_id = ?;
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertParkingSpot(ParkingSpot parkingSpot) {
        return true;
    }

    public ParkingSpot findSpotById(long spotId) {
        return null;
    }

    public List<ParkingSpot> findSpotsByLotId(long lotId) {
        return null;
    }

    public boolean updateSpotType(long spotId, String type) {
        return true;
    }

    public boolean updateSpotStatus(long spotId, String status) {
        return true;
    }
    public boolean updateSensorStatus(long spotId, String status) {
        return true;
    }
    public boolean updateSpotPrice(long spotId, Double price) {
        return true;
    }

    public boolean deleteSpotById(long spotId) {
        return true;
    }
}
