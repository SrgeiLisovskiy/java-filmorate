package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcOperations jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa getByID(Long id) throws NotFoundException {
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
