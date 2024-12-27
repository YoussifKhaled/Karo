package com.example.karo.controllers;

import com.example.karo.models.entities.Reservation;
import com.example.karo.services.ParkingReservationService;
import com.example.karo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
public class ParkingReservationController {

    @Autowired
    private ParkingReservationService parkingReservationService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> map) {
        try {
            long driverId = userService.getCurrentUser().getUserId();

            Integer lotId = (Integer) map.get("lotId");
            Integer spotId = (Integer) map.get("spotId");
            String start = (String) map.get("start");
            String end = (String) map.get("end");
            
            Reservation reservation = Reservation.builder()
                            .driverId(driverId)
                            .lotId(Long.valueOf(lotId))
                            .spotId(Long.valueOf(spotId))
                            .start(LocalDateTime.parse(start))
                            .end(LocalDateTime.parse(end))
                            .build();

            System.out.println(reservation.getSpotId());
            long reservationId = parkingReservationService.createReservation(reservation);
            return new ResponseEntity<>(reservationId, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
