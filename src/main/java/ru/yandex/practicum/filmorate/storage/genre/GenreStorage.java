package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    void addGenre(Film film);

    void delete(Film film);

    List<Genre> getFilmGenres(Long filmId);

    Genre getByID(Long id);
}