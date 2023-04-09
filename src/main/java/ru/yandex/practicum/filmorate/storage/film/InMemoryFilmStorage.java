package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private long id = 0;
    private HashMap<Long, Film> films = new HashMap<>();
    public static final LocalDate VALID_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public List<Film> getListFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        if (validationFilm(film)) {
            film.setId(++id);
            log.info("Фильм добавлен: {}", film);
            films.put(film.getId(), film);
        }
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            if (validationFilm(film)) {
                Film film1 = films.get(film.getId());
                film1.setDuration(film.getDuration());
                film1.setDescription(film.getDescription());
                film1.setReleaseDate(film.getReleaseDate());
                film1.setName(film.getName());
            }
        } else {
            log.error("При обновлении фильма: фильм с данным ID не найден: {}", film);
            throw new ValidationException("Фильм с данным ID не найден");
        }
        log.info("Фильм с ID = {} обновлен", film.getId());
        return films.get(film.getId());
    }

    private boolean validationFilm(Film film) {
        if (film.getDuration() < 0) {
            log.error("Отрицательная продолжительность фильма: {}", film);
            throw new ValidationException("Отрицательная продолжительность фильма");
        }
        if (film.getReleaseDate().isBefore(VALID_RELEASE_DATE)) {
            log.error("Дата релиза раньше 28 декабря 1895 года:{} ", film);
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года");
        }
        if (film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым: {}", film);
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания превысила 200 символов: {}", film);
            throw new ValidationException("максимальная длина описания — 200 символов");
        }

        return true;
    }

    @Override
    public void setFilms(HashMap<Long, Film> films) {
        this.films = films;
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }
}
