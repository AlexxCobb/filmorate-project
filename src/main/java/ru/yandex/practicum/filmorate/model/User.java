package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validator.interfaces.WithoutSpace;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @WithoutSpace
    private String login;
    private String name;
    @PastOrPresent
    @NotNull
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendsIds = new HashSet<>();
}
