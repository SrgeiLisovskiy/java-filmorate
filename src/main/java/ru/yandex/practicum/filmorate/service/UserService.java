package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.vadators.ValidatorUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor

public class UserService {

    private final UserStorage userStorage;

    public User addUser(User user) {
        ValidatorUser.validationUsers(user);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь добавлен: {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        ValidatorUser.validationUsers(user);
        return userStorage.updateUser(user);
    }

    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    public User getUserByID(long id) {
        if (userStorage.getUserByID(id) != null) {
            return userStorage.getUserByID(id);
        } else
            throw new NotFoundException("Не верный ID");
    }

    public List<User> getFriendUserId(long id) {
        return userStorage.getFriendsByUserId(id);
    }

    public User addFriends(long id, long friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public void deleteFriends(long userId, long friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> commonFriend(long userId, long friendId) {
        User user = getUserByID(userId);
        User friend = getUserByID(friendId);
        Set<User> commonFriend = null;
        if (user != null && friend != null) {
            commonFriend = new HashSet<>(getFriendUserId(userId));
            commonFriend.retainAll(getFriendUserId(friendId));
        }
        return new ArrayList<>(commonFriend);
    }


}
