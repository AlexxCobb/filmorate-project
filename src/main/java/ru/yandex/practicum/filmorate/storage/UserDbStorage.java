package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "where id = ?";
        if (jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) == 0) {
            log.info("User with id: {} not found.", user.getId());
            throw new NotFoundException("Wrong user ID" + user.getId());
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, MapRowClass::mapRowToUser);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        String sqlQuery = "select * from users where id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, MapRowClass::mapRowToUser, id);
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            throw new NotFoundException("Id doesn't exist. " + id);
        }
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        String sqlQuery = "insert into users_friendship (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        return findUserById(userId).get();
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sqlQuery = "delete from users_friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sqlQuery = "select * from users where id in " +
                "(select friend_id from users_friendship where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, MapRowClass::mapRowToUser, id);
    }

    @Override
    public List<User> commonFriends(Long userFirst, Long userSecond) {
        String sqlQuery = "select * from users where id in " +
                "(select friend_id from users_friendship where user_id = ?) " +
                "and id in (select friend_id from users_friendship where user_id = ?)";
        return jdbcTemplate.query(sqlQuery, MapRowClass::mapRowToUser, userFirst, userSecond);
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", user.getId());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }
}