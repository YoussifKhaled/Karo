package com.example.karo.services;

import com.example.karo.models.entities.ParkingSpot;
import com.example.karo.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {
    @Autowired
    ParkingSpotRepository parkingSpotRepository;

    public boolean addParkingSpot(ParkingSpot parkingSpot) {
        if (parkingSpot == null)
            throw new IllegalArgumentException("Parking Spot is null");

        return parkingSpotRepository.insertParkingSpot(parkingSpot);
    }

    public ParkingSpot getParkingSpotById(long spotId) {
        return parkingSpotRepository.findSpotById(spotId);
    }

    public List<ParkingSpot> getParkingSpotsByLotId(long lotId, int limit, int offset) {
        return parkingSpotRepository.findSpotsByLotId(lotId, limit, offset);
    }

    public boolean updateParkingSpotType(long spotId, String type) {
        if (type == null || type.isEmpty())
            throw new IllegalArgumentException("Type is invalid");

        return parkingSpotRepository.updateSpotType(spotId, type);
    }

    public boolean updateParkingSpotStatus(long spotId, String status) {
        if (status == null || status.isEmpty())
            throw new IllegalArgumentException("Status is invalid");

        return parkingSpotRepository.updateSpotStatus(spotId, status);
    }

    public boolean updateSensorStatus(long spotId, String status) {
        if (status == null || status.isEmpty())
            throw new IllegalArgumentException("Sensor status is invalid");

        return parkingSpotRepository.updateSensorStatus(spotId, status);
    }

    public boolean updateParkingSpotPrice(long spotId, Double price) {
        if (price == null || price < 0)
            throw new IllegalArgumentException("Price is invalid");

        return parkingSpotRepository.updateSpotPrice(spotId, price);
    }

    public boolean deleteParkingSpot(long spotId) {
        return parkingSpotRepository.deleteSpotById(spotId);
    }
}
