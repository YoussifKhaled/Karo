package com.example.karo.repositories;

import com.example.karo.models.entities.Driver;
import com.example.karo.models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private static final String SQL_FIND_USER_BY_EMAIL = """
        SELECT *
        FROM user
        WHERE email = ?;
        """;

    private static final String SQL_INSERT_USER = """
    INSERT INTO user
            	(user_id, name, email, password_hash,role)
            VALUES
            	(?, ?, ?, ?, ?);
    """;

    private static final String SQL_DELETE_USER_BY_ID = """
        DELETE FROM user
        WHERE user_id = ?;
        """;
    private static final String SQL_FIND_DRIVER_BY_ID = """
        SELECT *
        FROM driver
        WHERE user_id = ?;
        """;
    private static final String SQL_ADD_BALANCE = """
            UPDATE driver
            SET balance = balance + ?
            WHERE user_id = ?;
            """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User insertUser(User user) {
        if (user == null)
            throw new IllegalArgumentException("user is null");

        jdbcTemplate.update(
            SQL_INSERT_USER,
            user.getUserId(),
            user.getName(),
            user.getEmail(),
            user.getPasswordHash(),
            user.getRole());
        return findUserByEmail(user.getEmail());
    }

    public User findUserByEmail(String email) {
        return jdbcTemplate.queryForObject(SQL_FIND_USER_BY_EMAIL, new Object[]{email}, userRowMapper);
    }

    public boolean deleteUserById(Long userId) {
        return jdbcTemplate.update(SQL_DELETE_USER_BY_ID, userId) == 1;
    }

    public Driver findDriverById(long userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_DRIVER_BY_ID, new Object[]{userId},
                (rs, rowNum) -> Driver.builder()
                        .userId(rs.getLong("user_id"))
                        .balance(rs.getInt("balance"))
                        .licensePlateNumber(rs.getString("license_plate_number"))
                        .build());
    }

    public void addBalance(long userId, int balance) {
        jdbcTemplate.update(SQL_ADD_BALANCE, balance, userId);
    }

    private final RowMapper<User> userRowMapper = (((rs, rowNum) ->
        User.builder()
            .userId(rs.getLong("user_id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .role(rs.getString("role"))
            .passwordHash(rs.getString("password_hash"))
            .build()
    ));
}
