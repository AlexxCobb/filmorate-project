package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaService {
    List<Mpa> getAllMpaRatings();

    Mpa getMpaRatingById(Integer id);

    Mpa getRatingByFilmId(Long id);
}
