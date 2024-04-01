package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotValidIdException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long idGenerator = 1;

    @Override
    public User addUser(User user) {
        user.setId(idGenerator);
        checkName(user);
        log.info("Пользователь добавлен, присвоен номер id: {}.", user.getId());
        users.put(user.getId(), user);
        idGenerator++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с номером id: {}, обновлен.", user.getId());
            checkName(user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.info("Пользователь с таким id: " + user.getId() + " отсутствует");
            throw new NotValidIdException("Пользователь с таким id: " + user.getId() + " отсутствует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User getUserById(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new UserNotFoundException("Пользователь с таким id: " + id + "отсутствует");
        }
    }
}
