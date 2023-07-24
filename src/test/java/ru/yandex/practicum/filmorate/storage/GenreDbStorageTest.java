package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class GenreDbStorageTest {
    private final GenreStorage genreStorage;


    @Test
    void getAllGenres() {
        assertEquals(6,genreStorage.getAllGenres().size());
    }



    @Test
    void getByID() {
        Genre genre1 = Genre.builder().id(1L).name("Комедия").build();
        assertEquals(genre1,genreStorage.getByID(1L));
    }


}
