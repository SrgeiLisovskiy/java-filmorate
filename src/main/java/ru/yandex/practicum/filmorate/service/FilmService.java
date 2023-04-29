package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    public Film addFilm(Film film) {
        log.info("Фильм добавлен: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Фильм с ID = {} обновлен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public void userAddLike(long id, long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmNotFoundException("Не верный ID фильма =" + id);
        } else if (userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Не верный ID пользователя =" + userId);
        }
            filmStorage.getFilms().get(id).getLike().add(userId);
            log.info("Пользователь с ID= {} поставил лайк фильму c ID=", userId, id);


    }

    public Film getFilmByID(long id) {
        if (filmStorage.getFilms().containsKey(id)) {
            log.info("Получили фильм с ID: {}", id);
            return filmStorage.getFilms().get(id);
        } else
            throw new FilmNotFoundException("Не верный ID");
    }

    public void deleteLike(long id, long userId) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmNotFoundException("Не верный ID фильма =" + id);
        } else if (userStorage.getUsers().containsKey(userId)) {
            throw new UserNotFoundException("Не верный ID пользователя =" + userId);
        }
            filmStorage.getFilms().get(id).getLike().remove(userId);
        log.info("Лайк пользователя с ID= {} удалён с фильма с ID= {}",userId,id);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>(filmStorage.getListFilms());
        return popularFilms.stream().sorted((o1, o2) -> Integer.compare(o2.getLike().size(), o1.getLike().size())).limit(count).collect(Collectors.toList());
    }

}

