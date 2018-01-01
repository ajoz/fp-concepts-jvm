package io.github.ajoz.validation

import io.github.ajoz.constant
import io.github.ajoz.identity
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success

sealed class Validation<E : Semigroup<E>, A> : Functor<A> {
    open val value: A
        get() = throw NoSuchElementException("")

    open val error: E
        get() = throw NoSuchElementException("")

    abstract fun isSuccess(): Boolean
    fun isFailure() = !isSuccess()

    override abstract infix fun <B> map(func: (A) -> B): Validation<E, B>

    infix fun <B> apLeft(other: Validation<E, B>) =
            map(constant<A, B>()) ap other

    infix fun <B> apRight(other: Validation<E, B>) =
            mapConst<(B) -> B, A, E>(identity())(this) ap other

    data class Success<E : Semigroup<E>, A>(override val value: A) : Validation<E, A>() {
        override fun isSuccess() = true
        override fun <B> map(func: (A) -> B) = Success<E, B>(func(value))
    }

    data class Failure<E : Semigroup<E>, A>(override val error: E) : Validation<E, A>() {
        override fun isSuccess() = false
        override fun <B> map(func: (A) -> B) = Failure<E, B>(error)
    }
    }

fun <A, B, E : Semigroup<E>> mapConst(a: A): (Validation<E, B>) -> Validation<E, A> = { v ->
    v.map(constant(a))
}

// smart casts and when expression for the win!
infix fun <E : Semigroup<E>, A, B> Validation<E, (A) -> B>.ap(other: Validation<E, A>): Validation<E, B> =
        when (this) {
            is Failure -> when (other) {
                is Failure -> Failure(error.append(other.error))
                is Success -> Failure(error)
            }
            is Success -> when (other) {
                is Failure -> Failure(other.error)
                is Success -> Success(value(other.value))
            }
        }