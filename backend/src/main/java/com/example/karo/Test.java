package com.example.karo;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.repositories.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
public class Test {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    // Endpoint to create a new parking lot
    @PostMapping("/create")
    public ResponseEntity<Boolean> createParkingLot(@RequestBody ParkingLot parkingLot) {
        boolean res = parkingLotRepository.insertParkingLot(parkingLot);
        return ResponseEntity.ok(res);
    }

    // Endpoint to get all parking lots
    @GetMapping("/all")
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        System.out.println("Here");
        List<ParkingLot> parkingLots = parkingLotRepository.findAllLots();
        return ResponseEntity.ok(parkingLots);
    }

    // Endpoint to get a parking lot by ID
    @GetMapping("/{lotId}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Long lotId) {
        ParkingLot parkingLot = parkingLotRepository.findLotById(lotId);
        return ResponseEntity.ok(parkingLot);
    }

    // Endpoint to update a parking lot
    @PutMapping("/capacity")
    public ResponseEntity<Boolean> updateParkingLot(
            @RequestParam Long lotId,
            @RequestParam int capacity) {
        return ResponseEntity.ok(parkingLotRepository.updateParkingLotCapacity(lotId, capacity));
    }

    @PutMapping("/safe")
    public ResponseEntity<Boolean> updateParkingLotSafe(
            @RequestParam Long lotId,
            @RequestParam int safe) {
        return ResponseEntity.ok(parkingLotRepository.updateParkingLotSafe(lotId, safe));
    }

    // Endpoint to delete a parking lot
    @DeleteMapping("/{lotId}")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable Long lotId) {
        parkingLotRepository.deleteLotById(lotId);
        return ResponseEntity.noContent().build();
    }
}

