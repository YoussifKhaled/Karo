package com.example.karo.repositories;

import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {
    private static final String SQL_INSERT_MANAGER = """
            INSERT INTO user
                (name, email, password_hash, role)
            VALUES
                (?, ?, ?, 'manager')
        """;
    private static final String SQL_TOP_USERS_BY_RESERVATIONS = """
            SELECT u.user_id, u.name, COUNT(r.reservation_id) AS reservation_count
            FROM user u
            JOIN driver d ON u.user_id = d.user_id
            JOIN reservation r ON d.user_id = r.driver_id
            GROUP BY u.user_id, u.name
            ORDER BY reservation_count DESC
            LIMIT 10
        """;
    private static final String SQL_TOP_LOT_BY_REVENUE = """
            SELECT *
            FROM parking_lot
            ORDER BY safe
            LIMIT 10;
        """;

    
}
