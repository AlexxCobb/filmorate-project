package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.interfaces.*;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserService userService;
    @Autowired
    private MpaService mpaService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private LikesService likesService;

    @Override
    public Film addFilm(Film film) {
        if (film.getMpa().getId() < 0 || film.getMpa().getId() > 5) {
            throw new BadRequestException("Передан не существующий рейтинг Mpa.");
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() < 0 || genre.getId() > 6) {
                    throw new BadRequestException("Передан не существующий рейтинг");
                }
            }
        }
        Film createdFilm = filmStorage.addFilm(film);

        if (film.getLikeIds() == null) {
            film.setLikeIds(new HashSet<>());
        }
        if (!film.getLikeIds().isEmpty()) {
            for (Long likeId : film.getLikeIds()) {
                filmStorage.addLike(createdFilm.getId(), likeId);
            }
        }
        Optional.ofNullable(film.getGenres())
                .stream()
                .flatMap(Collection::stream)
                .map(Genre::getId)
                .forEach(genreId -> filmStorage.addGenreToFilm(createdFilm.getId(), genreId));
        return createdFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getMpa().getId() < 0 || film.getMpa().getId() > 5) {
            throw new BadRequestException("Передан не существующий рейтинг Mpa.");
        }
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() < 0 || genre.getId() > 6) {
                    throw new BadRequestException("Передан не существующий рейтинг");
                }
            }
        }

        Film updatedFilm = filmStorage.updateFilm(film);

        if (film.getLikeIds() != null) {
            for (Long likeId : film.getLikeIds()) {
                filmStorage.addLike(updatedFilm.getId(), likeId);
            }
        }

        if (film.getGenres() != null) {
            List<Integer> genresIds = film.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
            if (!genresIds.isEmpty()) {
                for (Integer genresId : genresIds) {
                    filmStorage.addGenreToFilm(updatedFilm.getId(), genresId);
                }
            }
        }

        return updatedFilm;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long id) {
        Film film = filmStorage.findFilmById(id).orElseThrow(
                () -> new NotFoundException(String.format("Фильм с таким id: %s, отсутствует", id)));
        return Film.builder()
                .id(film.getId())
                .name(film.getName())
                .duration(film.getDuration())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .mpa(mpaService.getRatingByFilmId(id))
                .likeIds(likesService.getLikes(film.getId()))
                .genres(genreService.getAllGenresByFilm(film.getId()))
                .build();
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        var film = getFilmById(filmId);
        userService.getUserById(userId);
        if (likesService.getLikes(filmId).contains(userId)) {
            throw new BadRequestException("One user - one like, exceeded the allowed number of likes");

        }
        return filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        var film = getFilmById(filmId);
        userService.getUserById(userId);

        if (!likesService.getLikes(filmId).contains(userId)) {
            throw new NotFoundException(
                    String.format("No like from user with id - %s to film with id - %s", userId, filmId));
        }
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Long size) {
        return filmStorage.getPopularFilms(size);

    }
}