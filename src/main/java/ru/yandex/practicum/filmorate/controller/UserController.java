package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotValidIdException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с таким id" + user.getId() + "уже существует.");
            throw new NotValidIdException("Пользователь с таким id" + user.getId() + "уже существует.");
        } else {
            user.setId(idGenerator);
            checkName(user);
            log.info("Пользователь добавлен, присвоен номер id: {}.", user.getId());
            users.put(user.getId(), user);
            idGenerator++;
            return user;
        }
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с номером id: {}, обновлен.", user.getId());
            checkName(user);
            users.put(user.getId(), user);
        } else {
            log.info("Пользователь с таким id: " + user.getId() + "отсутствует");
            throw new NotValidIdException("Пользователь с таким id: " + user.getId() + "отсутствует");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
