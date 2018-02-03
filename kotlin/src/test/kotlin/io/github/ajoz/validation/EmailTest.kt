package io.github.ajoz.validation

import io.github.ajoz.NonEmptyList
import io.github.ajoz.validation.EmailError.MustContainAt
import io.github.ajoz.validation.EmailError.MustContainPeriod
import io.github.ajoz.validation.EmailError.MustNotBeEmpty
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

// We can define a set of possible Email processing errors, this allows us to
// have a better control over the passed values in contrast to the example
// that was using "string semigroup"
enum class EmailError {
    MustNotBeEmpty,
    MustContainAt,
    MustContainPeriod
}

typealias Email = String
typealias EmailErrors = NonEmptyList<EmailError>

typealias ValidEmail = Success<EmailErrors, Email>
typealias InvalidEmail = Failure<EmailErrors, Email>

class EmailTest {

    @Test
    fun `should return a Failure for missing @`() {
        // <*
        val v1 = validateEmailApLeft("bobgmail.com")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_AT, v1.error)

        // *>
        val v2 = validateEmailApRight("bobgmail.com")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_AT, v2.error)
    }

    @Test
    fun `should return a Failure for missing period`() {
        // <*
        val v1 = validateEmailApLeft("bob@gmailcom")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_PERIOD, v1.error)

        // *>
        val v2 = validateEmailApRight("bob@gmailcom")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_PERIOD, v2.error)
    }

    @Test
    fun `should return a Failure for empty email`() {
        // <*
        val v1 = validateEmailApLeft("")
        assertTrue(v1.isFailure())
        assertEquals(ALL_MISSING, v1.error)

        // *>
        val v2 = validateEmailApRight("")
        assertTrue(v2.isFailure())
        assertEquals(ALL_MISSING, v2.error)
    }

    @Test
    fun `should return a Failure for missing @ and period`() {
        // <*
        val v1 = validateEmailApLeft("bobgmailcom")
        assertTrue(v1.isFailure())
        assertEquals(MUST_CONTAIN_AT_PERIOD, v1.error)

        // *>
        val v2 = validateEmailApRight("bobgmailcom")
        assertTrue(v2.isFailure())
        assertEquals(MUST_CONTAIN_AT_PERIOD, v2.error)
    }

    @Test
    fun `should return a Success for correct email`() {
        val emailAddress = "bob@gmail.com"
        // <*
        val v1 = validateEmailApLeft(emailAddress)
        assertTrue(v1.isSuccess())
        assertEquals(emailAddress, v1.value)

        // *>
        val v2 = validateEmailApRight(emailAddress)
        assertTrue(v2.isSuccess())
        assertEquals(emailAddress, v2.value)
    }

    @Suppress("MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
    companion object {
        val MUST_CONTAIN_AT = EmailErrors(MustContainAt)
        val MUST_CONTAIN_PERIOD = EmailErrors(MustContainPeriod)
        val MUST_NOT_BE_EMPTY = EmailErrors(MustNotBeEmpty)
        val MUST_CONTAIN_AT_PERIOD = MUST_CONTAIN_AT append MUST_CONTAIN_PERIOD
        val ALL_MISSING = MUST_NOT_BE_EMPTY append MUST_CONTAIN_AT append MUST_CONTAIN_PERIOD

        private fun validateAt(email: Email) =
                if (email.contains("@"))
                    ValidEmail(email)
                else
                    InvalidEmail(MUST_CONTAIN_AT)

        private fun validatePeriod(email: Email) =
                if (email.contains("."))
                    ValidEmail(email)
                else
                    InvalidEmail(MUST_CONTAIN_PERIOD)

        private fun validateNonEmpty(email: Email) =
                if (email.isNotEmpty())
                    ValidEmail(email)
                else
                    InvalidEmail(MUST_NOT_BE_EMPTY)

        // using apLeft <*
        // validateEmailApLeft :: String -> Validation [String] Email
        // validateEmailApLeft x = pure (Email x) <*
        //                         (validateNonEmpty x) <*
        //                         (validateAt x) <*
        //                         (validatePeriod x)

        fun validateEmailApLeft(email: Email) =
                ValidEmail(email)
                        .apLeft(validateNonEmpty(email))
                        .apLeft(validateAt(email))
                        .apLeft(validatePeriod(email))

        // using apRight *>
        // validateEmailApRight :: String -> Validation [String] Email
        // validateEmailApRight x = (validateNonEmpty x) *>
        //                          (validateAt x) *>
        //                          (validatePeriod x) *>
        //                          pure (Email x)
        fun validateEmailApRight(email: Email) =
                validateNonEmpty(email)
                        .apRight(validateAt(email))
                        .apRight(validatePeriod(email))
                        .apRight(ValidEmail(email))
    }
}
