package ru.yandex.practicum.javafilmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Genre;
import ru.yandex.practicum.javafilmorate.storage.GenreDao;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GenreService {
    private final GenreDao genreStorage;

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        log.info("Search for genre by id");
        Optional<Genre> genre = genreStorage.getGenreById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Genre not found");
        }
        return genre.get();
    }

    public List<Genre> getGenres() {
        log.info("Search for all genres");
        return genreStorage.getAllGenres();
    }
}