package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.interfaces.ValidReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;


@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(min = 0, max = 200)
    @NotNull
    private String description;
    @ValidReleaseDate
    private LocalDate releaseDate;
    @Positive
    @NotNull
    private Integer duration;
}
