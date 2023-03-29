package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private FilmController controller;


    @Test
    void addFilmNoName() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().name("").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }


    @Test
    void addFilmNoNegativeDuration() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.now()).description("Test film").duration(-50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void addFilmReleaseDate() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.of(1894, 11, 21)).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void addFilmDescriptionMaxSize200() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.now()).description("Валидация\n" +
                "Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя. Эти данные должны соответствовать определённым критериям. \n" +
                "Для Film:\n" +
                "название не может быть пустым;\n" +
                "максимальная длина описания — 200 символов;\n" +
                "дата релиза — не раньше 28 декабря 1895 года;\n" +
                "продолжительность фильма должна быть положительной.\n" +
                "Для User:\n" +
                "электронная почта не может быть пустой и должна содержать символ @;\n" +
                "логин не может быть пустым и содержать пробелы;\n" +
                "имя для отображения может быть пустым — в таком случае будет использован логин;\n" +
                "дата рождения не может быть в будущем.").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }


    @Test
    void updateFilmNoName() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().id(film.getId()).name("").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }


    @Test
    void updateFilmNoNegativeDuration() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.now()).description("Test film").duration(-50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void updateFilmReleaseDate() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.of(1894, 11, 21)).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void updateFilmDescriptionMaxSize200() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.now()).description("Валидация\n" +
                "Проверьте данные, которые приходят в запросе на добавление нового фильма или пользователя. Эти данные должны соответствовать определённым критериям. \n" +
                "Для Film:\n" +
                "название не может быть пустым;\n" +
                "максимальная длина описания — 200 символов;\n" +
                "дата релиза — не раньше 28 декабря 1895 года;\n" +
                "продолжительность фильма должна быть положительной.\n" +
                "Для User:\n" +
                "электронная почта не может быть пустой и должна содержать символ @;\n" +
                "логин не может быть пустым и содержать пробелы;\n" +
                "имя для отображения может быть пустым — в таком случае будет использован логин;\n" +
                "дата рождения не может быть в будущем.").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void getFilms() throws ValidationException {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
    }

}