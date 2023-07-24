package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
        filmService.addLike(1L, 1L);
        assertEquals(1, filmService.getFilmByID(1).getLike().size());
    }

    @Test
    void getFilmByID() {
        assertThat(filmService.getListFilms()).isNotNull();
        Film film = Film.builder().name("Test").description("Test film").duration(20).releaseDate(LocalDate.now()).build();
        filmService.addFilm(film);
        assertEquals(film, filmService.getFilmByID(1));
    }

//    @Test
//    void deleteLike() {
//        Film film = Film.builder().name("Test").description("Test film").duration(20).releaseDate(LocalDate.now()).build();
//        filmService.addFilm(film);
//        User user = User.builder().name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
//        userService.addUser(user);
//        assertThat(filmService.getFilmByID(1).getLike()).isNotNull();
//        filmService.userAddLike(1, 1);
//        assertEquals(1, filmService.getFilmByID(1).getLike().size());
//
//        assertThat(filmService.getFilmByID(1).getLike()).isNotNull();
//    }

    @Test
    void getPopularFilms() {
        for (int i = 1; i < 13; i++) {
            Film film = Film.builder().name("Test" + i).description("Test film").duration(20).releaseDate(LocalDate.now()).build();
            filmService.addFilm(film);
            User user = User.builder().name("TestName" + i).birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
            userService.addUser(user);
        }
        filmService.addLike(1L, 1L);
        filmService.addLike(1L, 2L);
        filmService.addLike(1L, 3L);
        filmService.addLike(1L, 4L);
        filmService.addLike(1L, 5L);
        filmService.addLike(1L, 6L);
        filmService.addLike(2L, 6L);
        filmService.addLike(2L, 5L);
        filmService.addLike(2L, 4L);
        filmService.addLike(3L, 1L);
        filmService.addLike(4L, 1L);
        assertEquals(filmService.getPopularFilms(5).get(0).getId(), 1);
        assertEquals(filmService.getPopularFilms(5).get(3).getId(), 4);
    }

}