package ru.yandex.practicum.filmorate.storage.in_memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long idGenerator = 1;

    @Override
    public User addUser(User user) {
        user.setId(idGenerator);
        log.info("Пользователь добавлен, присвоен номер id: {}.", user.getId());
        users.put(user.getId(), user);
        idGenerator++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с номером id: {}, обновлен.", user.getId());
            users.put(user.getId(), user);
            return user;
        } else {
            log.info("Пользователь с таким id: " + user.getId() + " отсутствует");
            throw new NotFoundException("Пользователь с таким id: " + user.getId() + " отсутствует");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        return null;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {

    }

    @Override
    public List<User> getFriends(Long id) {
        return null;
    }

    @Override
    public List<User> commonFriends(Long userFirst, Long userSecond) {
        return null;
    }
}
