package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public interface UserStorage {
    User addUser(@NotNull @Valid User user);

    User updateUser(@NotNull @Valid User user);

    List<User> getListUsers();


    User getUserByID(Long id);

    User deleteUser(Long id);

    User addFriend(Long userId, Long friendId);

    User deleteFriend(Long userId, Long friendId);

    List<User> getFriendsByUserId(Long id);


}
