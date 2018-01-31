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
    fun `should flatMap a Success to a Success`() {
        val string = "Test"
        val success = ValidString(string)

        val actual = success.flatMap { s -> ValidInt(s.length) }

        assertTrue(actual.isSuccess())
        assertEquals(string.length, actual.value)
    }

    @Test
    fun `should flatMap a Success to a Failure`() {
        val errors = NonEmptyList("Error!")
        val string = "Test"
        val success = ValidString(string)

        val actual = success.flatMap { _ -> InvalidInt(errors) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `should not flatMap a Failure to a Success`() {
        val errors = NonEmptyList("Error!")
        val failure = InvalidString(errors)

        val actual = failure.flatMap { s -> ValidInt(s.length) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `should not flatMap a Failure to a Failure`() {
        val errors = NonEmptyList("Error!")
        val failure = InvalidString(errors)

        val actual = failure.flatMap { s -> InvalidInt(errors) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `ap of Success and Failure should return a Failure`() {
        val error = NonEmptyList("Error!")
        val function = {s: String -> s.length}
        val success = Success<NonEmptyList<String>, (String) -> Int>(function)
        val failure = InvalidString(error)

        val actual = success ap failure

        assertTrue(actual.isFailure())
        assertEquals(error, actual.error)
    }

    @Test
    fun `ap of Failure and Success should return a Failure`() {
        val error = NonEmptyList("Error!")
        val string = "valid"
        val failure = Failure<NonEmptyList<String>, (String) -> Int>(error)
        val success = ValidString(string)

        val actual = failure ap success

        assertTrue(actual.isFailure())
        assertEquals(error, actual.error)
    }

    @Test
    fun `ap of Failure and Failure should return a Failure with combined errors`() {
        val error1 = NonEmptyList("Error1")
        val error2 = NonEmptyList("Error2")

        val expected = error1.append(error2)

        val failure1 = Failure<NonEmptyList<String>, (String) -> Int>(error1)
        val failure2 = Failure<NonEmptyList<String>, String>(error2)

        val actual = failure1 ap failure2

        assertTrue(actual.isFailure())
        assertEquals(expected, actual.error)
    }

    @Test
    fun `ap of a Success and Success should return a Success with applied value`() {
        val function = {s: String -> s.length}
        val string = "valid"
        val success1 = Success<NonEmptyList<String>, (String) -> Int>(function)
        val success2 = Success<NonEmptyList<String>, String>(string)

        val actual = success1 ap success2

        assertTrue(actual.isSuccess())
        assertEquals(string.length, actual.value)
    }
}