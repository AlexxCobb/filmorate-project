package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.interfaces.FriendsService;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendsServiceImpl implements FriendsService {
    private final FriendsStorage friendsStorage;

    @Override
    public Set<Long> getFriends(Long id) {
        return friendsStorage.getFriends(id);
    }
}
