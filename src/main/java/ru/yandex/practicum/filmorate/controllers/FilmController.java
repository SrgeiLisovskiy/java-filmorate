package ru.yandex.practicum.filmorate.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.debug("Получен запрос GET /films");
        return filmService.getListFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос POST /films");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос PUT /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void userAddLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
        log.debug("Получен запрос PUT /films/{id}/like/{userId}");
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable long id) {
        log.debug("Получен запрос GET /films/{id}");
        return filmService.getFilmByID(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
        log.debug("Получен запрос DEL /films/{id}/like/{userId}");
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.debug("Получен запрос GET /films/popular");
        return filmService.getAllPopular(count);
    }


}
