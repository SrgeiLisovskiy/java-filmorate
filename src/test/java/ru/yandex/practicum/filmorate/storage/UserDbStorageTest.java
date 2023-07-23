package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)


public class UserDbStorageTest {
    private final UserStorage userStorage;
    private User user;
    private User user1;
    private User user2;
    private User userUpdate;

    @AfterEach
    void afterEach() {
        List<User> usersList = userStorage.getListUsers();
        for (User u : usersList) {
            userStorage.deleteUser(u.getId());
        }
    }

    @BeforeEach
    public void beforeEach() {
        user = User.builder().name("name").
                login("name_log").
                email("name@mail.ru").
                birthday(LocalDate.now())
                .build();

        user1 = User.builder().name("name1").
                login("name1_log").
                email("name1@mail.ru").
                birthday(LocalDate.now())
                .build();
        user2 = User.builder().name("name2").
                login("name2_log").
                email("name2@mail.ru").
                birthday(LocalDate.now())
                .build();
        userUpdate = User.builder().name("nameUpdate").
                login("name3_log").
                email("name3@mail.ru").
                birthday(LocalDate.now())
                .build();
    }


    @Test
    void addUser() {
        assertThat(userStorage.getListUsers()).isNotNull();
        userStorage.addUser(user);
        assertEquals(user, userStorage.getUserByID(user.getId()));
    }

    @Test
    void updateUser() {
        assertThat(userStorage.getListUsers()).isNotNull();
        userStorage.addUser(user);
        assertEquals(user, userStorage.getUserByID(user.getId()));
        userUpdate.setId(user.getId());
        userStorage.updateUser(userUpdate);
        assertEquals(userUpdate, userStorage.getUserByID(user.getId()));
    }

    @Test
    void getListUsers() {
        assertThat(userStorage.getListUsers()).isNotNull();
        userStorage.addUser(user);
        userStorage.addUser(user1);
        userStorage.addUser(user2);

        List<User> testList = new ArrayList<>();
        testList.add(user);
        testList.add(user1);
        testList.add(user2);

        assertEquals(testList, userStorage.getListUsers());


    }

    @Test
    void getUserByID() {
        assertThat(userStorage.getListUsers()).isNotNull();
        userStorage.addUser(user);
        userStorage.addUser(user1);
        assertEquals(user, userStorage.getUserByID(user.getId()));
    }

    @Test
    void deleteUser() {
        assertThat(userStorage.getListUsers()).isNotNull();
        userStorage.addUser(user);
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        assertEquals(3, userStorage.getListUsers().size());
        userStorage.deleteUser(user.getId());
        assertEquals(2, userStorage.getListUsers().size());
    }


    @Test
    void addFriend() {
        userStorage.addUser(user);
        assertThat(userStorage.getFriendsByUserId(user.getId())).isNotNull();
        userStorage.addUser(user1);
        userStorage.addFriend(user.getId(), user1.getId());
        assertEquals(1, userStorage.getFriendsByUserId(user.getId()).size());
    }

    @Test
    void deleteFriend() {
        userStorage.addUser(user);
        assertThat(userStorage.getFriendsByUserId(user.getId())).isNotNull();
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addFriend(user.getId(), user1.getId());
        userStorage.addFriend(user.getId(), user2.getId());
        assertEquals(2, userStorage.getFriendsByUserId(user.getId()).size());
        userStorage.deleteFriend(user.getId(), user1.getId());
        assertEquals(1, userStorage.getFriendsByUserId(user.getId()).size());
    }

    @Test
    void getFriendsByUserId() {
        List<User> friends = new ArrayList<>();
        userStorage.addUser(user);
        assertThat(userStorage.getFriendsByUserId(user.getId())).isNotNull();
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addFriend(user.getId(), user1.getId());
        friends.add(user1);
        userStorage.addFriend(user.getId(), user2.getId());
        friends.add(user2);
        assertEquals(friends, userStorage.getFriendsByUserId(user.getId()));

    }

}
