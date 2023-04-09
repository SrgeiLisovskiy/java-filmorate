package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmServiceTest {
    @Autowired
    FilmService filmService;
    @Autowired
    UserService userService;


    @Test
    void userAddLike() {
        Film film = Film.builder().name("Test").description("Test film").duration(20).releaseDate(LocalDate.now()).build();
        filmService.addFilm(film);
        User user = User.builder().name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        assertThat(filmService.getFilmByID(1).getLike()).isNotNull();
        filmService.userAddLike(1, 1);
        assertEquals(1, filmService.getFilmByID(1).getLike().size());
    }

    @Test
    void getFilmByID() {
        InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
        assertThat(inMemoryFilmStorage.getFilms()).isNotNull();
        Film film = Film.builder().name("Test").description("Test film").duration(20).releaseDate(LocalDate.now()).build();
        filmService.addFilm(film);
        assertEquals(film, filmService.getFilmByID(1));
    }

    @Test
    void deleteLike() {
        Film film = Film.builder().name("Test").description("Test film").duration(20).releaseDate(LocalDate.now()).build();
        filmService.addFilm(film);
        User user = User.builder().name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        assertThat(filmService.getFilmByID(1).getLike()).isNotNull();
        filmService.userAddLike(1, 1);
        assertEquals(1, filmService.getFilmByID(1).getLike().size());
        filmService.deleteLike(1, 1);
        assertThat(filmService.getFilmByID(1).getLike()).isNotNull();
    }

    @Test
    void getPopularFilms() {
        for (int i = 1; i < 13; i++) {
            Film film = Film.builder().name("Test" + i).description("Test film").duration(20).releaseDate(LocalDate.now()).build();
            filmService.addFilm(film);
            User user = User.builder().name("TestName" + i).birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
            userService.addUser(user);
        }
        filmService.userAddLike(1, 1);
        filmService.userAddLike(1, 2);
        filmService.userAddLike(1, 3);
        filmService.userAddLike(1, 4);
        filmService.userAddLike(1, 5);
        filmService.userAddLike(1, 6);
        filmService.userAddLike(2, 6);
        filmService.userAddLike(2, 5);
        filmService.userAddLike(2, 4);
        filmService.userAddLike(3, 1);
        filmService.userAddLike(4, 1);
        assertEquals(filmService.getPopularFilms(5).get(0).getId(), 1);
        assertEquals(filmService.getPopularFilms(5).get(3).getId(), 4);
    }

}