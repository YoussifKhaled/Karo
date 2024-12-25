package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingLotRepository {

    private static final String SQL_INSERT_PARKING_LOT = """
            INSERT INTO parking_lot
            	(manager_id, longitude, latitude, capacity, safe)
            VALUES
            	(?, ?, ?, ?, ?);
            """;
    private static final String SQL_FIND_LOT_BY_ID = """
            SELECT *
            FROM
            	parking_lot
            WHERE
            	lot_id = ?;
            """;
    private static final String SQL_FIND_ALL_LOTS = """
            SELECT *
            FROM parking_lot
            """;
    private static final String SQL_UPDATE_LOT_CAPACITY = """
            UPDATE parking_lot
            SET capacity = ?
            WHERE lot_id = ?;
            """;
    private static final String SQL_UPDATE_LOT_SAFE = """
            UPDATE parking_lot
            SET safe = ?
            WHERE lot_id = ?;
            """;
    private static final String SQL_DELETE_PARKING_LOT = """
            DELETE FROM parking_lot
            WHERE lot_id = ?;
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertParkingLot(ParkingLot parkingLot) {
        return true;
    }

    public ParkingLot findLotById(Long lotId) {
        return null;
    }

    public List<ParkingLot> findAllLots() {
        return null;
    }

    public Boolean updateParkingLotCapacity(Long lotId, int capacity) {
        return null;
    }

    public Boolean updateParkingLotSafe(Long lotId, int safe) {
        return null;
    }

    public Boolean deleteLotById(Long lotId) {
        return null;
    }
}
