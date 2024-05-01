package ru.yandex.practicum.filmorate.service.interfaces;

import java.util.Set;

public interface LikesService {
    Set<Long> getLikes(Long id);
}
