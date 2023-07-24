package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RequestMapping("/users")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.debug("Получен запрос GET /users");
        return userService.getListUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.debug("Получен запрос POST /users");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Получен запрос PUT /users");
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Integer id) {
        log.debug("Получен запрос GET /users/{id}");
        return userService.getUserByID(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendUser(@PathVariable Integer id) {
        log.debug("Получен запрос GET /users/{id}/friends");
        return userService.getFriendUserId(id);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriends(id, friendId);
        log.debug("Получен запрос DEL /users/{id}/friends/{friendId}");

    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}");
         return userService.addFriends(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriend(@PathVariable Integer id, @PathVariable(value = "otherId") Integer friendId) {
        log.debug("Получен запрос GET /users/{id}/friends/common/{otherId}");
        return userService.commonFriend(id, friendId);
    }


}
