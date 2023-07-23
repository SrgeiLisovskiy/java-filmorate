package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;



    @Test
    void getUserByID() {
        assertThat(userService.getListUsers()).isNotNull();
        User user = User.builder().name("TestName").birthday(LocalDate.of(1999, 1, 21)).login("Testlogin").email("lis@mail.ru").build();
        userService.addUser(user);
        assertEquals(userService.getListUsers().size(), 1);
        assertEquals(userService.getListUsers().get((int) user.getId()), user);
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
        userService.addFriends(1, 3);
        assertEquals(userService.getFriendUserId(1).size(), 2);
        assertEquals(userService.getFriendUserId(2).size(), 1);
        assertEquals(userService.getFriendUserId(3).size(), 1);
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
        userService.addFriends(1, 3);
        assertEquals(userService.getFriendUserId(1).size(), 2);
        userService.deleteFriends(1, 3);
        assertEquals(userService.getFriendUserId(1).size(), 1);
        assertThat(userService.getFriendUserId(3)).isNotNull();
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