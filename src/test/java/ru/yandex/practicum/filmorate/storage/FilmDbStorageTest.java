package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class FilmDbStorageTest {
    private final FilmStorage filmStorage;

    private Film film;
    private Film film2;


    @BeforeEach
    public void beforeEach() {
        film = Film.builder()
                .name("film").
                description("good film").
                releaseDate(LocalDate.parse("2020-02-02"))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();

        film2 = Film.builder()
                .name("filmUpdate").
                description("good film").
                releaseDate(LocalDate.parse("2020-02-02"))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
    }

    @AfterEach
    void afterEach() {
        List<Film> films = new ArrayList<>(filmStorage.getListFilms());
        films.stream().forEach(f -> filmStorage.deleteFilm(f.getId()));
    }

    @Test
    void addFilm() {
        assertThat(filmStorage.getListFilms()).isNotNull();
        filmStorage.addFilm(film);
        assertEquals(film, filmStorage.getFilm(film.getId()));
    }

    @Test
    void updateFilm() {
        assertThat(filmStorage.getListFilms()).isNotNull();
        filmStorage.addFilm(film);
        assertEquals(film, filmStorage.getFilm(film.getId()));
        film2.setId(film.getId());
        filmStorage.updateFilm(film2);
        assertEquals(film2, filmStorage.getFilm(film.getId()));
    }

    @Test
    void getListFilms() {
        List<Film> filmsTest = new ArrayList<>();
        assertThat(filmStorage.getListFilms()).isNotNull();
        filmStorage.addFilm(film);
        filmsTest.add(film);
        filmStorage.addFilm(film2);
        filmsTest.add(film2);
        assertEquals(filmsTest, filmStorage.getListFilms());
    }

    @Test
    void getFilm() {
        assertThat(filmStorage.getListFilms()).isNotNull();
        filmStorage.addFilm(film);
        filmStorage.addFilm(film2);
        assertEquals(film, filmStorage.getFilm(film.getId()));

    }
}
