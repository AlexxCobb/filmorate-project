package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long id);

    User addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> commonFriends(Long userFirst, Long userSecond);
}
