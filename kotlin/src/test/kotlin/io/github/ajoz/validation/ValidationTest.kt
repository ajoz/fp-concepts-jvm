package io.github.ajoz.validation

import io.github.ajoz.NonEmptyList
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.*
import org.junit.Test

typealias ValidString = Success<NonEmptyList<String>, String>
typealias InvalidString = Failure<NonEmptyList<String>, String>

typealias ValidInt = Success<NonEmptyList<String>, Int>
typealias InvalidInt = Failure<NonEmptyList<String>, Int>

class ValidationTest {
    @Test(expected = NoSuchElementException::class)
    fun `error property should throw NoSuchElementException for a Success`() {
        val success = ValidString("Test")
        success.error
    }

    @Test(expected = NoSuchElementException::class)
    fun `value property should throw NoSuchElementException for a Failure`() {
        val failure = InvalidString(NonEmptyList("Error!"))
        failure.value
    }

    @Test
    fun `isSuccess should return true for a Success`() {
        val success = ValidString("Test")
        assertTrue(success.isSuccess())
    }

    @Test
    fun `isFailure should return false for a Success`() {
        val success = ValidString("Test")
        assertFalse(success.isFailure())
    }

    @Test
    fun `isFailure should return true for a Failure`() {
        val failure = InvalidString(NonEmptyList("Error!"))
        assertTrue(failure.isFailure())
    }

    @Test
    fun `isSuccess should return false for a Failure`() {
        val failure = InvalidString(NonEmptyList("Error!"))
        assertFalse(failure.isSuccess())
    }

    @Test
    fun `should map over a Success`() {
        val string  = "Test"
        val success = ValidString(string)

        val actual = success.map { s -> s.length }.value

        assertEquals(string.length, actual)
    }

    @Test
    fun `should not map over a Failure`() {
        val errors = NonEmptyList("Error!")
        val failure = InvalidString(errors)

        val actual = failure.map { s -> s.length }.error

        assertEquals(errors, actual)
    }

    @Test
    fun `should flatMap a Success`() {
        val string = "Test"
        val success = ValidString(string)

        val actual = success.flatMap { s -> ValidInt(s.length) }

        assertEquals(string.length, actual.value)
    }
}