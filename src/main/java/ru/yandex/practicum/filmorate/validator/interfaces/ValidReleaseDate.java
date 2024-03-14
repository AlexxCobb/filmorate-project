package ru.yandex.practicum.filmorate.validator.interfaces;


import ru.yandex.practicum.filmorate.validator.ReleaseDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = {ReleaseDateValidator.class})
public @interface ValidReleaseDate {
    String message() default "Дата релиза должна быть не раньше 28.12.1895 и не позже сегодняшнего дня";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
