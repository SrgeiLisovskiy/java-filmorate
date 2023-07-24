package ru.yandex.practicum.filmorate.vadators;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class ValidatorUser {
    public static void validationUsers(User user) {
        if (user.getEmail().isBlank()) {
            log.error("Электронная почта не может быть пустой: {}", user);
            throw new ValidationException("Электронная почта не может быть пустой");
        }
        if (!user.getEmail().contains("@")) {
            log.error("Электронная почта должна содержать символ @ : {}", user);
            throw new ValidationException("Электронная почта должна содержать символ @");
        }
        if (user.getLogin().isBlank()) {
            log.error("Login не может быть пустым:{}", user);
            throw new ValidationException("Login не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем: {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
