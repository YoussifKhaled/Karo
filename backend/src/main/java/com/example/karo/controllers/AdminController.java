package com.example.karo.controllers;

import com.example.karo.models.entities.TopLot;
import com.example.karo.models.entities.TopUser;
import com.example.karo.models.entities.User;
import com.example.karo.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-manager")
    public ResponseEntity<?> createManager(@RequestBody User user) {
        try {
            return ResponseEntity.ok(adminService.addManager(user));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/top-users")
    public ResponseEntity<?> getTopUsersByReservations() {
        try {
            List<TopUser> topUsers = adminService.getTopUsersByReservations();
            return ResponseEntity.ok(topUsers);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/top-lots")
    public ResponseEntity<?> getTopLotsByRevenue() {
        try {
            List<TopLot> topLots = adminService.getTopLotsByRevenue();
            return ResponseEntity.ok(topLots);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
