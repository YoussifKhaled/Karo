package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingSpot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingSpotRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insertParkingSpot(ParkingSpot parkingSpot) {
        return true;
    }

    public ParkingSpot findSpotById(long spotId) {
        return null;
    }

    public List<ParkingSpot> findSpotsByLotId(long lotId) {
        return null;
    }

    public boolean updateSpotType(long spotId, String type) {
        return true;
    }

    public boolean updateSpotStatus(long spotId, String status) {
        return true;
    }
    public boolean updateSensorStatus(long spotId, String status) {
        return true;
    }
    public boolean updateSpotPrice(long spotId, Double price) {
        return true;
    }

    public boolean deleteSpotById(long spotId) {
        return true;
    }
}
