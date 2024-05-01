package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> getAllGenres() {
        String sqlQuery = "select * from genres";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre));
    }

    @Override
    public Optional<Genre> findGenreById(Integer id) {
        String sqlQuery = "select * from genres where id = ?";
        try {
            Genre genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
            return Optional.ofNullable(genre);
        } catch (DataAccessException e) {
            throw new NotFoundException("Id doesn't exist." + id);
        }
    }

    @Override
    public Set<Genre> getAllGenresByFilm(Long id) {
        String sqlQuery = "select * from genres where id in " +
                "(select genre_id from film_genres where film_id = ?)";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id));
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
