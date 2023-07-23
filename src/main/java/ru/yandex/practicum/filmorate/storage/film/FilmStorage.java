package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
}
