package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@Component
public interface UserStorage {
    User addUser(@NotNull @Valid User user);

    User updateUser(@NotNull @Valid User user);

    List<User> getListUsers();

    void setUsers(@NotBlank HashMap<Long, User> users);

    HashMap<Long, User> getUsers();


}
