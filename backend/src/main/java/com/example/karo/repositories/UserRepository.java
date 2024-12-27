package com.example.karo.repositories;

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
            	(user_id, name, date_of_birth, email, password_hash,role)
            VALUES
            	(?, ?, ?, ?, ?, ?);
    """;

    private static final String SQL_DELETE_USER_BY_ID = """
        DELETE FROM user
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
            user.getDateOfBirth(),
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

    private final RowMapper<User> userRowMapper = (((rs, rowNum) ->
        User.builder()
            .userId(rs.getLong("user_id"))
            .name(rs.getString("name"))
            .dateOfBirth(rs.getDate("date_of_birth"))
            .email(rs.getString("email"))
            .role(rs.getString("role"))
            .passwordHash(rs.getString("password_hash"))
            .build()
    ));
}
