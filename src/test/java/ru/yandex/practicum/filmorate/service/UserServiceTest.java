package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    InMemoryUserStorage inMemoryUserStorage;


    @Test
    void getUserByID() {
        assertThat(inMemoryUserStorage.getUsers()).isNotNull();
        User user = User.builder().name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        assertEquals(inMemoryUserStorage.getUsers().size(), 1);
        assertEquals(inMemoryUserStorage.getUsers().get(user.getId()), user);
    }

    @Test
    void getFriendUserId() {
        User user = User.builder().id(1).name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user1 = User.builder().id(2).name("TestName1").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user2 = User.builder().id(3).name("TestName2").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
        assertThat(userService.getFriendUserId(1)).isNotNull();
        userService.addFriends(1, 2);
        userService.addFriends(1, 3);
        assertEquals(userService.getFriendUserId(1).size(), 2);
    }

    @Test
    void addFriends() {
        User user = User.builder().id(1).name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user1 = User.builder().id(2).name("TestName1").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user2 = User.builder().id(3).name("TestName2").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
        assertThat(userService.getFriendUserId(1)).isNotNull();
        userService.addFriends(1, 2);
        assertEquals(userService.getUserByID(1).getStatus(2), FriendshipStatus.NOTCONFIRMED);
        userService.addFriends(2, 1);
        assertEquals(userService.getUserByID(1).getStatus(2), FriendshipStatus.CONFIRMED);
        assertEquals(userService.getUserByID(2).getStatus(1), FriendshipStatus.CONFIRMED);
    }

    @Test
    void deleteFriends() {
        User user = User.builder().id(1).name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user1 = User.builder().id(2).name("TestName1").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user2 = User.builder().id(3).name("TestName2").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
        assertThat(userService.getFriendUserId(1)).isNotNull();
        userService.addFriends(1, 2);
        userService.addFriends(2, 1);
        assertEquals(userService.getUserByID(1).getStatus(2), FriendshipStatus.CONFIRMED);
        assertEquals(userService.getUserByID(2).getStatus(1), FriendshipStatus.CONFIRMED);
        userService.deleteFriends(1, 2);
        assertThat(userService.getFriendUserId(1)).isNotNull();
        assertEquals(userService.getUserByID(2).getStatus(1), FriendshipStatus.NOTCONFIRMED);
    }

    @Test
    void commonFriend() {
        User user = User.builder().id(1).name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user1 = User.builder().id(2).name("TestName1").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        User user2 = User.builder().id(3).name("TestName2").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
        assertThat(userService.getFriendUserId(1)).isNotNull();
        userService.addFriends(1, 2);
        userService.addFriends(1, 3);
        assertEquals(userService.commonFriend(2, 3).size(), 1);
    }
}