package com.example.karo.repositories;

import com.example.karo.models.entities.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ReservationRepository {

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

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int count = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_RESERVATION, new String[]{"reservation_id"});
            ps.setLong(1, reservation.getDriverId());
            ps.setLong(2, reservation.getSpotId());
            ps.setLong(3, reservation.getLotId());
            ps.setTimestamp(4, Timestamp.valueOf(reservation.getStart()));
            ps.setTimestamp(5, Timestamp.valueOf(reservation.getEnd()));
            ps.setString(6, reservation.getViolation());
            ps.setDouble(7, reservation.getInitialCost());
            return ps;
        }, keyHolder);

        if (count == 1)
            return keyHolder.getKey().longValue();
        throw new RuntimeException("Failed to insert reservation");
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
                .lotId(rs.getLong("lot_it"))
                .start(rs.getTimestamp("start").toLocalDateTime())
                .end(rs.getTimestamp("end").toLocalDateTime())
                .violation(rs.getString("violation"))
                .initialCost(rs.getDouble("initial_cost"))
                .build()
    );
}
