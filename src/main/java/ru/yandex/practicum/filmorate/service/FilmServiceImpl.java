package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotValidLikeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        var film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        if (film.getLikeIds() == null) {
            Set<Long> filmAddLikeId = new HashSet<>();
            filmAddLikeId.add(userId);
            film.setLikeIds(filmAddLikeId);
            return film;
        }
        if (film.getLikeIds().contains(userId)) {
            throw new NotValidLikeException("One user - one like, exceeded the allowed number of likes");
        }
        film.getLikeIds().add(userId);
        return film;
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        var film = filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        if (film.getLikeIds().contains(userId)) {
            film.getLikeIds().remove(userId);
        } else {
            throw new NotValidLikeException(
                    String.format("No like from user with id - %s to film with id - %s", userId, filmId));
        }
    }

    @Override
    public List<Film> getPopularFilms(Long size) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> !film.getLikeIds().isEmpty())
                .sorted(Comparator.comparingInt((Film film) -> film.getLikeIds().size()).reversed())
                .limit(size)
                .collect(Collectors.toList());
    }
}
