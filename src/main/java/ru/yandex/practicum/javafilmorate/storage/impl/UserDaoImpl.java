package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.User;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (name,login,email,birthday) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, user.getName());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getEmail());
            ps.setDate(4, (java.sql.Date.valueOf(user.getBirthday())));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Create users");
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        log.info("Get user with id = {}", id);
        List<User> userList = jdbcTemplate.query(sqlQuery, this::makeUser, id);
        if (userList.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(userList.get(0));
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "name = ?," +
                "login = ?," +
                "email = ?," +
                "birthday = ?" +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sqlQuery, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        if (count == 0)
            throw new NotFoundException("User not found");
        log.info("Update user with id = {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        log.info("Get All user");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        log.info("Make users");
        return new User(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}