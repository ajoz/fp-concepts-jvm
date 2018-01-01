package io.github.ajoz.validation

import io.github.ajoz.curry
import io.github.ajoz.validation.Validation.Success
import org.junit.Assert.assertEquals
import org.junit.Test

typealias IntOptSuccess = Success<ErrorMessage, (Int) -> (Int) -> Int>
typealias IntSuccess = Success<ErrorMessage, Int>

class SimpleTest {

    @Test
    fun shouldSumTwoValues() {
        val first = IntOptSuccess(curry(::plus))
        val second = IntSuccess(1)
        val third = IntSuccess(2)

        val combined = first ap second ap third
        val value = combined.value
        assertEquals(3, value.toLong())
    }
}

fun plus(a: Int, b: Int) = a + b