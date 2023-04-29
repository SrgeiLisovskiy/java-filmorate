package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public User addUser(User user) {
        log.info("Пользователь добавлен: {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Пользователь {} обновлен:", user);
        return userStorage.updateUser(user);
    }

    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    public User getUserByID(long id) {
        if (userStorage.getUsers().containsKey(id)) {
            return userStorage.getUsers().get(id);
        } else
            throw new UserNotFoundException("Не верный ID");
    }

    public List<User> getFriendUserId(long id) {
        if (userStorage.getUsers().containsKey(id)) {
            List<User> friendUser = new ArrayList<>();
            userStorage.getUsers().get(id).getFriends().stream().forEach(h -> friendUser.add(userStorage.getUsers().get(h)));
            return friendUser;
        } else
            throw new UserNotFoundException("Проверти введенный ID");
    }

    public void addFriends(long id, long friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Введен не верный ID=" + id);
        } else if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Введен не верный ID=" + friendId);
        }
        userStorage.getUsers().get(id).getFriends().add(friendId);
            userStorage.getUsers().get(friendId).getFriends().add(id);
            log.info("Пользователи с ID {} и {} теперь друзья", id, friendId);
    }

    public void deleteFriends(long id, long friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Введен не верный ID=" + id);
        } else if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Введен не верный ID=" + friendId);
        }
            userStorage.getUsers().get(id).getFriends().remove(friendId);
            userStorage.getUsers().get(friendId).getFriends().remove(id);
    }

    public List<User> commonFriend(long id, long friendId) {
        List<User> commonFriend = new ArrayList<>();
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Введен не верный ID=" + id);
        } else if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Введен не верный ID=" + friendId);
        }
            Set<Long> usersId = userStorage.getUsers().get(id).getFriends();
            Set<Long> usersFriendId = userStorage.getUsers().get(friendId).getFriends();
            usersId.forEach(h -> {
                        if (usersFriendId.contains(h)) {
                            commonFriend.add(userStorage.getUsers().get(h));
                        }
                    }
            );
        return commonFriend;
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
}
