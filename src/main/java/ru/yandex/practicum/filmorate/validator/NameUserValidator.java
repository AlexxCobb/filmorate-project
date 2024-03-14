package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.validator.interfaces.ValidNameUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NameUserValidator implements ConstraintValidator<ValidNameUser, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return false;
    }
}
