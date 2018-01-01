package io.github.ajoz.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.function.Function;

public class PersonTest {
    private final static ErrorMessage WRONG_NAME_ERROR = new ErrorMessage("Name length not between 1 and 50 characters");
    private final static ErrorMessage WRONG_EMAIL_ERROR = new ErrorMessage("Email does not have @ sign");
    private final static ErrorMessage WRONG_AGE_ERROR = new ErrorMessage("Age not between 0 and 120");

    private final static ErrorMessage WRONG_ALL_ERROR = WRONG_NAME_ERROR.append(WRONG_EMAIL_ERROR).append(WRONG_AGE_ERROR);

    public final static Function<String, Function<String, Function<Integer, Person>>> consPerson =
            name -> email -> age -> new Person(name, email, age);

    private final static Function<String, Validation<ErrorMessage, String>> mkName =
            name -> {
                final int length = name.length();
                if (length >= 1 && length <= 50) {
                    return Validation.success(name);
                } else {
                    return Validation.failure(WRONG_NAME_ERROR);
                }
            };

    private final static Function<String, Validation<ErrorMessage, String>> mkEmail =
            email -> {
                if (email.contains("@")) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(WRONG_EMAIL_ERROR);
                }
            };

    private final static Function<Integer, Validation<ErrorMessage, Integer>> mkAge =
            age -> {
                if (age >= 0 && age <= 120) {
                    return Validation.success(age);
                } else {
                    return Validation.failure(WRONG_AGE_ERROR);
                }
            };

    private final static Function<String, Function<String, Function<Integer, Validation<ErrorMessage, Person>>>> mkPerson =
            pName -> pEmail -> pAge -> {
                final Validation<ErrorMessage, Function<String, Function<Integer, Person>>> vName = mkName.apply(pName).map(consPerson);
                final Validation<ErrorMessage, Function<Integer, Person>> vEmail = Validation.ap(vName, mkEmail.apply(pEmail));
                final Validation<ErrorMessage, Person> vPerson = Validation.ap(vEmail, mkAge.apply(pAge));
                return vPerson;
            };

    private static Validation<ErrorMessage, Person> validatePerson(final String name,
                                                                   final String email,
                                                                   final Integer age) {
        return mkPerson.apply(name).apply(email).apply(age);
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
