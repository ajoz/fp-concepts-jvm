package io.github.ajoz.validation

import io.github.ajoz.Semigroup
import io.github.ajoz.curry
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

typealias Name = String
typealias ValidName = Success<ErrorMessage, Name>
typealias InvalidName = Failure<ErrorMessage, Name>

typealias Mail = String
typealias ValidMail = Success<ErrorMessage, Mail>
typealias InvalidMail = Failure<ErrorMessage, Mail>

typealias Age = Int
typealias ValidAge = Success<ErrorMessage, Age>
typealias InvalidAge = Failure<ErrorMessage, Age>

data class Person(val name: String, val email: String, val age: Int)

@Suppress("MemberVisibilityCanBePrivate")
data class ErrorMessage(val message: String) : Semigroup<ErrorMessage> {
    override fun append(item: ErrorMessage): ErrorMessage {
        return ErrorMessage(this.message + "\n" + item.message)
    }
}

class PersonTest {

    @Test
    fun `should return a Failure for a wrong name`() {
        val actual = validatePerson("", "wrong@name.com", 1)

        assertTrue(actual.isFailure())
        assertEquals(WRONG_NAME_ERROR, actual.error)
    }

    @Test
    fun `should return a Failure for a wrong email`() {
        val actual = validatePerson("WrongEmail", "wrongemail.com", 2)

        assertTrue(actual.isFailure())
        assertEquals(WRONG_EMAIL_ERROR, actual.error)
    }

    @Test
    fun `should return a Failure for a wrong age`() {
        val actual = validatePerson("Wrong", "wrong@age.com", 121)
        assertTrue(actual.isFailure())
        assertEquals(WRONG_AGE_ERROR, actual.error)
    }

    @Test
    fun `should return a Failure for wrong name and email and age`() {
        val actual = validatePerson("", "all wrong!", 121)
        assertTrue(actual.isFailure())
        assertEquals(WRONG_ALL_ERROR, actual.error)
    }

    @Test
    fun `should return a Success for correct Person data`() {
        val person = Person("Foo", "foo@bar.com", 64)

        val actual = validatePerson(person.name, person.email, person.age)
        assertTrue(actual.isSuccess())
        assertEquals(person, actual.value)
    }

    @Suppress("MemberVisibilityCanPrivate")
    companion object {
        val WRONG_NAME_ERROR = ErrorMessage("Name length not between 1 and 50 characters")
        val WRONG_EMAIL_ERROR = ErrorMessage("Email does not have @ sign")
        val WRONG_AGE_ERROR = ErrorMessage("Age not between 0 and 120")
        val WRONG_ALL_ERROR = WRONG_NAME_ERROR append WRONG_EMAIL_ERROR append WRONG_AGE_ERROR

        private fun validateName(name: Name) =
                if (name.length in 1..50)
                    ValidName(name)
                else
                    InvalidName(WRONG_NAME_ERROR)

        private fun validateEmail(email: Email) =
                if (email.contains("@"))
                    ValidMail(email)
                else
                    InvalidMail(WRONG_EMAIL_ERROR)

        private fun validateAge(age: Age) =
                if (age in 0..120)
                    ValidAge(age)
                else
                    InvalidAge(WRONG_AGE_ERROR)

        // validatePerson:: Name -> Email -> Age -> Validation [String] Person
        // validatePerson name email age = Person <$> (validateName name) <*> (validateEmail email) <*> (validateAge age)
        fun validatePerson(name: Name, email: Email, age: Age) =
                validateName(name) map curry(::Person) ap validateEmail(email) ap validateAge(age)
    }
}
