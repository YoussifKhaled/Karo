package com.example.karo.controllers;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.services.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("/lots")
    public ResponseEntity<?> getLotsOwned(@RequestParam("managerId") long managerId) {
        try {
            List<ParkingLot> lots = managerService.getLotsOwnedByManager(managerId);
            return ResponseEntity.ok(lots);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/lots/utilization")
    public ResponseEntity<?> getLotUtilization(@RequestParam("managerId") long managerId) {
        try {
            List<Map<String, Object>> utilization = managerService.getLotUtilization(managerId);
            return ResponseEntity.ok(utilization);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/lots/{lotId}/spots-status")
    public ResponseEntity<?> getSpotStatusReport(@PathVariable("lotId") Long lotId) {
        try {
            List<Map<String, Object>> spotStatus = managerService.getSpotStatusReport(lotId);
            return ResponseEntity.ok(spotStatus);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
