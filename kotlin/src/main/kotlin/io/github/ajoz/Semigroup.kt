package io.github.ajoz

interface Semigroup<T : Semigroup<T>> {
    infix fun append(item: T): T
}

fun <T : Semigroup<T>> associativity(a: T, b: T, c: T): Boolean =
        (a append b) append c == a append (b append c)