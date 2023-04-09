package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private InMemoryUserStorage controller;


    @Test
    void addUserNoLoginAndNull() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().name("Test").login("").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
        User user2 = User.builder().name("Test").login("Test login").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.addUser(user2);
        });
        assertNotNull(thrown1.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void addUserCheckMail() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().name("Test").login("testlogin").email("mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
        User user2 = User.builder().name("Test").login("login").email("").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.addUser(user2);
        });
        assertNotNull(thrown1.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void addUserNoName() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().login("testlogin").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user1);
        assertEquals(2, controller.getUsers().size());
    }

    @Test
    void addUserCheckBirthday() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().name("Test").login("testlogin").email("mail@mail.ru").birthday(LocalDate.of(2024, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.addUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void updateUserNoLoginAndNull() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().id(1).name("Test").login("").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
        User user2 = User.builder().id(1).name("Test").login("Test login").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user2);
        });
        assertNotNull(thrown1.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void updateUserCheckMail() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().id(1).name("Test").login("testlogin").email("mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
        User user2 = User.builder().id(1).name("Test").login("login").email("").birthday(LocalDate.of(1991, 2, 14)).build();
        Throwable thrown1 = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user2);
        });
        assertNotNull(thrown1.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void updateUserNoName() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().id(user.getId()).name("").login("testlogin").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.updateUser(user1);
        assertEquals(controller.getUsers().get(user1.getId()).getName(), user1.getLogin());
        assertEquals(1, controller.getUsers().size());
    }

    @Test
    void updateUserCheckBirthday() {
        controller.getUsers().clear();
        assertThat(controller).isNotNull();
        assertNotNull(controller.getUsers());
        User user = User.builder().name("Test").login("serg").email("mail@mail.ru").birthday(LocalDate.of(1991, 2, 14)).build();
        controller.addUser(user);
        assertEquals(1, controller.getUsers().size());
        User user1 = User.builder().id(1).name("Test").login("testlogin").email("mail@mail.ru").birthday(LocalDate.of(2024, 2, 14)).build();
        Throwable thrown = assertThrows(ValidationException.class, () -> {
            controller.updateUser(user1);
        });
        assertNotNull(thrown.getMessage());
        assertEquals(1, controller.getUsers().size());
    }

}