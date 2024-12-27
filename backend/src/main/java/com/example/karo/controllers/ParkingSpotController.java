package com.example.karo.controllers;

import com.example.karo.models.entities.ParkingSpot;
import com.example.karo.services.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spot")
public class ParkingSpotController {
    @Autowired
    private ParkingSpotService parkingSpotService;


    @PostMapping("/create")
    public ResponseEntity<?> createParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        try {
            Boolean res = parkingSpotService.addParkingSpot(parkingSpot);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<?> getParkingSpotById(@PathVariable Long spotId) {
        try {
            ParkingSpot parkingSpot =  parkingSpotService.getParkingSpotById(spotId);
            return ResponseEntity.ok(parkingSpot);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/lot-spots/{lotId}")
    public ResponseEntity<?> getParkingSpotsByLotId(@PathVariable Long lotId, @RequestParam int limit, @RequestParam int offset) {
        try {
            List<ParkingSpot> parkingSpots = parkingSpotService.getParkingSpotsByLotId(lotId, limit, offset);
            return ResponseEntity.ok(parkingSpots);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-spot-type")
    public ResponseEntity<?> updateParkingSpotType(
            @RequestParam Long spotId,
            @RequestParam String type) {
        try {
        boolean res = parkingSpotService.updateParkingSpotType(spotId, type);
        return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-spot-status")
    public ResponseEntity<?> updateParkingSpotStatus(
            @RequestParam Long spotId,
            @RequestParam String status) {
        try {
            boolean res = parkingSpotService.updateParkingSpotStatus(spotId, status);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-sensor-status")
    public ResponseEntity<?> updateSensorStatus(
            @RequestParam Long spotId,
            @RequestParam String status) {
        try {
            boolean res = parkingSpotService.updateSensorStatus(spotId, status);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-price")
    public ResponseEntity<?> updateParkingSpotPrice(
            @RequestParam Long spotId,
            @RequestParam Double price) {
        try {
            boolean res = parkingSpotService.updateParkingSpotPrice(spotId, price);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete/{spotId}")
    public ResponseEntity<?> deleteParkingSpot(@PathVariable Long spotId) {
        try {
            boolean res = parkingSpotService.deleteParkingSpot(spotId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
