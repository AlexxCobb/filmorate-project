package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.validator.interfaces.ValidReleaseDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class ReleaseDateValidator implements ConstraintValidator<ValidReleaseDate, LocalDate> {
    private final LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return (value.isAfter(movieBirthday) && value.isBefore(LocalDate.now()));
    }
}
