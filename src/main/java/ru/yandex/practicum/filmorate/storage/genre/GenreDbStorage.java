package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcOperations jdbcTemplate;


    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }


    @Override
    public Genre getByID(Long id) {
        Genre genre;
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Жанр c введенным id  не найден");
        }
        return genre;
    }


    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }


}
