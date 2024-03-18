package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.validator.interfaces.WithoutSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WithoutSpaceValidator implements ConstraintValidator<WithoutSpace, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.chars().noneMatch(Character::isWhitespace);
    }
}
