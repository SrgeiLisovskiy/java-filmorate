package ru.yandex.practicum.filmorate.vadators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ValidatorFilm {
    public static final LocalDate VALID_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public static void validationFilm(Film film) {
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
    }
}
