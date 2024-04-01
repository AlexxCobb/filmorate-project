package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsDoesNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        var user = userStorage.getUserById(userId);
        var friend = userStorage.getUserById(friendId);

        if (user.getFriendsIds() == null && friend.getFriendsIds() == null) { // изменил || на &&
            Set<Long> userAddFriendId = new HashSet<>();
            userAddFriendId.add(friendId);
            user.setFriendsIds(userAddFriendId);
            updateUser(user);

            Set<Long> friendAddUserId = new HashSet<>();
            friendAddUserId.add(userId);
            friend.setFriendsIds(friendAddUserId);
            updateUser(friend);
        }
        checkFriendsIds(user);
        checkFriendsIds(friend);
        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
        return user;
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        var user = userStorage.getUserById(userId);
        var friend = userStorage.getUserById(friendId);
        checkFriendsIds(user);
        checkFriendsIds(friend);
        if (user.getFriendsIds().contains(friendId) && friend.getFriendsIds().contains(userId)) {
            user.getFriendsIds().remove(friendId);
            friend.getFriendsIds().remove(userId);
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        var user = userStorage.getUserById(id);
        List<User> userFriends = new ArrayList<>();
        checkFriendsIds(user);
        for (Long friendsId : user.getFriendsIds()) {
            if (userStorage.getUserById(friendsId) != null) {
                userFriends.add(userStorage.getUserById(friendsId));
            } else {
                throw new UserNotFoundException("User id doesn't exist");
            }
        }
        return userFriends;
    }

    @Override
    public List<User> commonFriends(Long userFirst, Long userSecond) {
        var user = userStorage.getUserById(userFirst);
        var friend = userStorage.getUserById(userSecond);

        checkFriendsIds(user);
        checkFriendsIds(friend);

        Set<Long> userFriendsIds = user.getFriendsIds();
        Set<Long> friendFriendsIds = friend.getFriendsIds();
        Set<Long> commonFriends = new HashSet<>(userFriendsIds);
        commonFriends.retainAll(friendFriendsIds);

        List<User> commonUsers = new ArrayList<>();
        for (Long commonFriendId : commonFriends) {
            if (userStorage.getUserById(commonFriendId) != null) {
                commonUsers.add(userStorage.getUserById(commonFriendId));
            } else {
                throw new UserNotFoundException("User id doesn't exist");
            }
        }
        return commonUsers;
    }

    private void checkFriendsIds(User user) {
        if (user.getFriendsIds() == null) {
            throw new FriendsDoesNotExistException("User don't have friends");
        }
    }
}
