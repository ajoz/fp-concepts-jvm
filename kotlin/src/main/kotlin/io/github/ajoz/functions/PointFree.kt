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

fun plus(a: Int, b: Int) = a + b

// I cannot invoke it with only one argument
// val plus1 = plus(1)

// to use the partially applied function later

// - it was designed to allow easy creation of DSL's and primarily is Object
// Oriented

// Just browsing through the available API it's easy to notice
// that most of the functions are defined as extensions to some existing types
// and even if those extension functions need a function it's always placed
// as a last one. This is supposed to make them look like language elements
// Example

/*
public inline fun <R> synchronized(lock: Any, block: () -> R): R {
    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "INVISIBLE_MEMBER")
    monitorEnter(lock)
    try {
        return block()
    }
    finally {
        @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE", "INVISIBLE_MEMBER")
        monitorExit(lock)
    }
}
 */

// This can be used like:

val myLock = Any()
val someValue = synchronized(myLock) {
    // some really important code that needs to be synchronized
}

// now synchronized function in Kotlin looks like a synchronized block in Java
// I think that the whole design was rotating around that idea

// - due to mentioned DSL design decision any higher order function that takes
// function as the first argument looks hideous with a lambda

fun needsAFunctionFirst(f: (String) -> Int, s: String): Int = f(s)

val naff = needsAFunctionFirst({ s -> s.length }, "This is ugly")

// Ugly, even without a type declaration for the lambda argument it still looks
// messy due to the pair of bracers {} surrounding it

// So why is this even a problem?

// Let's have a function that takes a list and another function, and maps all
// values on the list with said function:

fun <T, R> map1(list: List<T>, function: (T) -> R): List<R> = list.map(function)

fun first(string: String, count: Int): String =
        string.take(count) //for the ease of implementation std lib is used

// Let's also use first function that takes a string and returns its n first elements

val listOfStrings1 = listOf("This", "is a", "list")
val listOfStringsShort = map1(listOfStrings1) {
    first(it, 2)
}

// even if we use it, it's still messy and is glued to the data it's processing
// I cannot construct a composition of map1 and first as a new function called
// firstTwoWords

// To be perfectly honest the order doesn't matter as the functions are not
// curried by default

// - due to the same DSL design decision curried function definition looks awful

// Let's redefine our map and first functions:
// - swap the order of arguments (functions first)
// - change them to a curried form so instead of (A, B, C) -> D it should be
// (A) -> (B) -> (C) -> D

fun <T, R> map2(function: (T) -> R): (List<T>) -> List<R> = { list ->
    list.map(function)
}

fun first2(count: Int): (String) -> String = { string ->
    string.take(count)
}

// getting back to previous example:

val listOfStringsShort2: List<String> = map2(first2(2))(listOfStrings1)

// this doesn't look better at all
// what if we don't pass the value at all


val firstTwo: (String) -> String = first2(2)
val firstTwoLetters: (List<String>) -> List<String>  = map2(firstTwo)

// now we can pass firsTwoLetters function anywhere


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

// This causes issues with expressing simple things in concise way very hard in
// Kotlin or at least you need to bare the bracers orgy ;-)


fun main(args: Array<String>) {
    val l = listOf(1, 2, 3)
    val plus10 = { x: Int -> x + 10 }

    // we did nice composition of functions thanks to the fact that we take function first
    val mapPlus10 = map(plus10)

    println(mapPlus10(l))
}

