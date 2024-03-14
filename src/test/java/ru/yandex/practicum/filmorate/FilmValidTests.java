package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class FilmValidTests {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenFilmWithEmptyNameThenFailValidation() {
        Film film = new Film();
        film.setName("");
        film.setReleaseDate(LocalDate.of(2000, 2, 20));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmDescriptionExceededMaxLengthThenFailValidation() {
        Film film = new Film();
        film.setName("Inception");
        film.setReleaseDate(LocalDate.of(2010, 7, 8));
        film.setDescription("���� � ����������� ���, ������ �� ������ � ������� ��������� " +
                "����������: �� ������ ������ ������� �� ������ ����������� �� ����� ���, " +
                "����� ������������ ����� �������� ������. " +
                "������ ����������� ����� ������� ��� ������ ������� � ��������� " +
                "� ������������� ���� ������������� ��������, �� ��� �� ���������� " +
                "��� � ��������� ������� � ������ �����, ��� �� �����-���� �����.\n" +
                "\n" +
                "� ��� � ����� ���������� ���� ��������� ������. ��� " +
                "��������� ���� ����� ������� ��� �����, �� ��� ����� ��� " +
                "����� ��������� ����������� � ���������. ������ ��������� " +
                "����� ���� � ��� ������� ������ ������ ����� ���������� ��������. " +
                "������ �� ������ � �� ������� ����, � �������� ��. " +
                "���� � ��� ���������, ��� � ������ ��������� �������������.\n" +
                "\n" +
                "�� ������� ������������ ��� ���������� �� ����� ����������� " +
                "������� � ������� � ������� �����������, �������, �������, " +
                "������������� ������ �� ���. ������, ������� �������� ��� �� ���� ����.");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithInvalidDateReleaseInPastThenFailValidation() {
        Film film = new Film();
        film.setName("Tristan and Isolde");
        film.setReleaseDate(LocalDate.of(1210, 2, 20));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithInvalidDateReleaseInFutureThenFailValidation() {
        Film film = new Film();
        film.setName("Putin - The King");
        film.setReleaseDate(LocalDate.of(2210, 2, 20));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithInvalidDurationThenFailValidation() {
        Film film = new Film();
        film.setName("La grande bellezza");
        film.setReleaseDate(LocalDate.of(2013, 5, 21));
        film.setDuration(-141);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }
}
