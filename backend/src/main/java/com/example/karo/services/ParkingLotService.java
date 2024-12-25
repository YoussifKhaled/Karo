package com.example.karo.services;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.models.entities.ParkingSpot;
import com.example.karo.repositories.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private ParkingSpotService parkingSpotService;

    public long addParkingLot(ParkingLot parkingLot) {
        if (parkingLot == null)
            throw new IllegalArgumentException("Parking Lot cannot be null");

        long lotId = parkingLotRepository.insertParkingLot(parkingLot);

        ParkingSpot parkingSpot = ParkingSpot.builder()
                .lotId(lotId)
                .type("regular")
                .spotStatus("available")
                .sensorStatus("active")
                .price(100.0)
                .build();
        for (int spotId = 1; spotId <= parkingLot.getCapacity(); ++spotId) {
            parkingSpot.setSpotId((long) spotId);
            parkingSpot.setSensorId((long) spotId);

            parkingSpotService.addParkingSpot(parkingSpot);
        }

        return lotId;
    }

    public ParkingLot getParkingLotById(long lotId) {
        return parkingLotRepository.findLotById(lotId);
    }

    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAllLots();
    }

    public boolean updateParkingLotCapacity(long lotId, int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity cannot be negative");

        return parkingLotRepository.updateParkingLotCapacity(lotId, capacity);
    }

    public boolean updateParkingLotSafe(long lotId, int safe) {
        return parkingLotRepository.updateParkingLotSafe(lotId, safe);
    }

    public boolean deleteParkingLotById(long lotId) {
        return parkingLotRepository.deleteLotById(lotId);
    }
}
