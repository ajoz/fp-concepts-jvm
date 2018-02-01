package io.github.ajoz.validation

import io.github.ajoz.NonEmptyList

import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.*
import org.junit.Test

typealias StrErr = NonEmptyList<String>

typealias ValidString = Success<StrErr, String>
typealias InvalidString = Failure<StrErr, String>

typealias ValidInt = Success<StrErr, Int>
typealias InvalidInt = Failure<StrErr, Int>

class ValidationTest {
    @Test(expected = NoSuchElementException::class)
    fun `error property should throw NoSuchElementException for a Success`() {
        val sut = ValidString("Test")
        sut.error
    }

    @Test(expected = NoSuchElementException::class)
    fun `value property should throw NoSuchElementException for a Failure`() {
        val sut = InvalidString(StrErr("Error!"))
        sut.value
    }

    @Test
    fun `isSuccess should return true for a Success`() {
        val sut = ValidString("Test")
        assertTrue(sut.isSuccess())
    }

    @Test
    fun `isFailure should return false for a Success`() {
        val sut = ValidString("Test")
        assertFalse(sut.isFailure())
    }

    @Test
    fun `isFailure should return true for a Failure`() {
        val sut = InvalidString(StrErr("Error!"))
        assertTrue(sut.isFailure())
    }

    @Test
    fun `isSuccess should return false for a Failure`() {
        val sut = InvalidString(StrErr("Error!"))
        assertFalse(sut.isSuccess())
    }

    @Test
    fun `should map over a Success`() {
        val string = "Test"
        val sut = ValidString(string)

        val actual = sut.map { s -> s.length }.value

        assertEquals(string.length, actual)
    }

    @Test
    fun `should not map over a Failure`() {
        val errors = StrErr("Error!")
        val sut = InvalidString(errors)

        val actual = sut.map { s -> s.length }.error

        assertEquals(errors, actual)
    }

    @Test
    fun `should flatMap a Success to a Success`() {
        val string = "Test"
        val sut = ValidString(string)

        val actual = sut.flatMap { s -> ValidInt(s.length) }

        assertTrue(actual.isSuccess())
        assertEquals(string.length, actual.value)
    }

