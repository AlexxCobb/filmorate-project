package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.interfaces.FriendsService;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendsStorage;

import java.util.Set;

@Service
public class FriendsServiceImpl implements FriendsService {

    @Autowired
    private FriendsStorage friendsStorage;

    @Override
    public Set<Long> getFriends(Long id) {
        return friendsStorage.getFriends(id);
    }
}
