package com.example.karo.services;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.repositories.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public List<ParkingLot> getLotsOwnedByManager(long managerId) {
        return managerRepository.findLotsByManagerId(managerId);
    }

    public List<Map<String, Object>> getLotUtilization(long managerId) {
        return managerRepository.getLotUtilization(managerId);
    }

    public List<Map<String, Object>> getSpotStatusReport(Long lotId) {
        return managerRepository.getSpotStatusReport(lotId);
    }
}
