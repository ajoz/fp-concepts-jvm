package io.github.ajoz

import org.junit.Assert.assertTrue
import org.junit.Test

class SemigroupTest {
    /**
     * String as a semigroup.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    data class StringS(val value: String): Semigroup<StringS> {
        override fun append(other: StringS) = StringS(value + other.value)
    }

    @Test
    fun `should fulfill associativity law`() {
        val string1 = "string1"
        val stringS1 = StringS(string1)

        val string2 = "string2"
        val stringS2 = StringS(string2)

        val string3 = "string3"
        val stringS3 = StringS(string3)

        assertTrue(associativity(stringS1, stringS2, stringS3))
    }
}