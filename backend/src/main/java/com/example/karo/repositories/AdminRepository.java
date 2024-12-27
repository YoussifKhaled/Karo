package com.example.karo.repositories;

import com.example.karo.models.entities.TopLot;
import com.example.karo.models.entities.TopUser;
import com.example.karo.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            ORDER BY reservation_count DESC;
        """;
    private static final String SQL_TOP_LOT_BY_REVENUE = """
            SELECT *
            FROM parking_lot
            ORDER BY safe DESC;
        """;
    private static final String SQL_ALL_MANAGERS = """
            SELECT *
            FROM user
            WHERE role = 'manager';
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public long insertManager(User user) {
        if (user == null)
            throw new IllegalArgumentException("User is null");

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int count = jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(SQL_INSERT_MANAGER, new String[]{"user_id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            return ps;
        }, keyHolder);

        if (count == 1)
            return keyHolder.getKey().longValue();

        throw new RuntimeException("Failed to insert manager");
    }

    public List<TopUser> getTopUsersByReservations() {

        return jdbcTemplate.query(SQL_TOP_USERS_BY_RESERVATIONS, (rs, rowNum) ->
                new TopUser(
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getInt("reservation_count")
                )
        );
    }

    public List<TopLot> getTopLotsByRevenue() {

        return jdbcTemplate.query(SQL_TOP_LOT_BY_REVENUE, (rs, rowNum) ->
                new TopLot(
                        rs.getLong("lot_id"),
                        rs.getBigDecimal("safe")
                )
        );
    }

    public List<User> getAllManagers() {
        return jdbcTemplate.query(SQL_ALL_MANAGERS, userRowMapper);
    }

    private final RowMapper<User> userRowMapper = (((rs, rowNum) ->
            User.builder()
                    .userId(rs.getLong("user_id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .role(rs.getString("role"))
                    .build()
    ));
}
