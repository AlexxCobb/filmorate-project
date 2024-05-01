package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.interfaces.LikesService;
import ru.yandex.practicum.filmorate.storage.interfaces.LikesStorage;

import java.util.Set;

@Service
public class LikesServiceImpl implements LikesService {

    @Autowired
    private LikesStorage likesStorage;

    @Override
    public Set<Long> getLikes(Long id) {
        return likesStorage.getLikes(id);
    }
}
