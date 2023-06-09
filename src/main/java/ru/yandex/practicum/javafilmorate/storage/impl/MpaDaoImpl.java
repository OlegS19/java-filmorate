package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.MpaDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Mpa> getMpaById(int id) {
        String sqlQuery = "SELECT * FROM rating_mpa WHERE id = ?";
        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, this::makeMpa, id);
        if (mpa.size() == 0)
            return Optional.empty();
        return Optional.of(mpa.get(0));
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM rating_mpa";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("id"), rs.getString("name"));
    }
}
