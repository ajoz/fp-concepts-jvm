package io.github.ajoz.functions

// One of the talks that took place on Mobilization 2017 I enjoyed the most was
// a talk about FP in Kotlin by Jorge Castillo called "Functional approach to
// Android architecture using Kotlin"

// Everything was fine until I read the description of the presentation on
// realm.io, especially this part "Kotlin is arising as one of the most powerful
// functional languages".

// The "most powerful" part was the thing that made me think. Is it really that
// powerful and how one defines power?

// I cannot say for anyone else then me, but I think that conciseness of
// expressing intent behind the code is something of utmost importance. I know
// that there are extremes in everything. It's possible to write a one line
// program in APL or 50k line behemoth in Java and barely anyone would understand
// what is going on there.

// Kotlin advantages:
// - syntax is shorter then in Java
// - standalone functions
// - although functions are objects in disguise there is nice syntax for
// declaring them and invoking them

// Kotlin disadvantages:
// - functions are not curried by default

// with a function like this:

fun first(string: String, count: Int): String =
        string.take(count) //for the ease of implementation std lib is used

// I cannot invoke it with only one argument
// val firstInWord: (Int) -> String = first("Word")


// - it was designed to allow easy creation of DSL's and primarily is Object
// Oriented
// - due to mentioned DSL design decision any higher order function that takes
// function as the first argument looks hideous with a lambda
// - due to the same DSL design decision curried function definition looks awful

// This causes issues with expressing simple things in concise way very hard in
// Kotlin or at least you need to bare the bracers orgy ;-)




//cheating a bit and implementing foldLeft with List.fold
fun <T, R> foldLeft(f: (R, T) -> R): (R) -> (List<T>) -> R =
        { initial ->
            { list -> list.fold(initial, f) }
        }


// a map can be expressed in terms of fold
fun <T, R> map(f: (T) -> R): (List<T>) -> List<R> = {
    foldLeft<T, List<R>> { r, t ->
        r + listOf(f(t))
    }(emptyList())(it)
}

fun main(args: Array<String>) {
    val l = listOf(1, 2, 3)
    val plus10 = { x: Int -> x + 10 }

    // we did nice composition of functions thanks to the fact that we take function first
    val mapPlus10 = map(plus10)

    println(mapPlus10(l))
}
