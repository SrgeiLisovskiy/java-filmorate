package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.vadators.ValidatorFilm;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final GenreStorage genreStorage;
    private final MpaService mpaService;
    private final JdbcOperations jdbcTemplate;
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Film addFilm(Film film) {
        ValidatorFilm.validationFilm(film);
        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, mpa_id) VALUES (:name, :description, :releaseDate, :duration, :mpa_id);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("name", film.getName());
        map.addValue("description", film.getDescription());
        map.addValue("releaseDate", film.getReleaseDate());
        map.addValue("duration", film.getDuration());
        map.addValue("mpa_id", film.getMpa().getId());
        jdbcOperations.update(sqlQuery, map, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        genreStorage.addGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(@NotNull Film film) {
        String sqlQuery = "UPDATE films " +
                "SET name = ?, description = ?, releaseDate = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?;";
        jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (film.getGenres() != null) {
            Collection<Genre> genres = film.getGenres().stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toList());
            film.setGenres(new LinkedHashSet<>(genres));
            for (Genre genre : film.getGenres()) {
                genre.setName(genreStorage.getByID(genre.getId()).getName());
            }
        }
        genreStorage.delete(film);
        genreStorage.addGenre(film);
        return film;
    }

    @Override
    public List<Film> getListFilms() {
        String sqlQuery = "select * from films";
        log.info("Загружаю список пользователей");
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilms);
        return jdbcTemplate.query(sqlQuery, this::makeFilms);
    }


    @Override
    public Film getFilm(Long id) {
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeFilms, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Фильм c введенным ID не найден");
        }
    }

    @Override
    public void deleteFilm(Long id) {
        String sqlQuery =
                "DELETE " +
                        "FROM users " +
                        "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }


    private Film makeFilms(ResultSet rs, int rowNum) throws SQLException {
        Set<Genre> genres = new HashSet<>(genreStorage.getFilmGenres(rs.getLong("id")));
        return Film.builder().id(rs.getLong("id")).name(rs.getString("name")).
                description(rs.getString("description")).releaseDate(rs.getDate("releaseDate").toLocalDate()).duration(rs.getInt("duration")).
                mpa(mpaService.getByID(rs.getLong("mpa_id"))).genres(genres).build();

    }
}