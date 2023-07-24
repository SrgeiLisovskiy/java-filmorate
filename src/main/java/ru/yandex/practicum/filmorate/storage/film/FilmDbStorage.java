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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.vadators.ValidatorFilm;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

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
        addGenre(film);
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

    @Override
    public void deleteGenre(Film film) {
        jdbcTemplate.update("DELETE FROM genre_film WHERE film_id = ?", film.getId());
    }


    @Override
    public void addGenre(Film film) {
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)",
                        film.getId(), genre.getId());
            }
        }

    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT * FROM genres AS g" +
                " LEFT JOIN genre_film AS gf  ON g.id = gf.genre_id " +
                " WHERE gf.film_id = ?" +
                "ORDER BY g.id";
        return jdbcTemplate.query(sql, this::makeGenre, filmId
        );
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public List<Film> getAllPopular(Integer count) {
        String sqlQuery = "SELECT id, name, description, releaseDate, duration, mpa_id FROM films" +
                " LEFT JOIN likes ON films.id = likes.film_id" +
                " GROUP BY films.id " +
                "ORDER BY COUNT(likes.user_id) DESC LIMIT ? ";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Film(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDate("releaseDate").toLocalDate(),
                        rs.getInt("duration"),
                        new HashSet<>(getLikes(rs.getLong("id"))),
                        getByMpaID(rs.getLong("mpa_id")),
                        new HashSet<>(getFilmGenres(rs.getLong("id")))),
                count);
    }

    @Override
    public List<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }


    private Film makeFilms(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder().id(rs.getLong("id")).name(rs.getString("name")).
                description(rs.getString("description")).releaseDate(rs.getDate("releaseDate").toLocalDate())
                .genres(new HashSet<>(getFilmGenres(rs.getLong("id")))).mpa(getByMpaID(rs.getLong("mpa_id"))).duration(rs.getInt("duration")).build();

    }

    private Mpa getByMpaID(Long id) throws NotFoundException {
        Mpa mpa;
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        try {
            return mpa = jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Mpa c введенным ID не найден");
        }
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }
}