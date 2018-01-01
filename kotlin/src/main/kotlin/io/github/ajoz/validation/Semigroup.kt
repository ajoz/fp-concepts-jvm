package io.github.ajoz.validation

interface Semigroup<T : Semigroup<T>> {
    fun append(item: T): T
}