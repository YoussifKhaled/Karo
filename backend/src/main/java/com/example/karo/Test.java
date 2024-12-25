package com.example.karo;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.models.entities.ParkingSpot;
import com.example.karo.repositories.ParkingLotRepository;
import com.example.karo.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class Test {
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    // Endpoint to create a new parking lot
    @PostMapping("/lot/create")
    public ResponseEntity<Long> createParkingLot(@RequestBody ParkingLot parkingLot) {
        long res = parkingLotRepository.insertParkingLot(parkingLot);
        return ResponseEntity.ok(res);
    }

    // Endpoint to get all parking lots
    @GetMapping("/lot/all")
    public ResponseEntity<List<ParkingLot>> getAllParkingLots() {
        System.out.println("Here");
        List<ParkingLot> parkingLots = parkingLotRepository.findAllLots();
        return ResponseEntity.ok(parkingLots);
    }

    // Endpoint to get a parking lot by ID
    @GetMapping("/lot/{lotId}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Long lotId) {
        ParkingLot parkingLot = parkingLotRepository.findLotById(lotId);
        return ResponseEntity.ok(parkingLot);
    }

    // Endpoint to update a parking lot
    @PutMapping("/lot/capacity")
    public ResponseEntity<Boolean> updateParkingLot(
            @RequestParam Long lotId,
            @RequestParam int capacity) {
        return ResponseEntity.ok(parkingLotRepository.updateParkingLotCapacity(lotId, capacity));
    }

    @PutMapping("/lot/safe")
    public ResponseEntity<Boolean> updateParkingLotSafe(
            @RequestParam Long lotId,
            @RequestParam int safe) {
        return ResponseEntity.ok(parkingLotRepository.updateParkingLotSafe(lotId, safe));
    }

    // Endpoint to delete a parking lot
    @DeleteMapping("/lot/{lotId}")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable Long lotId) {
        parkingLotRepository.deleteLotById(lotId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/spot/create")
    public ResponseEntity<Boolean> createParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        Boolean res = parkingSpotRepository.insertParkingSpot(parkingSpot);
        return ResponseEntity.ok(res);
    }


    // Endpoint to get a parking spot by ID
    @GetMapping("/spot/{spotId}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(@RequestParam Long spotId) {
        ParkingSpot parkingSpot =  parkingSpotRepository.findSpotById(spotId);
        return ResponseEntity.ok(parkingSpot);
    }

    // Endpoint to get parking spots by parking lot ID
    @GetMapping("/spots/{lotId}")
    public ResponseEntity<List<ParkingSpot>> getParkingSpotsByLotId(@RequestParam Long lotId) {
        List<ParkingSpot> parkingSpots = parkingSpotRepository.findSpotsByLotId(lotId);
        return ResponseEntity.ok(parkingSpots);
    }

    // Endpoint to update a parking spot
    @PutMapping("/spot/type")
    public ResponseEntity<Boolean> updateParkingSpot(
            @RequestParam Long spotId,
            @RequestParam String type) {
        return ResponseEntity.ok(parkingSpotRepository.updateSpotType(spotId, type));
    }

    // Endpoint to delete a parking spot
    @DeleteMapping("/spot/delete/{spotId}")
    public ResponseEntity<Boolean> deleteParkingSpot(@RequestParam Long spotId) {
        boolean res = parkingSpotRepository.deleteSpotById(spotId);
        return ResponseEntity.ok(res);
    }
}

