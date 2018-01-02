package io.github.ajoz.validation;

import static io.github.ajoz.validation.Validation.ap;
import static io.github.ajoz.validation.Validation.failure;
import static io.github.ajoz.validation.Validation.success;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PersonTest {
    private final static ErrorMessage WRONG_NAME_ERROR = new ErrorMessage("Name length not between 1 and 50 characters");
    private final static ErrorMessage WRONG_EMAIL_ERROR = new ErrorMessage("Email does not have @ sign");
    private final static ErrorMessage WRONG_AGE_ERROR = new ErrorMessage("Age not between 0 and 120");

    private final static ErrorMessage WRONG_ALL_ERROR = WRONG_NAME_ERROR.append(WRONG_EMAIL_ERROR).append(WRONG_AGE_ERROR);

    private static Validation<ErrorMessage, String> validateName(final String name) {
        final int length = name.length();
        if (length >= 1 && length <= 50) {
            return success(name);
        } else {
            return failure(WRONG_NAME_ERROR);
        }
    }

    private static Validation<ErrorMessage, String> validateEmail(final String email) {
        if (email.contains("@")) {
            return success(email);
        } else {
            return failure(WRONG_EMAIL_ERROR);
        }
    }

    private static Validation<ErrorMessage, Integer> validateAge(final Integer age) {
        if (age >= 0 && age <= 120) {
            return success(age);
        } else {
            return failure(WRONG_AGE_ERROR);
        }
    }

    private static Validation<ErrorMessage, Person> validatePerson(final String name,
                                                                   final String email,
                                                                   final Integer age) {
        return ap(ap(validateName(name).map(Person.cons), validateEmail(email)), validateAge(age));
    }

    @Test
    public void shouldFailForWrongName() {
        final Validation<ErrorMessage, Person> actual = validatePerson("", "wrong@name.com", 1);

        assertTrue(actual.isFailure());
        assertEquals(WRONG_NAME_ERROR, actual.getError());
    }

    @Test
    public void shouldFailForWrongEmail() {
        final Validation<ErrorMessage, Person> actual = validatePerson("WrongEmail", "wrongemail.com",2);

        assertTrue(actual.isFailure());
        assertEquals(WRONG_EMAIL_ERROR, actual.getError());
    }

    @Test
    public void shouldFailForWrongAge() {
        final Validation<ErrorMessage, Person> actual = validatePerson("Wrong", "wrong@age.com", 121);
        assertTrue(actual.isFailure());
        assertEquals(WRONG_AGE_ERROR, actual.getError());
    }

    @Test
    public void shouldFailForWrongNameEmailAge() {
        final Validation<ErrorMessage, Person> actual = validatePerson("", "all wrong!", 121);
        assertTrue(actual.isFailure());
        assertEquals(WRONG_ALL_ERROR, actual.getError());
    }

    @Test
    public void shouldBeSuccessful() {
        final Person person = new Person("Foo", "foo@bar.com", 64);

        final Validation<ErrorMessage, Person> actual = validatePerson(person.name, person.email, person.age);
        assertTrue(actual.isSuccess());
        assertEquals(person, actual.getValue());
    }
}
