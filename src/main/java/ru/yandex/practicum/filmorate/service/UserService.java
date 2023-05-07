package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;

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
            userStorage.getUsers().get(id).getFriends().forEach((h,g)->friendUser.add(userStorage.getUsers().get(h)));
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
        if (userStorage.getUsers().get(friendId).getFriends().containsKey(id)) {
            Map<Long, FriendshipStatus> friends = userStorage.getUsers().get(id).getFriends();
            friends.put(friendId, FriendshipStatus.CONFIRMED);
            Map<Long, FriendshipStatus> friendFriends = userStorage.getUsers().get(friendId).getFriends();
            friendFriends.put(id, FriendshipStatus.CONFIRMED);

        } else {
            userStorage.getUsers().get(id).getFriends().put(friendId, FriendshipStatus.NOTCONFIRMED);
        }
    }


    public void deleteFriends(long id, long friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Введен не верный ID=" + id);
        } else if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Введен не верный ID=" + friendId);
        }
            userStorage.getUsers().get(id).getFriends().remove(friendId);
        if (userStorage.getUsers().get(friendId).getFriends().containsKey(id)) {
            userStorage.getUsers().get(friendId).getFriends().put(id, FriendshipStatus.NOTCONFIRMED);
        }
    }

    public List<User> commonFriend(long id, long friendId) {
        List<User> commonFriend = new ArrayList<>();
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserNotFoundException("Введен не верный ID=" + id);
        } else if (!userStorage.getUsers().containsKey(friendId)) {
            throw new UserNotFoundException("Введен не верный ID=" + friendId);
        }
            Set<Long> usersId = new TreeSet<>();
        userStorage.getUsers().get(id).getFriends().forEach(((h,g)->usersId.add(h)));
            Set<Long> usersFriendId = new TreeSet<>();
            userStorage.getUsers().get(friendId).getFriends().forEach((h,g)->usersFriendId.add(h));
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
