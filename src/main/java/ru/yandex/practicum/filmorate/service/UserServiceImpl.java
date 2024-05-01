package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        var createdUser = userStorage.addUser(user);
        checkName(user);
        return createdUser;
    }

    @Override
    public User updateUser(User user) {
        var updatedUser = userStorage.updateUser(user);
        checkName(updatedUser);
        return updatedUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.findUserById(id).orElseThrow(
                () -> new NotFoundException(String.format("Пользователь с таким id: %s, отсутствует", id)));
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        var user = getUserById(userId);
        var friend = getUserById(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        var user = getUserById(userId);
        var friend = getUserById(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        var user = getUserById(id);
        return userStorage.getFriends(id);
    }

    @Override
    public List<User> commonFriends(Long userFirst, Long userSecond) {
        var user = getUserById(userFirst);
        var friend = getUserById(userSecond);
        return userStorage.commonFriends(userFirst, userSecond);
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
