package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllRatings() {
        String sqlQuery = "select * from ratings";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Optional<Mpa> findRatingById(Integer id) {
        String sqlQuery = "select * from ratings where id = ?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
            return Optional.ofNullable(mpa);
        } catch (DataAccessException e) {
            throw new NotFoundException("Mpa id doesn't exist." + id);
        }
    }

    @Override
    public Optional<Mpa> findRatingByFilmId(Long id) {
        String sqlQuery = "select r.id, r.name from films f join ratings r on f.rating_mpa_id = r.id where f.id = ?";
        try {
            Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
            return Optional.ofNullable(mpa);
        } catch (DataAccessException e) {
            throw new NotFoundException("Mpa with film id:" + id + "doesn't exist.");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