    @Test
    fun `should flatMap a Success to a Failure`() {
        val errors = StrErr("Error!")
        val string = "Test"
        val sut = ValidString(string)

        val actual = sut.flatMap { _ -> InvalidInt(errors) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `should not flatMap a Failure to a Success`() {
        val errors = StrErr("Error!")
        val sut = InvalidString(errors)

        val actual = sut.flatMap { s -> ValidInt(s.length) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `should not flatMap a Failure to a Failure`() {
        val errors = StrErr("Error!")
        val sut = InvalidString(errors)

        val actual = sut.flatMap { s -> InvalidInt(errors) }

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `ap of Success and Failure should return a Failure`() {
        val error = StrErr("Error!")
        val function = { s: String -> s.length }
        val success = Success<StrErr, (String) -> Int>(function)
        val failure = InvalidString(error)

        val actual = success ap failure

        assertTrue(actual.isFailure())
        assertEquals(error, actual.error)
    }

    @Test
    fun `ap of Failure and Success should return a Failure`() {
        val error = StrErr("Error!")
        val string = "valid"
        val failure = Failure<StrErr, (String) -> Int>(error)
        val success = ValidString(string)

        val actual = failure ap success

        assertTrue(actual.isFailure())
        assertEquals(error, actual.error)
    }

    @Test
    fun `ap of Failure and Failure should return a Failure with combined errors`() {
        val error1 = StrErr("Error1")
        val error2 = StrErr("Error2")

        val expected = error1.append(error2)

        val failure1 = Failure<StrErr, (String) -> Int>(error1)
        val failure2 = Failure<StrErr, String>(error2)

        val actual = failure1 ap failure2

        assertTrue(actual.isFailure())
        assertEquals(expected, actual.error)
    }

    @Test
    fun `ap of a Success and Success should return a Success with applied value`() {
        val function = { s: String -> s.length }
        val string = "valid"
        val success1 = Success<StrErr, (String) -> Int>(function)
        val success2 = Success<StrErr, String>(string)

        val actual = success1 ap success2

        assertTrue(actual.isSuccess())
        assertEquals(string.length, actual.value)
    }

    @Test
    fun `should map const over a Success`() {
        val string = "valid"
        val value = string.length
        val success = ValidString(string)

        val actual = mapConst<StrErr, Int, String>(value)(success)

        assertTrue(actual.isSuccess())
        assertEquals(value, actual.value)
    }

    @Test
    fun `should not map const over a Failure`() {
        val errors = StrErr("Error!")
        val failure = InvalidString(errors)

        val actual = mapConst<StrErr, Int, String>(5)(failure)

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `Success1 apRight Success2 should equal Success2`() {
        // resulting validation should have the same type as Success2
        // Success err a *> Success err b == Success err b
        val string1 = "valid"
        val string2 = "valid-er"

        val success1 = ValidInt(string1.length)
        val success2 = ValidString(string2)

        val actual = success1 apRight success2

        assertTrue(actual.isSuccess())
        assertEquals(success2, actual)
    }

    @Test
    fun `Failure apRight Success should equal another Failure`() {
        // the error will stay the same but the type will be of Success
        // Failure err a *> Success err b == Failure err b
        val errors = StrErr("Error!")
        val string = "valid"

        val failure = InvalidString(errors)
        val success = ValidInt(string.length)

        val actual = failure apRight success

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `Success apRight Failure should equal another Failure`() {
        // the error will stay the same and type will stay the same
        // Success err a *> Failure err b == Failure err b
        val errors = StrErr("Error!")
        val string = "valid"

        val success = ValidString(string)
        val failure = InvalidInt(errors)

        val actual = success apRight failure

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `Failure1 apRight Failure2 should equal another Failure3`() {
        // errors are combined but the type will be of Failure2
        // err is defined as a Semigroup so it can be appended
        // Failure err a *> Failure err b == Failure (err + err) b
        val error1 = StrErr("Error1")
        val error2 = StrErr("Error2")

        val expected = error1.append(error2)

        val failure1 = InvalidInt(error1)
        val failure2 = InvalidString(error2)

        val actual = failure1 apRight failure2

        assertTrue(actual.isFailure())
        assertEquals(expected, actual.error)
    }

    @Test
    fun `Success1 apLeft Success2 should equal Success1`() {
        // resulting validation should have the same type as Success1
        // Success err a <* Success err b == Success err a
        val string1 = "valid"
        val string2 = "valid-er"

        val success1 = ValidInt(string1.length)
        val success2 = ValidString(string2)

        val actual = success1 apLeft success2

        assertTrue(actual.isSuccess())
        assertEquals(success1, actual)
    }

    @Test
    fun `Failure apLeft Success should equal another Failure`() {
        // the error and type will stay the same as the Failure
        // Failure err a <* Success err b == Failure err a
        val errors = StrErr("Error!")
        val string = "valid"

        val failure = InvalidString(errors)
        val success = ValidInt(string.length)

        val actual = failure apLeft success

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `Success apLeft Failure should equal another Failure`() {
        // the error will stay the same but the type will be of Success
        // Success err a <* Failure err b == Failure err a
        val errors = StrErr("Error!")
        val string = "valid"

        val success = ValidString(string)
        val failure = InvalidInt(errors)

        val actual = success apLeft failure

        assertTrue(actual.isFailure())
        assertEquals(errors, actual.error)
    }

    @Test
    fun `Failure1 apLeft Failure2 should equal another Failure3`() {
        // errors are combined but the type will be of Failure1
        // err is defined as a Semigroup so it can be appended
        // Failure err a <* Failure err b == Failure (err + err) a
        val error1 = StrErr("Error1")
        val error2 = StrErr("Error2")

        val expected = error1.append(error2)

        val failure1 = InvalidInt(error1)
        val failure2 = InvalidString(error2)

        val actual = failure1 apLeft failure2

        assertTrue(actual.isFailure())
        assertEquals(expected, actual.error)
    }
}