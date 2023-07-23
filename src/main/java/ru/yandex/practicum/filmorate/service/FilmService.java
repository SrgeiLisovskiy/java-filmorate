package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.vadators.ValidatorFilm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaService mpaService;
    private final UserDbStorage userStorage;
    private final LikeStorage likeStorage;

    public Film addFilm(Film film) {
        ValidatorFilm.validationFilm(film);
        log.info("Фильм добавлен: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        ValidatorFilm.validationFilm(film);
        getFilmByID(film.getId());
        log.info("Фильм обновлен: {}", film);
        return filmStorage.updateFilm(film);
    }


    public Film getFilmByID(long id) {
        return filmStorage.getFilm(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUserByID(userId);
        if (user != null && film != null) {
            likeStorage.addLike(filmId, userId);
            log.info("Пользователь с ID = {} поставил лайк фильму с ID = {}", userId, filmId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getListFilms());
        return popularFilms.stream().sorted((o1, o2) -> Integer.compare(o2.getLike().size(), o1.getLike().size())).limit(count).collect(Collectors.toList());
    }

    public List<Film> getAllPopular(Integer count) {
        return likeStorage.getAllPopular(count);
    }


    public List<Long> getLikes(Long filmId) {
        return likeStorage.getLikes(filmId);
    }

    public void deleteLike(long id, long userId) {
        if (filmStorage.getFilm(id) == null) {
            throw new NotFoundException("Не верный ID фильма =" + id);
        } else if (userStorage.getUserByID(userId) == null) {
            throw new NotFoundException("Не верный ID пользователя =" + userId);
        }
        log.info("Лайк пользователя с ID= {} удалён с фильма с ID= {}", userId, id);
        likeStorage.deleteLike(id, userId);
    }

    public List<Film> getListFilms() {
        return filmStorage.getListFilms();
    }


}

