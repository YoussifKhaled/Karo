package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.models.entities.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingSpotRepository {
    private static final String SQL_INSERT_SPOT = """
            INSERT INTO parking_spot
            	(spot_id, lot_id, type, sensor_id, spot_status, sensor_status)
            VALUES
            	(?, ?, ?, ?, ?, ?);
            """;
    private static final String SQL_FIND_SPOT_BY_ID = """
            SELECT * FROM parking_spot
            WHERE spot_id = ? AND lot_id = ?;
            """;
    private static final String SQL_FIND_SPOTS_BY_LOT_ID = """
            SELECT * FROM parking_spot
            WHERE lot_id = ?
            LIMIT ?
            OFFSET ?;
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
        int count = jdbcTemplate.update(SQL_INSERT_SPOT,
                parkingSpot.getSpotId(),
                parkingSpot.getLotId(),
                parkingSpot.getType(),
                parkingSpot.getSensorId(),
                parkingSpot.getSpotStatus(),
                parkingSpot.getSensorStatus());
        return count == 1;
    }

    public ParkingSpot findSpotById(long spotId, long lotId) {
        return jdbcTemplate.queryForObject(SQL_FIND_SPOT_BY_ID, new Object[]{spotId, lotId}, parkingSpotRowMapper);
    }

    public List<ParkingSpot> findSpotsByLotId(long lotId, int limit, int offset) {
        return jdbcTemplate.query(SQL_FIND_SPOTS_BY_LOT_ID, new Object[]{lotId, limit, offset}, parkingSpotRowMapper);
    }

    public boolean updateSpotType(long spotId, String type) {
        int count = jdbcTemplate.update(SQL_UPDATE_SPOT_TYPE, type, spotId);
        return count == 1;
    }

    public boolean updateSpotStatus(long spotId, String status) {
        int count = jdbcTemplate.update(SQL_UPDATE_SPOT_STATUS, status, spotId);
        return count == 1;
    }
    public boolean updateSensorStatus(long spotId, String status) {
        int count = jdbcTemplate.update(SQL_UPDATE_SENSOR_STATUS, status, spotId);
        return count == 1;
    }
    public boolean updateSpotPrice(long spotId, Double price) {
        int count = jdbcTemplate.update(SQL_UPDATE_SPOT_PRICE, price, spotId);
        return count == 1;
    }

    public boolean deleteSpotById(long spotId) {
        int count = jdbcTemplate.update(SQL_DELETE_SPOT_BY_ID, spotId);
        return count == 1;
    }

    private final RowMapper<ParkingSpot> parkingSpotRowMapper= ((rs, rowNum) ->
            ParkingSpot.builder()
                    .spotId(rs.getLong("spot_id"))
                    .lotId(rs.getLong("lot_id"))
                    .type(rs.getString("type"))
                    .sensorId(rs.getLong("sensor_id"))
                    .spotStatus(rs.getString("spot_status"))
                    .sensorStatus(rs.getString("sensor_status"))
                    .build()
    );
}
