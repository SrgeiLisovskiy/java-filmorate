package ru.yandex.practicum.filmorate.model;


import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @NotBlank
    private LocalDate releaseDate;
    @NotNull
    private int duration;
    private Set<Long> like;
    private List<Genre> genres;
    private Mpa mpa;


}
