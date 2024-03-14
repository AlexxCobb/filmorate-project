package ru.yandex.practicum.filmorate.validator.interfaces;

import ru.yandex.practicum.filmorate.validator.NameUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {NameUserValidator.class})
public @interface ValidNameUser {
    String message() default "Имя не указано, будет использован логин";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
