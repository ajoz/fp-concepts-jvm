package io.github.ajoz

interface Functor<out A> {
    infix fun <B> map(func: (A) -> B): Functor<B>
}