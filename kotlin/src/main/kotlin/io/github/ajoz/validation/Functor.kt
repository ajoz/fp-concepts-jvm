package io.github.ajoz.validation

interface Functor<out A> {
    fun <B> map(func: (A) -> B): Functor<B>
}