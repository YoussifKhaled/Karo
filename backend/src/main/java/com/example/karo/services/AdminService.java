package com.example.karo.services;

import com.example.karo.models.entities.TopLot;
import com.example.karo.models.entities.TopUser;
import com.example.karo.models.entities.User;
import com.example.karo.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public long addManager(User user) {
        String ph = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(ph);

        return adminRepository.insertManager(user);
    }

    public List<TopUser> getTopUsersByReservations() {
        return adminRepository.getTopUsersByReservations();
    }

    public List<TopLot> getTopLotsByRevenue() {
        return adminRepository.getTopLotsByRevenue();
    }
}
