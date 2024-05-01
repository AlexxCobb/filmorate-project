package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MpaServiceImpl implements MpaService {

    @Autowired
    private MpaStorage mpaStorage;

    @Override
    public List<Mpa> getAllMpaRatings() {
        return mpaStorage.getAllRatings().stream().sorted(Comparator.comparing(Mpa::getId)).collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaRatingById(Integer id) {
        return mpaStorage.findRatingById(id).
                orElseThrow(() -> new BadRequestException(String.format("Передан отсутствующий Mpa с id: %s.", id)));
    }

    @Override
    public Mpa getRatingByFilmId(Long id) {
        return mpaStorage.findRatingByFilmId(id).
                orElseThrow(() -> new NotFoundException(String.format("Mpa с film id: %s не существует", id)));
    }
}
