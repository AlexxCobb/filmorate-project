package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotValidIdException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(idGenerator);
        log.info("Фильм добавлен, присвоен номер id: {}.", film.getId());
        films.put(film.getId(), film);
        idGenerator++;
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Фильм с номером id: {}, обновлен.", film.getId());
            films.put(film.getId(), film);
        } else {
            log.info("Фильм с таким id: " + film.getId() + "отсутствует");
            throw new NotValidIdException("Фильм с таким id: " + film.getId() + "отсутствует");
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
