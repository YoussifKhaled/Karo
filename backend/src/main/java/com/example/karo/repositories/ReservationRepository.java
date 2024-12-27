package com.example.karo.repositories;

import com.example.karo.models.entities.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findReservationsByStartTime(LocalDateTime targetTime) {
        String sql = "SELECT * FROM RESERVATION WHERE start = ?";
        return jdbcTemplate.query(sql, new ReservationRowMapper(), targetTime);
    }

//    public List<Reservation> findReservationsOverdue(LocalDateTime currentTime) {
//        String sql = "SELECT * FROM RESERVATION WHERE end < ? AND is_active = true";
//        return jdbcTemplate.query(sql, new ReservationRowMapper(), currentTime);
//    }
}

class ReservationRowMapper implements RowMapper<Reservation> {
    @Override
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getLong("reservation_id"));
        reservation.setDriverId(rs.getLong("user_id"));
        reservation.setStart(rs.getTimestamp("start_time").toLocalDateTime());
        return reservation;
    }
}
