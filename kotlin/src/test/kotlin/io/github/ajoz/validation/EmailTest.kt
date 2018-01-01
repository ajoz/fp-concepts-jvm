package io.github.ajoz.validation

import io.github.ajoz.validation.EmailError.*
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailTest {

    @Test
    fun shouldFailForMissingAt() {
        // <*
        val v1 = email("bobgmail.com")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_AT, v1.error)

        // *>
        val v2 = email2("bobgmail.com")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_AT, v2.error)
    }

    @Test
    fun shouldFailForMissingPeriod() {
        // <*
        val v1 = email("bob@gmailcom")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_PERIOD, v1.error)

        // *>
        val v2 = email2("bob@gmailcom")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_PERIOD, v2.error)
    }

    @Test
    fun shouldFailForEmpty() {
        // <*
        val v1 = email("")
        assertTrue(v1.isFailure())
        assertEquals(ALL_MISSING, v1.error)

        // *>
        val v2 = email2("")
        assertTrue(v2.isFailure())
        assertEquals(ALL_MISSING, v2.error)
    }

    @Test
    fun shouldFailForMissingPeriodAndAt() {
        // <*
        val v1 = email("bobgmailcom")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_AT_PERIOD, v1.error)

        // *>
        val v2 = email2("bobgmailcom")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_AT_PERIOD, v2.error)
    }

    @Test
    fun shouldBeSuccessful() {
        val emailAddress = "bob@gmail.com"
        // <*
        val v1 = email(emailAddress)
        assertTrue(v1.isSuccess())
        assertEquals(emailAddress, v1.value)

        // *>
        val v2 = email2(emailAddress)
        assertTrue(v2.isSuccess())
        assertEquals(emailAddress, v2.value)
    }

    companion object {
        private val MUST_CONTAIN_AT = NonEmptyList(MustContainAt)
        private val MUST_CONTAIN_PERIOD = NonEmptyList(MustContainPeriod)
        private val MUST_NOT_BE_EMPTY = NonEmptyList(MustNotBeEmpty)
        private val MUST_CONTAIN_AT_PERIOD = MUST_CONTAIN_AT.append(MUST_CONTAIN_PERIOD)
        private val ALL_MISSING = MUST_NOT_BE_EMPTY.append(MUST_CONTAIN_AT).append(MUST_CONTAIN_PERIOD)

        private val atString: (String) -> Validation<NonEmptyList<EmailError>, String> = { email ->
            if (email.contains("@"))
                Success(email)
            else
                Failure(MUST_CONTAIN_AT)

        }

        private val periodString: (String) -> Validation<NonEmptyList<EmailError>, String> = { email ->
            if (email.contains(".")) {
                Success(email)
            } else {
                Failure(MUST_CONTAIN_PERIOD)
            }
        }

        private val nonEmptyString: (String) -> Validation<NonEmptyList<EmailError>, String> = { email ->
            if (email.isNotEmpty()) {
                Success(email)
            } else {
                Failure(MUST_NOT_BE_EMPTY)
            }
        }

        // using apLeft <*
        private val email: (String) -> Validation<NonEmptyList<EmailError>, String> = { email ->
            Success<NonEmptyList<EmailError>, String>(email)
                    .apLeft(nonEmptyString(email))
                    .apLeft(atString(email))
                    .apLeft(periodString(email))
        }

        // using apRight *>
        private val email2: (String) -> Validation<NonEmptyList<EmailError>, String> = { email ->
            nonEmptyString(email)
                    .apRight(atString(email))
                    .apRight(periodString(email))
                    .apRight(Success(email))
        }
    }
}
