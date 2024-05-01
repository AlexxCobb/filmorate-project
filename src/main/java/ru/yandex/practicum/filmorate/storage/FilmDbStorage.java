package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, rating_mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ? " +
                "where id = ?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) == 0) {
            log.info("Film with id: {} not found.", film.getId());
            throw new NotFoundException("Wrong film ID: " + film.getId());
        }
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        String sqlQuery = "select * from films where id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
            return Optional.ofNullable(film);
        } catch (
                DataAccessException e) {
            throw new NotFoundException("Id doesn't exists. " + id);
        }
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String sqlQuery = "insert into like_films (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        return findFilmById(filmId).get();
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sqlQuery = "delete from like_films where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        String sqlQuery = "select f.id, f.name, f.description, f.release_date, f.duration " +
                "from films f " +
                "join like_films l on f.id = l.film_id " +
                "group by l.film_id " +
                "order by count(l.user_id) desc " +
                "limit ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public void addGenreToFilm(Long filmId, Integer genreId) {
        String sqlQuery = "insert into film_genres (film_id, genre_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, genreId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .build();
    }
}