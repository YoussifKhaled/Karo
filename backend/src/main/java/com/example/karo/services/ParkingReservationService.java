package com.example.karo.services;

import com.example.karo.models.entities.ParkingSpot;
import com.example.karo.models.entities.Reservation;
import com.example.karo.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ParkingReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ParkingSpotService parkingSpotService;

    public long createReservation(Reservation reservation) {
        if (reservation == null)
            throw new IllegalArgumentException("Reservation cannot be null");

        ParkingSpot spot = parkingSpotService.getParkingSpotById(reservation.getSpotId());
        if (spot == null || !Objects.equals(spot.getLotId(), reservation.getLotId()))
            throw new IllegalArgumentException("Invalid parking spot or lot ID");

        if (!"available".equals(spot.getSpotStatus()))
            throw new IllegalStateException("Parking spot is not available for reservation");

        parkingSpotService.updateParkingSpotStatus(reservation.getSpotId(), "reserved");

        return reservationRepository.insertReservation(reservation);
    }

    public List<Reservation> getReservationsByLotAndSpot(long lotId, long spotId) {
        return reservationRepository.findReservationsByLotAndSpot(lotId, spotId);
    }

    public List<Reservation> getReservationsByLotAndSpotAfterToday(long lotId, long spotId) {
        return reservationRepository.findReservationsByLotAndSpotAfterToday(lotId, spotId);
    }

    public List<Reservation> getReservationsByDriverId(long driverId) {
        return reservationRepository.findReservationByDriverId(driverId);
    }

    public List<Reservation> getReservationsByLotId(long lotId) {
        return reservationRepository.findReservationByLotId(lotId);
    }

    public boolean cancelReservationById(long reservationId) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);

        boolean deleted = reservationRepository.deleteReservationById(reservationId);

        if (deleted)
            parkingSpotService.updateParkingSpotStatus(reservation.getSpotId(), "available");

        return deleted;
    }
}

