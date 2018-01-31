package io.github.ajoz.validation

import io.github.ajoz.Functor
import io.github.ajoz.Semigroup
import io.github.ajoz.constant
import io.github.ajoz.identity
import io.github.ajoz.validation.Validation.Failure
import io.github.ajoz.validation.Validation.Success

sealed class Validation<E : Semigroup<E>, A> : Functor<A> {
    open val value: A
        get() = throw NoSuchElementException("No value element available!")

    open val error: E
        get() = throw NoSuchElementException("No error element available!")

    abstract fun isSuccess(): Boolean
    fun isFailure() = !isSuccess()

    abstract override fun <B> map(func: (A) -> B): Validation<E, B>

    abstract fun <B> flatMap(func: (A) -> Validation<E, B>): Validation<E, B>

    /* We would like to sequence actions, discarding the value of the first
       argument.

       

       apLeft :: Validation err a -> Validation err b -> Validation err a

     */
    infix fun <B> apLeft(other: Validation<E, B>): Validation<E, A> =
            this map constant<A, B>() ap other

    // apRight :: Validation err a -> Validation err b -> Validation err b
    infix fun <B> apRight(other: Validation<E, B>): Validation<E, B> =
            this map constant<(B) -> B, A>(::identity) ap other

    data class Success<E : Semigroup<E>, A>(override val value: A) : Validation<E, A>() {
        override fun isSuccess() = true
        override fun <B> map(func: (A) -> B) = Success<E, B>(func(value))
        override fun <B> flatMap(func: (A) -> Validation<E, B>) = func(value)
    }

    data class Failure<E : Semigroup<E>, A>(override val error: E) : Validation<E, A>() {
        override fun isSuccess() = false
        override fun <B> map(func: (A) -> B) = Failure<E, B>(error)
        override fun <B> flatMap(func: (A) -> Validation<E, B>) = Failure<E, B>(error)
    }
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