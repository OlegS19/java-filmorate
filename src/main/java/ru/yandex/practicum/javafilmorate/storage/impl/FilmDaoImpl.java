package ru.yandex.practicum.javafilmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.FilmDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name,description,release_date,duration,rating_id) VALUES (?,?,?,?,?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).intValue());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id WHERE f.id = ?";
        List<Film> film = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (film.size() == 0)
            return Optional.empty();
        return Optional.of(film.get(0));
    }

    @Override
    public List<Film> getFavoritesFilms(int id) {
        String sqlQuery = "SELECT f.*, rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?," +
                "description = ?," +
                "release_date = ?," +
                "duration = ?," +
                "rating_id = ?" +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        if (count == 0)
            throw new NotFoundException("Film not found");
        return film;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(rs.getInt("rating_id"), rs.getString("mpa_name"));
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                new LinkedHashSet<>()
        );
    }

}
