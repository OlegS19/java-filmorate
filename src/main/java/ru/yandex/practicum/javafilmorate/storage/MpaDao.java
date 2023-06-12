package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    Optional<Mpa> getMpaById(int id);

    List<Mpa> getAllMpa();
}
