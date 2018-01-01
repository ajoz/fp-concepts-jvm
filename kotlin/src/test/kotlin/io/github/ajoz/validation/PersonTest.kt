package io.github.ajoz.validation

import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PersonTest {

    @Test
    fun shouldFailForWrongName() {
        val actual = validatePerson("", "wrong@name.com", 1)

        assertTrue(actual.isFailure())
        assertEquals(WRONG_NAME_ERROR, actual.error)
    }

    @Test
    fun shouldFailForWrongEmail() {
        val actual = validatePerson("WrongEmail", "wrongemail.com", 2)

        assertTrue(actual.isFailure())
        assertEquals(WRONG_EMAIL_ERROR, actual.error)
    }

    @Test
    fun shouldFailForWrongAge() {
        val actual = validatePerson("Wrong", "wrong@age.com", 121)
        assertTrue(actual.isFailure())
        assertEquals(WRONG_AGE_ERROR, actual.error)
    }

    @Test
    fun shouldFailForWrongNameEmailAge() {
        val actual = validatePerson("", "all wrong!", 121)
        assertTrue(actual.isFailure())
        assertEquals(WRONG_ALL_ERROR, actual.error)
    }

    @Test
    fun shouldBeSuccessful() {
        val person = Person("Foo", "foo@bar.com", 64)

        val actual = validatePerson(person.name, person.email, person.age)
        assertTrue(actual.isSuccess())
        assertEquals(person, actual.value)
    }

    companion object {
        private val WRONG_NAME_ERROR = ErrorMessage("Name length not between 1 and 50 characters")
        private val WRONG_EMAIL_ERROR = ErrorMessage("Email does not have @ sign")
        private val WRONG_AGE_ERROR = ErrorMessage("Age not between 0 and 120")

        private val WRONG_ALL_ERROR = WRONG_NAME_ERROR.append(WRONG_EMAIL_ERROR).append(WRONG_AGE_ERROR)

        private val consPerson: (String) -> (String) -> (Int) -> Person =
                { name -> { email -> { age -> Person(name, email, age) } } }

        private val mkName: (String) -> Validation<ErrorMessage, String> = { name ->
            if (name.length in 1..50) {
                Success(name)
            } else {
                Failure(WRONG_NAME_ERROR)
            }
        }

        private val mkEmail: (String) -> Validation<ErrorMessage, String> = { email ->
            if (email.contains("@")) {
                Success(email)
            } else {
                Failure(WRONG_EMAIL_ERROR)
            }
        }

        private val mkAge: (Int) -> Validation<ErrorMessage, Int> = { age ->
            if (age in 0..120) {
                Success(age)
            } else {
                Failure(WRONG_AGE_ERROR)
            }
        }

        private val mkPerson: (String) -> (String) -> (Int) -> Validation<ErrorMessage, Person> = { pName ->
            { pEmail ->
                { pAge ->
                    mkName(pName) map consPerson ap mkEmail(pEmail) ap mkAge(pAge)
                }
            }
        }

        private fun validatePerson(name: String, email: String, age: Int) =
                mkPerson(name)(email)(age)
    }
}
