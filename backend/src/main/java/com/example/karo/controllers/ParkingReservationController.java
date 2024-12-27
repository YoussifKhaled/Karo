package com.example.karo.controllers;

import com.example.karo.models.entities.Reservation;
import com.example.karo.services.ParkingReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ParkingReservationController {

    @Autowired
    private ParkingReservationService parkingReservationService;

    @PostMapping
    public ResponseEntity<Long> createReservation(@RequestBody Reservation reservation) {
        try {
            long reservationId = parkingReservationService.createReservation(reservation);
            return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/by-lot-and-spot")
    public ResponseEntity<List<Reservation>> getReservationsByLotAndSpot(
            @RequestParam long lotId,
            @RequestParam long spotId) {
        List<Reservation> reservations = parkingReservationService.getReservationsByLotAndSpot(lotId, spotId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/by-lot-and-spot/future")
    public ResponseEntity<List<Reservation>> getReservationsByLotAndSpotAfterToday(
            @RequestParam long lotId,
            @RequestParam long spotId) {
        List<Reservation> reservations = parkingReservationService.getReservationsByLotAndSpotAfterToday(lotId, spotId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/by-driver/{driverId}")
    public ResponseEntity<List<Reservation>> getReservationsByDriverId(@PathVariable long driverId) {
        List<Reservation> reservations = parkingReservationService.getReservationsByDriverId(driverId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/by-lot/{lotId}")
    public ResponseEntity<List<Reservation>> getReservationsByLotId(@PathVariable long lotId) {
        List<Reservation> reservations = parkingReservationService.getReservationsByLotId(lotId);
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable long reservationId) {
        boolean isDeleted = parkingReservationService.cancelReservationById(reservationId);
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
