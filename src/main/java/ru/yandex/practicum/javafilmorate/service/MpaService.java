package ru.yandex.practicum.javafilmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Mpa;
import ru.yandex.practicum.javafilmorate.storage.MpaDao;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final MpaDao mpaStorage;

    public Mpa getMpaById(Integer id) {
        log.info("Search for Mpa by ID");
        Optional<Mpa> mpa = mpaStorage.getMpaById(id);
        if (mpa.isEmpty())
            throw new NotFoundException("Mpa not found");
        return mpa.get();
    }

    public List<Mpa> getAllMpa() {
        log.info("Search for all Mpa");
        return mpaStorage.getAllMpa();
    }
}
