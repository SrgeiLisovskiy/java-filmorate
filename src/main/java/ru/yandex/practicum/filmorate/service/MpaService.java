package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        log.info("Получен список всех рейтингов");
        return mpaStorage.getAllMpa();
    }

    public Mpa getByID(Long id) throws NotFoundException {
        if (mpaStorage.getByID(id) != null) {
            log.info("Получен рейтинг с ID = {}",id);
            return mpaStorage.getByID(id);
        } else throw new NotFoundException("Mpa с данным id  не найден");
    }

}
