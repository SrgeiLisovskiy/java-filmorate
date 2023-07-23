package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.HashSet;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcOperations jdbcTemplate;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaService mpaService;
    private final GenreService genreService;

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
                        mpaService.getByID(rs.getLong("mpa_id")),
                        genreService.getFilmGenres(rs.getLong("id"))),
                count);
    }

    @Override
    public List<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), filmId);
    }
}

