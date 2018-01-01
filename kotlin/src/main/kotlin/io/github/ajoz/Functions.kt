package io.github.ajoz

fun <A, B> constant(): (A) -> (B) -> A = { a ->
    { _ ->
        a
    }
}

fun <A, B> constant(a: A): (B) -> A = { _ ->
    a
}

fun <A> identity(): (A) -> A = {
    a -> a
}