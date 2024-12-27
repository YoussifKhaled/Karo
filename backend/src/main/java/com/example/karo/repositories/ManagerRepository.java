package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.models.entities.TopLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ManagerRepository {

    private static final String SQL_FIND_LOTS_BY_MANAGER_ID = """
            SELECT *
            FROM parking_lot
            WHERE manager_id = ?
            """;
    private static final String SQL_LOT_UTILIZATION = """
            SELECT p.lot_id,
                   p.capacity,
                   COUNT(CASE WHEN ps.spot_status <> 'available' THEN 1 END) AS occupied_spots,
                   ROUND(COUNT(CASE WHEN ps.spot_status <> 'available' THEN 1 END) * 100.0 / p.capacity, 2) AS occupancy_rate
            FROM parking_lot p
            LEFT JOIN parking_spot ps ON p.lot_id = ps.lot_id
            WHERE p.manager_id = ?
            GROUP BY p.lot_id, p.capacity
            """;

    private final static String SQL_SPOTS_REPORT = """
            SELECT spot_status, COUNT(*) AS count
            FROM parking_spot
            WHERE lot_id = ?
            GROUP BY spot_status
        """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ParkingLot> findLotsByManagerId(long managerId) {
        return jdbcTemplate.query(SQL_FIND_LOTS_BY_MANAGER_ID, new Object[]{managerId}, parkingLotRowMapper);
    }

    public List<Map<String, Object>> getLotUtilization(long managerId) {
        return jdbcTemplate.queryForList(SQL_LOT_UTILIZATION, managerId);
    }

    public List<Map<String, Object>> getSpotStatusReport(Long lotId) {
        return jdbcTemplate.queryForList(SQL_SPOTS_REPORT, lotId);
    }

    private final RowMapper<ParkingLot> parkingLotRowMapper= ((rs, rowNum) ->
            ParkingLot.builder()
                    .lotId(rs.getLong("lot_id"))
                    .managerId(rs.getLong("manager_id"))
                    .longitude(rs.getDouble("longitude"))
                    .latitude(rs.getDouble("latitude"))
                    .capacity(rs.getInt("capacity"))
                    .safe(rs.getInt("safe"))
                    .price(rs.getDouble("price"))
                    .build()
    );
}
