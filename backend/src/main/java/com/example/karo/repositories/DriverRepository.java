package com.example.karo.repositories;

import com.example.karo.models.entities.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DriverRepository {

    private static final String SQL_INSERT_DRIVER = """
        INSERT INTO driver (user_id, license_plate_number, balance)
                VALUES (?, ?, ?);
        """;

    private static final String SQL_FIND_DRIVER_BY_LICENSE_PLATE_NUMBER = """
        SELECT u.* , d.license_plate_number, d.balance
        FROM user u join driver d ON u.user_id = d.user_id
        WHERE d.license_plate_number = ?
        """;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Driver insertDriver(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver is null");
        }

        jdbcTemplate.update(
            SQL_INSERT_DRIVER,
            driver.getUserId(),
            driver.getLicensePlateNumber(),
            driver.getBalance()
        );

        return findDriverByLicensePlateNumber(driver.getLicensePlateNumber());
    }

    public Driver findDriverByLicensePlateNumber(String licensePlateNumber) {
        return jdbcTemplate.queryForObject(SQL_FIND_DRIVER_BY_LICENSE_PLATE_NUMBER,
            new Object[]{licensePlateNumber}, driverRowMapper);
    }

    private final RowMapper<Driver> driverRowMapper = (rs, rowNum) ->
        Driver.builder()
            .userId(rs.getLong("user_id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .role(rs.getString("role"))
            .passwordHash(rs.getString("password_hash"))
            .licensePlateNumber(rs.getString("license_plate_number"))
            .balance(rs.getInt("balance"))
            .build();
}
