package com.example.karo.repositories;

import com.example.karo.models.entities.Reservation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationRepository {

    public List<Reservation> findReservationsByStartTime(LocalDateTime targetTime) {
        String sql = "SELECT * FROM RESERVATION WHERE start = ?";
        return jdbcTemplate.query(sql, reservationRowMapper, targetTime);
    }

    private static final String SQL_INSERT_RESERVATION = """
        INSERT INTO reservation
        	(driver_id, spot_id, lot_id, start, end, violation, initial_cost)
        VALUES
        	(?, ?, ?, ?, ?, ?, ?);
        """;
    private static final String SQL_FIND_RESERVATION_BY_ID = """
        SELECT * FROM reservation
        WHERE reservation_id = ?;
        """;
    private static final String SQL_FIND_RESERVATIONS_BY_LOT_AND_SPOT = """
        SELECT * FROM reservation
        WHERE lot_id = ? AND spot_id = ?;
        """;
    private static final String SQL_FIND_RESERVATIONS_BY_LOT_AND_SPOT_AFTER_TODAY = """
        SELECT * FROM reservation
        WHERE lot_id = ? AND spot_id = ? AND start > CURRENT_DATE;
        """;
    private static final String SQL_FIND_RESERVATIONS_BY_DRIVER_ID = """
        SELECT * FROM reservation
        WHERE driver_id = ?;
        """;
    private static final String SQL_FIND_RESERVATIONS_BY_LOT_ID = """
        SELECT * FROM reservation
        WHERE lot_id = ?;
        """;
    private static final String SQL_DELETE_RESERVATION_BY_ID = """
            DELETE FROM reservation
            WHERE reservation_id = ?;
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public long insertReservation(Reservation reservation) {
        if (reservation == null)
            throw new IllegalArgumentException("Reservation cannot be null");

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("make_reservation");

        // Input parameters
        MapSqlParameterSource inParams = new MapSqlParameterSource()
                .addValue("p_driver_id", reservation.getDriverId())
                .addValue("p_spot_id", reservation.getSpotId())
                .addValue("p_lot_id", reservation.getLotId())
                .addValue("p_start_time", Timestamp.valueOf(reservation.getStart()))
                .addValue("p_end_time", Timestamp.valueOf(reservation.getEnd()));

        // Execute the stored procedure
        Map<String, Object> outParams = jdbcCall.execute(inParams);

        // Retrieve the reservation ID
        return ((Number) outParams.get("p_reservation_id")).longValue();
    }

    public Reservation findReservationById(Long reservationId) {
        return jdbcTemplate.queryForObject(SQL_FIND_RESERVATION_BY_ID, reservationRowMapper, reservationId);
    }

    public List<Reservation> findReservationsByLotAndSpot(long lotId, long spotId) {
        return jdbcTemplate.query(SQL_FIND_RESERVATIONS_BY_LOT_AND_SPOT, new Object[]{lotId, spotId}, reservationRowMapper);
    }

    public List<Reservation> findReservationsByLotAndSpotAfterToday(long lotId, long spotId) {
        return jdbcTemplate.query(SQL_FIND_RESERVATIONS_BY_LOT_AND_SPOT_AFTER_TODAY, new Object[]{lotId, spotId}, reservationRowMapper);
    }

    public List<Reservation> findReservationByDriverId(long driverId) {
        return jdbcTemplate.query(SQL_FIND_RESERVATIONS_BY_DRIVER_ID, new Object[]{driverId}, reservationRowMapper);
    }

    public List<Reservation> findReservationByLotId(long lotId) {
        return jdbcTemplate.query(SQL_FIND_RESERVATIONS_BY_LOT_ID, new Object[]{lotId}, reservationRowMapper);
    }

    public boolean deleteReservationById(long reservationId) {
        int count = jdbcTemplate.update(SQL_DELETE_RESERVATION_BY_ID, reservationId);
        return count == 1;
    }

    private final RowMapper<Reservation> reservationRowMapper = ((rs, rowNum) ->
        Reservation.builder()
                .reservationId(rs.getLong("reservation_id"))
                .driverId(rs.getLong("driver_id"))
                .spotId(rs.getLong("spot_id"))
                .lotId(rs.getLong("lot_id"))
                .start(rs.getTimestamp("start").toLocalDateTime())
                .end(rs.getTimestamp("end").toLocalDateTime())
                .violation(rs.getString("violation"))
                .initialCost(rs.getDouble("initial_cost"))
                .build()
    );
}
