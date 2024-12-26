package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
        if (parkingLot == null)
            throw new IllegalArgumentException("Parking Lot is null");

        int count = jdbcTemplate.update(
                SQL_INSERT_PARKING_LOT,
                parkingLot.getManagerId(),
                parkingLot.getLongitude(),
                parkingLot.getLatitude(),
                parkingLot.getCapacity(),
                parkingLot.getSafe());
        return count == 1;
    }

    public ParkingLot findLotById(long lotId) {
        return jdbcTemplate.queryForObject(SQL_FIND_LOT_BY_ID, new Object[]{lotId}, parkingLotRowMapper);
    }

    public List<ParkingLot> findAllLots() {
        return jdbcTemplate.query(SQL_FIND_ALL_LOTS, parkingLotRowMapper);
    }

    public boolean updateParkingLotCapacity(long lotId, int capacity) {
        int count = jdbcTemplate.update(SQL_UPDATE_LOT_CAPACITY, capacity, lotId);
        return count == 1;
    }

    public boolean updateParkingLotSafe(long lotId, int safe) {
        int count = jdbcTemplate.update(SQL_UPDATE_LOT_SAFE, safe, lotId);
        return count == 1;
    }

    public boolean deleteLotById(long lotId) {
        int count = jdbcTemplate.update(SQL_DELETE_PARKING_LOT, lotId);
        return count == 1;
    }

    private final RowMapper<ParkingLot> parkingLotRowMapper= ((rs, rowNum) ->
        ParkingLot.builder()
                .lotId(rs.getLong("lot_id"))
                .managerId(rs.getLong("manager_id"))
                .longitude(rs.getDouble("longitude"))
                .latitude(rs.getDouble("latitude"))
                .capacity(rs.getInt("capacity"))
                .safe(rs.getInt("safe"))
                .build()
    );
}
