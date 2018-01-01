package io.github.ajoz.validation;

import static io.github.ajoz.validation.EmailError.MustContainAt;
import static io.github.ajoz.validation.EmailError.MustContainPeriod;
import static io.github.ajoz.validation.EmailError.MustNotBeEmpty;
import static io.github.ajoz.validation.NonEmptyList.nel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.function.Function;

public class EmailTest {

    private static final NonEmptyList<EmailError> MUST_CONTAIN_AT = nel(MustContainAt);
    private static final NonEmptyList<EmailError> MUST_CONTAIN_PERIOD = nel(MustContainPeriod);
    private static final NonEmptyList<EmailError> MUST_NOT_BE_EMPTY = nel(MustNotBeEmpty);
    private static final NonEmptyList<EmailError> MUST_CONTAIN_AT_PERIOD = MUST_CONTAIN_AT.append(MUST_CONTAIN_PERIOD);
    private static final NonEmptyList<EmailError> ALL_MISSING = MUST_NOT_BE_EMPTY.append(MUST_CONTAIN_AT).append(MUST_CONTAIN_PERIOD);

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> atString =
            email -> {
                if (email.contains("@")) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(MUST_CONTAIN_AT);
                }
            };

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> periodString =
            email -> {
                if (email.contains(".")) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(MUST_CONTAIN_PERIOD);
                }
            };

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> nonEmptyString =
            email -> {
                if (null != email && email.length() > 0) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(MUST_NOT_BE_EMPTY);
                }
            };

    // using apLeft <*
    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> email =
            email -> Validation.<NonEmptyList<EmailError>, String>success(email)
                    .apLeft(nonEmptyString.apply(email))
                    .apLeft(atString.apply(email))
                    .apLeft(periodString.apply(email));

    // using apRight *>
    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> email2 =
            email -> nonEmptyString.apply(email)
                    .apRight(atString.apply(email))
                    .apRight(periodString.apply(email))
                    .apRight(Validation.success(email));

    @Test
    public void shouldFailForMissingAt() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply("bobgmail.com");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_AT, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply("bobgmail.com");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_AT, v2.getError());
    }

    @Test
    public void shouldFailForMissingPeriod() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply("bob@gmailcom");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_PERIOD, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply("bob@gmailcom");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_PERIOD, v2.getError());
    }

    @Test
    public void shouldFailForEmpty() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply("");
        assertTrue(v1.isFailure());
        assertEquals(ALL_MISSING, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply("");
        assertTrue(v2.isFailure());
        assertEquals(ALL_MISSING, v2.getError());
    }

    @Test
    public void shouldFailForMissingPeriodAndAt() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply("bobgmailcom");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_AT_PERIOD, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply("bobgmailcom");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_AT_PERIOD, v2.getError());
    }

    @Test
    public void shouldBeSuccessful() {
        final String emailAddress = "bob@gmail.com";
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply(emailAddress);
        assertTrue(v1.isSuccess());
        assertEquals(emailAddress, v1.getValue());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply(emailAddress);
        assertTrue(v2.isSuccess());
        assertEquals(emailAddress, v2.getValue());
    }
}
