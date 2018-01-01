package io.github.ajoz.validation;

import static io.github.ajoz.validation.EmailError.MustContainAt;
import static io.github.ajoz.validation.EmailError.MustContainPeriod;
import static io.github.ajoz.validation.EmailError.MustNotBeEmpty;
import static io.github.ajoz.validation.NonEmptyList.nel;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.util.function.Function;

public class EmailTest {

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> atString =
            email -> {
                if (email.contains("@")) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(nel(MustContainAt));
                }
            };

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> periodString =
            email -> {
                if (email.contains(".")) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(nel(MustContainPeriod));
                }
            };

    private static final Function<String, Validation<NonEmptyList<EmailError>, String>> nonEmptyString =
            email -> {
                if (null != email && email.length() > 0) {
                    return Validation.success(email);
                } else {
                    return Validation.failure(nel(MustNotBeEmpty));
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
        final Validation<NonEmptyList<EmailError>, String> v1 = email.apply("bobgmail.com");
        assertTrue(v1.isFailure());

        final Validation<NonEmptyList<EmailError>, String> v2 = email2.apply("bobgmail.com");
        assertTrue(v2.isFailure());
    }


}
