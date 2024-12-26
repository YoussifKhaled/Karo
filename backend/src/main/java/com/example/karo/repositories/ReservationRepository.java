package com.example.karo.repositories;

import com.example.karo.models.entities.Reservation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReservationRepository {

    public long insertReservation(Reservation reservation) {
        return 0;
    }

    public List<Reservation> findReservationsByLotAndSpot(long lotId, long spotId) {
        return null;
    }

    public List<Reservation> findReservationsByLotAndSpotAfterToday(long lotId, long spotId) {
        return null;
    }

    public List<Reservation> findReservationByDriverId(long driverId) {
        return null;
    }

    public List<Reservation> findReservationByLotId(long lotId) {
        return null;
    }

    public boolean deleteReservationById(long reservationId) {
        return true;
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
