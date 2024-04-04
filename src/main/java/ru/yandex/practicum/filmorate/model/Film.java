package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.interfaces.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private Long id;
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
    @JsonIgnore
    private Set<Long> likeIds = new HashSet<>();
}
