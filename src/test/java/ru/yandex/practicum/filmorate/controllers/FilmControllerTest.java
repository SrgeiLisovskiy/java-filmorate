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
    void addFilmNoName() {
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
    void addFilmNoNegativeDuration() {
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
    void addFilmReleaseDate() {
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
    void addFilmDescriptionMaxSize200() {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.now()).description("1".repeat(201)).duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }


    @Test
    void updateFilmNoName() {
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
    void updateFilmNoNegativeDuration() {
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
    void updateFilmReleaseDate() {
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
    void updateFilmDescriptionMaxSize200() {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.films.size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.now()).description("1".repeat(201)).duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.films.size());
    }

    @Test
    void getFilms() {
        controller.films.clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.films);
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
    }

}