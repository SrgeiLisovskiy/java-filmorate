package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private long id = 0;
    private HashMap<Long, User> users = new HashMap<>();

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        if (validationUsers(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Пользователь добавлен: {}", user);
        }
        return users.get(user.getId());
    }

    @Override
    public User updateUser(@Valid User user) {
        if (users.containsKey(user.getId())) {
            if (validationUsers(user)) {
                User user1 = users.get(user.getId());
                user1.setBirthday(user.getBirthday());
                user1.setLogin(user.getLogin());
                user1.setEmail(user.getEmail());
                if (user.getName() == null || user.getName().isBlank()) {
                    user1.setName(user.getLogin());
                } else user1.setName(user.getName());
            }
        } else {
            throw new ValidationException("User с данным ID не существует");
        }
        return users.get(user.getId());
    }

    private boolean validationUsers(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ @: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.error("Login содержит пробелы:{}", user);
            throw new ValidationException("Login не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        return true;
    }

    @Override
    public void setUsers(@NotBlank HashMap<Long, User> users) {
        this.users = users;
    }

    @Override
    public HashMap<Long, User> getUsers() {
        return users;
    }
}
