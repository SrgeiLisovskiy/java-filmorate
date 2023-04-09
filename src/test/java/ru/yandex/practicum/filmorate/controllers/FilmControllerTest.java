package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {

    @Autowired
    private InMemoryFilmStorage controller;


    @Test
    void addFilmNoName() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().name("").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }


    @Test
    void addFilmNoNegativeDuration() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.now()).description("Test film").duration(-50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }

    @Test
    void addFilmReleaseDate() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.of(1894, 11, 21)).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }

    @Test
    void addFilmDescriptionMaxSize200() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().name("test2").releaseDate(LocalDate.now()).description("1".repeat(201)).duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }


    @Test
    void updateFilmNoName() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().id(film.getId()).name("").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }


    @Test
    void updateFilmNoNegativeDuration() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.now()).description("Test film").duration(-50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }

    @Test
    void updateFilmReleaseDate() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.of(1894, 11, 21)).description("Test film").duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }

    @Test
    void updateFilmDescriptionMaxSize200() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
        Film film2 = Film.builder().id(film.getId()).name("test2").releaseDate(LocalDate.now()).description("1".repeat(201)).duration(50).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateFilm(film2);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getFilms().size());
    }

    @Test
    void getFilms() {
        controller.getFilms().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getFilms());
        Film film = Film.builder().name("test").releaseDate(LocalDate.now()).description("Test film").duration(50).build();
        controller.addFilm(film);
        assertEquals(1, controller.getFilms().size());
    }

}