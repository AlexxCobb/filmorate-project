package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserValidTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenUserWithInvalidEmailThenFailValidation() {
        User user = new User();
        user.setLogin("Apollo");
        user.setEmail("Apollo.13");
        user.setBirthday(LocalDate.of(2000, 2, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenUserWithInvalidLoginThenFailValidation() {
        User user = new User();
        user.setLogin("Apollo 13");
        user.setEmail("Apollo@mail.ru");
        user.setBirthday(LocalDate.of(2000, 2, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenUserWithEmptyLoginThenFailValidation() {
        User user = new User();
        user.setLogin("");
        user.setEmail("Apollo@mail.ru");
        user.setBirthday(LocalDate.of(2000, 2, 20));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenPostUserWithEmptyNameThenUsedLoginInsteadOfNameAndValidTrue() {
        User user = new User();
        user.setLogin("Apollo");
        user.setEmail("Apollo@mail.ru");
        user.setBirthday(LocalDate.of(2000, 2, 20));
        UserController userController = new UserController();
        userController.addUser(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void whenUserWithInvalidLocalDateThenFailValidation() {
        User user = new User();
        user.setLogin("Apollo");
        user.setEmail("Apollo@mail.ru");
        user.setBirthday(LocalDate.of(2030, 11, 11));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenUserWithNullLocalDateThenFailValidation() {
        User user = new User();
        user.setLogin("Apollo");
        user.setEmail("Apollo@mail.ru");
        user.setBirthday(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }
}
