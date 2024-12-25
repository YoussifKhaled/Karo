package com.example.karo.repositories;

import com.example.karo.models.entities.ParkingLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParkingLotRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean insertParkingLot(ParkingLot parkingLot) {
        return true;
    }

    public ParkingLot findLotById(Long lotId) {
        return null;
    }

    public List<ParkingLot> findAllLots() {
        return null;
    }

    public Boolean updateParkingLot(ParkingLot parkingLot) {
        return null;
    }

    public Boolean deleteLotById(Long lotId) {
        return null;
    }
}
