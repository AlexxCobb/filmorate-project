package ru.yandex.practicum.filmorate.service.interfaces;

import java.util.Set;

public interface FriendsService {
    Set<Long> getFriends(Long id);
}
