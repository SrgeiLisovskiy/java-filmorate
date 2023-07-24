package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public interface FilmStorage {
    Film addFilm(@NotNull @Valid Film film);

    Film updateFilm(@NotNull @Valid Film film);

    List<Film> getListFilms();

    void deleteFilm(Long id);

    Film getFilm(Long id);

    void deleteGenre(Film film);

    void addGenre(Film film);

    List<Genre> getFilmGenres(Long filmId);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getAllPopular(Integer count);

    List<Long> getLikes(Long filmId);
}
