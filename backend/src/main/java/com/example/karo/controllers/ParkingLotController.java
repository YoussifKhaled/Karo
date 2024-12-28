package com.example.karo.controllers;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.karo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/lot")
public class ParkingLotController {
    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createParkingLot(@RequestBody ParkingLot parkingLot) {
        try {
            parkingLot.setManagerId(userService.getCurrentUser().getUserId());
            long lotId = parkingLotService.addParkingLot(parkingLot);
            return ResponseEntity.ok(lotId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllParkingLots() {
        try {
            List<ParkingLot> parkingLots = parkingLotService.getAllParkingLots();
            return ResponseEntity.ok(parkingLots);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<?> getParkingLotById(@PathVariable Long lotId) {
        try {
            ParkingLot parkingLot = parkingLotService.getParkingLotById(lotId);
            return ResponseEntity.ok(parkingLot);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-capacity")
    public ResponseEntity<?> updateParkingLot(
            @RequestParam Long lotId,
            @RequestParam int capacity) {
        try {
            boolean res = parkingLotService.updateParkingLotCapacity(lotId, capacity);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update-safe")
    public ResponseEntity<?> updateParkingLotSafe(
            @RequestParam Long lotId,
            @RequestParam int safe) {
        try {
            boolean res = parkingLotService.updateParkingLotSafe(lotId, safe);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete/{lotId}")
    public ResponseEntity<?> deleteParkingLot(@PathVariable Long lotId) {
        try {
            boolean res = parkingLotService.deleteParkingLotById(lotId);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
