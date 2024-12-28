package com.example.karo.controllers;

import com.example.karo.models.entities.ParkingLot;
import com.example.karo.services.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.karo.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;
    private final UserService userService;

    public ManagerController(ManagerService managerService, UserService userService) {
        this.managerService = managerService;
        this.userService = userService;
    }

    @GetMapping("/lots")
    public ResponseEntity<?> getLotsOwned() {
        try {
            long managerId = userService.getCurrentUser().getUserId();
            List<ParkingLot> lots = managerService.getLotsOwnedByManager(managerId);
            return ResponseEntity.ok(lots);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/lots/utilization")
    public ResponseEntity<?> getLotUtilization() {
        try {
            long managerId = userService.getCurrentUser().getUserId();
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
