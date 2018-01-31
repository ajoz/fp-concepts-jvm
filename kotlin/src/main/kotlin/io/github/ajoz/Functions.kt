package io.github.ajoz

@Suppress("UNUSED_PARAMETER")
fun <A, B> constant(a: A, b: B): A = a

fun <A, B> constant(): (A) -> (B) -> A =
        curry<A, B, A>(::constant)

fun <A, B> constant(a: A): (B) -> A =
        curry<A, B, A>(::constant)(a)

fun <A> identity(a: A): A = a

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