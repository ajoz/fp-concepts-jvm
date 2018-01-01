package io.github.ajoz.validation

import io.github.ajoz.validation.Validation.Success
import org.junit.Assert
import org.junit.Test

import java.util.function.Function

class SimpleTest {

    @Test
    fun shouldSumTwoValues() {
        val first = Success<ErrorMessage, (Int) -> (Int) -> Int>(plus)
        val second = Success<ErrorMessage, Int>(1)
        val third = Success<ErrorMessage, Int>(2)

        val combined = first ap second ap third
        val value = combined.value
        Assert.assertEquals(3, value.toLong())
    }

    companion object {
        private val plus: (Int) -> (Int) -> Int =
                { first -> { second -> first + second } }
    }
}
