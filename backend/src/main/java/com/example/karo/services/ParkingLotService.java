package com.example.karo.services;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.repositories.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    public long addParkingLot(ParkingLot parkingLot) {
        if (parkingLot == null)
            throw new IllegalArgumentException("Parking Lot cannot be null");

        return parkingLotRepository.insertParkingLot(parkingLot);
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
