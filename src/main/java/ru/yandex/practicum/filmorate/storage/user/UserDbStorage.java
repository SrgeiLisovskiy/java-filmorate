package ru.yandex.practicum.filmorate.storage.user;

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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.vadators.ValidatorUser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcOperations jdbcTemplate;
    private final NamedParameterJdbcOperations jdbcOperations;


    @Override
    public User addUser(User user) {
        ValidatorUser.validationUsers(user);
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES(:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", user.getEmail());
        map.addValue("login", user.getLogin());
        map.addValue("name", user.getName());
        map.addValue("birthday", user.getBirthday());
        jdbcOperations.update(sqlQuery, map, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (getUserByID(user.getId()) != null) {
            String sqlQuery = "UPDATE users " +
                    "SET email=?, login=?, name=?, birthday=? " +
                    "WHERE id=?";
            jdbcTemplate.update(
                    sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );
        } else
            throw new NotFoundException("Пользователь не найден");
        return user;
    }

    @Override
    public List<User> getListUsers() {
        String salQuery = "SELECT *" +
                " FROM users ";
        log.info("Загружаю список пользователей");
        return jdbcTemplate.query(salQuery, this::makeUsers);
    }


    @Override
    public User getUserByID(Long id) throws NotFoundException {
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::makeUsers, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("Пользователь c введенным ID не найден");
        }
    }


    @Override
    public User deleteUser(Long id) {
        User user = getUserByID(id);
        String sqlQuery =
                "DELETE " +
                        "FROM users " +
                        "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
        return user;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        int statusId;
        User user = getUserByID(userId);
        User friend = getUserByID(friendId);
        if ((user != null && friend != null)) {
            if (getFriendsByUserId(friendId).contains(user)) {
                statusId = 2;
                String sql = "INSERT INTO friendship (user_id,friend_id,status_id)" +
                        "VALUES (?, ?, ?)";
                jdbcTemplate.update(sql, userId, friendId, statusId);
                String sqlQuery = "UPDATE friendship SET status_id = ?" +
                        "WHERE user_id = ? AND friend_id = ?";
                log.info("Пользователи с ID {} и {} теперь друзья", userId, friendId);
                jdbcTemplate.update(sqlQuery, statusId, friendId, userId);
            } else {
                statusId = 1;
                String sqlQuery = "INSERT INTO friendship (user_id, friend_id,status_id)" + " VALUES (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, userId, friendId, statusId);
            }
        }
        return getUserByID(userId);
    }

    @Override
    public User deleteFriend(Long userId, Long friendId) {
        User user = getUserByID(userId);
        User friend = getUserByID(friendId);
        if (friend != null && friend != null) {
            if (getFriendsByUserId(userId).contains(friend)) {
                String sqlQuery =
                        "DELETE " +
                                "FROM friendship " +
                                "WHERE user_id = ? AND friend_id = ?";
                jdbcTemplate.update(sqlQuery, userId, friendId);
            }
            if (getFriendsByUserId(friendId).contains(user)) {
                String sqlQuery = "UPDATE friendship SET status_id = ?" +
                        " WHERE user_id = ? AND friend_id = ?";
                jdbcTemplate.update(sqlQuery, 1, friendId, userId);
            }
        }
        return getUserByID(userId);
    }

    @Override
    public List<User> getFriendsByUserId(Long id) {
        String sqlQuery =
                "SELECT id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE id " +
                        "IN(SELECT friend_id " +
                        "FROM friendship " +
                        "WHERE user_id=?)";
        return jdbcTemplate.query(sqlQuery, this::makeUsers, id);
    }


    private User makeUsers(ResultSet rs, int rowNum) throws SQLException {
        return User.builder().id(rs.getLong("id")).email(rs.getString("email")).
                login(rs.getString("login")).name(rs.getString("name")).
                birthday(rs.getDate("birthday").toLocalDate()).build();
    }
}
