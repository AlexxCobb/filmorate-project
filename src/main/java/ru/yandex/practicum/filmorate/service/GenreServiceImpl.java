package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.GenreService;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreStorage genreStorage;

    @Override
    public Set<Genre> getAllGenres() {
        return genreStorage.getAllGenres().stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Genre getGenreById(Integer id) {
        return genreStorage.findGenreById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Жанр с таким id: %s, отсутствует", id)));
    }

    @Override
    public Set<Genre> getAllGenresByFilm(Long id) {
        return genreStorage.getAllGenresByFilm(id);
    }
}
