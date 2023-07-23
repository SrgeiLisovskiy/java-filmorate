package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class MpaDbStorageTest {
    private final MpaStorage mpaStorage;

    @Test
    void getAllMpa() {
        assertEquals(5, mpaStorage.getAllMpa().size());
    }

    @Test
    void getByID() {
        Mpa mpa = Mpa.builder().id(1L).name("G").build();
        assertEquals(mpa, mpaStorage.getByID(1L));
    }
}
