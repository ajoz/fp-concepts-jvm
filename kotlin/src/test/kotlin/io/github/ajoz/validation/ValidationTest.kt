package io.github.ajoz.validation

import io.github.ajoz.NonEmptyList
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.*
import org.junit.Test

typealias ValidString = Success<NonEmptyList<String>, String>
typealias InvalidString = Failure<NonEmptyList<String>, String>

class ValidationTest {
    @Test(expected = NoSuchElementException::class)
    fun errorPropShouldThrowExceptionForSuccess() {
        val success = ValidString("Test")
        success.error
    }

    @Test(expected = NoSuchElementException::class)
    fun valuePropShouldThrowExceptionForFailure() {
        val failure = InvalidString(NonEmptyList("Error!"))
        failure.value
    }

    @Test
    fun shouldReturnTrueForSuccess() {
        val success = ValidString("Test")
        assertTrue(success.isSuccess())
    }

    @Test
    fun shouldReturnFalseForSuccess() {
        val success = ValidString("Test")
        assertFalse(success.isFailure())
    }

    @Test
    fun shouldReturnTrueForFailure() {
        val failure = InvalidString(NonEmptyList("Error!"))
        assertTrue(failure.isFailure())
    }

    @Test
    fun shouldReturnFalseForFailure() {
        val failure = InvalidString(NonEmptyList("Error!"))
        assertFalse(failure.isSuccess())
    }

    @Test
    fun shouldMapASuccess() {
        val string  = "Test"
        val success = ValidString(string)

        val actual = success.map { s -> s.length }.value

        assertEquals(string.length, actual)
    }

    @Test
    fun shouldNotMapAFailure() {
        val errors = NonEmptyList("Error!")
        val failure = InvalidString(errors)

        val actual = failure.map { s -> s.length }.error

        assertEquals(errors, actual)
    }
}