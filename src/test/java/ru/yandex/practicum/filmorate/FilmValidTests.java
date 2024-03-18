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
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2000, 2, 20));
        film.setDuration(10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmDescriptionExceededMaxLengthThenFailValidation() {
        Film film = new Film();
        film.setName("Inception");
        film.setReleaseDate(LocalDate.of(2010, 7, 8));
        film.setDescription("Кобб – талантливый вор, лучший из лучших в опасном искусстве извлечения: " +
                "он крадет ценные секреты из глубин подсознания во время сна, " +
                "когда человеческий разум наиболее уязвим. Редкие способности Кобба " +
                "сделали его ценным игроком в привычном к предательству мире промышленного шпионажа, " +
                "но они же превратили его в извечного беглеца и лишили всего, что он когда-либо любил.\n" +
                "\n" +
                "И вот у Кобба появляется шанс исправить ошибки. Его последнее дело может " +
                "вернуть все назад, но для этого ему нужно совершить невозможное – инициацию. " +
                "Вместо идеальной кражи Кобб и его команда спецов должны будут провернуть обратное. " +
                "Теперь их задача – не украсть идею, а внедрить ее. Если у них получится, " +
                "это и станет идеальным преступлением.\n" +
                "\n" +
                "Но никакое планирование или мастерство не могут подготовить команду к встрече " +
                "с опасным противником, который, кажется, предугадывает каждый их ход. Врагом, " +
                "увидеть которого мог бы лишь Кобб.");
        film.setDuration(148);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithInvalidDateReleaseThenFailValidation() {
        Film film = new Film();
        film.setName("Tristan and Isolde");
        film.setReleaseDate(LocalDate.of(1210, 2, 20));
        film.setDuration(130);
        film.setDescription("novel");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithNullDateReleaseThenFailValidation() {
        Film film = new Film();
        film.setName("Tristan and Isolde");
        film.setReleaseDate(null);
        film.setDuration(130);
        film.setDescription("novel");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithNullDurationFailValidation() {
        Film film = new Film();
        film.setName("Putin - The King");
        film.setReleaseDate(LocalDate.of(2210, 2, 20));
        film.setDescription("poem");
        film.setDuration(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenFilmWithInvalidDurationThenFailValidation() {
        Film film = new Film();
        film.setName("La grande bellezza");
        film.setDescription("Богемный писатель задумывается о смысле жизни. " +
                "Шедевр Паоло Соррентино о душевной пустоте, залитой шампанским");
        film.setReleaseDate(LocalDate.of(2013, 5, 21));
        film.setDuration(-141);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }
}
