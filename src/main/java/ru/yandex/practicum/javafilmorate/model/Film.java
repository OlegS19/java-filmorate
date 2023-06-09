package ru.yandex.practicum.javafilmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.javafilmorate.validation.FilmReleaseDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Builder
@AllArgsConstructor
@Data
public class Film {

    private Integer id;
    @NotBlank(message = "Film name can't be blank")
    private String name;
    @Size(max = 200, message = "Film description has to be less than 200 symbols")
    @NotBlank(message = "Film description can't be blank")
    private String description;
    @FilmReleaseDate(message = "The release date has to be before today")
    private LocalDate releaseDate;
    @NotNull(message = "The film duration can't be empty")
    @Positive(message = "The film duration can't be positive")
    private Integer duration;

    @Valid
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = new LinkedHashSet<>();
        }
        genres.add(genre);

    }

}