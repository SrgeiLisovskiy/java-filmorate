package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RequestMapping("/genres")
@RestController
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public List<Genre> getAllGenres() {
        log.debug("Получен запрос GET /genres");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre getByID(@PathVariable Long id) {
        log.debug("Получен запрос GET /genres/{id}");
        return genreService.getByID(id);
    }

}
