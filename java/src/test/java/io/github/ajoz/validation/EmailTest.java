package io.github.ajoz.validation;

import io.github.ajoz.util.NonEmptyList;
import static io.github.ajoz.util.NonEmptyList.nel;
import static io.github.ajoz.validation.Validation.failure;
import static io.github.ajoz.validation.Validation.success;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class EmailTest {

    enum EmailError {
        MustNotBeEmpty,
        MustContainAt,
        MustContainPeriod
    }

    private static final NonEmptyList<EmailError> MUST_CONTAIN_AT = nel(EmailError.MustContainAt);
    private static final NonEmptyList<EmailError> MUST_CONTAIN_PERIOD = nel(EmailError.MustContainPeriod);
    private static final NonEmptyList<EmailError> MUST_NOT_BE_EMPTY = nel(EmailError.MustNotBeEmpty);
    private static final NonEmptyList<EmailError> MUST_CONTAIN_AT_PERIOD = MUST_CONTAIN_AT.append(MUST_CONTAIN_PERIOD);
    private static final NonEmptyList<EmailError> ALL_MISSING = MUST_NOT_BE_EMPTY.append(MUST_CONTAIN_AT).append(MUST_CONTAIN_PERIOD);

    private static Validation<NonEmptyList<EmailError>, String> validateAt(final String email) {
        if (email.contains("@")) {
            return success(email);
        } else {
            return failure(MUST_CONTAIN_AT);
        }
    }

    private static Validation<NonEmptyList<EmailError>, String> validatePeriod(final String email) {
        if (email.contains(".")) {
            return success(email);
        } else {
            return failure(MUST_CONTAIN_PERIOD);
        }
    }

    private static Validation<NonEmptyList<EmailError>, String> validateNonEmpty(final String email) {
        if (null != email && email.length() > 0) {
            return success(email);
        } else {
            return failure(MUST_NOT_BE_EMPTY);
        }
    }

    // using apLeft <*
    private static Validation<NonEmptyList<EmailError>, String> validateEmailApLeft(final String email) {
        return Validation.<NonEmptyList<EmailError>, String>success(email)
                .apLeft(validateNonEmpty(email))
                .apLeft(validateAt(email))
                .apLeft(validatePeriod(email));
    }

    // using apRight *>
    private static Validation<NonEmptyList<EmailError>, String> validateEmailApRight(final String email) {
        return validateNonEmpty(email)
                .apRight(validateAt(email))
                .apRight(validatePeriod(email))
                .apRight(success(email));
    }

    @Test
    public void shouldFailForMissingAt() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = validateEmailApLeft("bobgmail.com");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_AT, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = validateEmailApRight("bobgmail.com");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_AT, v2.getError());
    }

    @Test
    public void shouldFailForMissingPeriod() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = validateEmailApLeft("bob@gmailcom");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_PERIOD, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = validateEmailApRight("bob@gmailcom");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_PERIOD, v2.getError());
    }

    @Test
    public void shouldFailForEmpty() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = validateEmailApLeft("");
        assertTrue(v1.isFailure());
        assertEquals(ALL_MISSING, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = validateEmailApRight("");
        assertTrue(v2.isFailure());
        assertEquals(ALL_MISSING, v2.getError());
    }

    @Test
    public void shouldFailForMissingPeriodAndAt() {
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = validateEmailApLeft("bobgmailcom");
        assertTrue(v1.isFailure());
        assertEquals(MUST_CONTAIN_AT_PERIOD, v1.getError());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = validateEmailApRight("bobgmailcom");
        assertTrue(v2.isFailure());
        assertEquals(MUST_CONTAIN_AT_PERIOD, v2.getError());
    }

    @Test
    public void shouldBeSuccessful() {
        final String emailAddress = "bob@gmail.com";
        // <*
        final Validation<NonEmptyList<EmailError>, String> v1 = validateEmailApLeft(emailAddress);
        assertTrue(v1.isSuccess());
        assertEquals(emailAddress, v1.getValue());

        // *>
        final Validation<NonEmptyList<EmailError>, String> v2 = validateEmailApRight(emailAddress);
        assertTrue(v2.isSuccess());
        assertEquals(emailAddress, v2.getValue());
    }
}
