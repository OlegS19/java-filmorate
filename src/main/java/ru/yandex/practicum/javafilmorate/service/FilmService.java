package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.*;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmStorage;
    private final UserDao userStorage;
    private final MpaDao daoStorage;
    private final GenreDao genreStorage;
    private final LikeDao likeStorage;

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        genreStorage.createFilmGenre(film);
        log.info("Create a film with id = {} ", film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        genreStorage.updateFilmGenre(film);
        filmStorage.updateFilm(film);
        log.info("Update the film with id = {} ", film.getId());
        return film;
    }

    public List<Film> getAllFilms() {
        log.info("GET {} films", filmStorage.getAllFilms().size());
        List<Film> films = filmStorage.getAllFilms();
        genreStorage.loadGenres(films);
        return films;
    }

    public Film getFilmById(int id) {
        Optional<Film> film = filmStorage.getFilmById(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Film id: " + id + " does not exist...");
        }
        genreStorage.loadGenres(List.of(film.get()));
        return film.get();
    }


    public void addLikes(int filmId, int userId) {
        likeStorage.createLike(filmId, userId);
        log.info("Film id: {} like from user: {} ", filmId, userId);
    }

    public Film removeLikes(int filmId, int userId) {
        if (filmId < 0 || userId < 0) {
            throw new NotFoundException("Negative value is not allowed");
        }
        Film film = getFilmById(filmId);
        likeStorage.deleteLike(filmId, userId);
        log.info("The user with id = {} remove a like from the film id = {}", userId, filmId);
        return film;
    }

    public List<Film> favoritesFilms(Integer number) {
        return filmStorage.getFavoritesFilms(number);
    }
}
