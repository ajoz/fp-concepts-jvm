package io.github.ajoz

fun <A, B> constant(): (A) -> (B) -> A = { a ->
    { _ ->
        a
    }
}

fun <A, B> constant(a: A): (B) -> A = { _ ->
    a
}

fun <A> identity(): (A) -> A = { a ->
    a
}

fun <A, B, C> curry(func: (A, B) -> C): (A) -> (B) -> C =
        { a: A ->
            { b: B -> func(a, b) }
        }

fun <A, B, C, D> curry(func: (A, B, C) -> D): (A) -> (B) -> (C) -> D =
        { a: A ->
            { b: B ->
                { c: C -> func(a, b, c) }
            }
        }