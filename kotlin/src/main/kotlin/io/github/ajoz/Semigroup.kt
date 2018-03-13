package io.github.ajoz

interface Semigroup<T : Semigroup<T>> {
    infix fun append(other: T): T
}

/**
 * Checks if the Semigroup fulfills associativity law.
 */
fun <T : Semigroup<T>> associativity(a: T, b: T, c: T): Boolean =
        (a append b) append c == a append (b append c)