package ru.yandex.practicum.filmorate.exception;

public class NotValidIdException extends RuntimeException {
    public NotValidIdException(String message) {
        super(message);
    }
}
