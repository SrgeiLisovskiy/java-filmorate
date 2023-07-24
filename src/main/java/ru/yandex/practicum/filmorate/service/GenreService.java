package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> getAllGenres() {
        log.info("Получен список всех жанров");
        return genreStorage.getAllGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList());
    }

    public Genre getByID(Long id) {
        if (genreStorage.getByID(id) != null) {
            log.info("Получен жанр с ID = {}",id);
            return genreStorage.getByID(id);
        } else throw new NotFoundException("Жанр с id = ? не найден");
    }


}
