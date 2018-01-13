package io.github.ajoz.functions

/*
It's been around 4 months since Mobilization 2017 and I'm still watching the talks
I missed during the conference. One of the presentations I really enjoyed was
"Functional approach to Android architecture using Kotlin" by Jorge Castillo.

I was surprised it's available not only on JUG Łódź youtube channel but also on
realm.io with a full transcript (sic!). I strongly suggest you to check it out
on their site.

Everything would be fine if not for a one sentence in the description of the talk:

> "Kotlin is arising as one of the most powerful functional languages".

Imho this is a bit of exaggeration, especially unfair for Scala, Clojure and ETA.
I even checked what did we have in the description on the Mobilization site:

> "Modern languages with functional colors are mainstream lately. Kotlin is
> arising as one of the most powerful ones"

Ok so it was "most powerful with functional colors". Someone just did a
an unfortunate mental shortcut when writing the description, regardless this
"most powerful" thing made me think. Is Kotlin really that powerful and how one
defines programming language power?

Starting with advantages.

Kotlin's syntax is much more concise then Java's (for me it's a main selling point
on the JVM, after years of working with Java 6).

Language allows for standalone functions, they are still objects in disguise
(this is JVM we are talking about here) but there is a really nice syntax for declaring and invoking them.

Is this enough to contend the title of "most powerful" functional language?

I think not. Kotlin is limited by it's syntax and design decisions, in other words
FP was imho not it's primary vision of usage. (I don't say this was a bad decision!)

FP is mainly about functions and their composition, let's look how Kotlin fairs
in this department.
*/

// foo :: String -> Int
fun foo(s: String): Int =
        s.length

// bar :: Int -> Boolean
fun bar(i: Int): Boolean =
        i.mod(2) == 0
/*
Now I want to have a function from type String to type Boolean, we could achieve
it with function composition.

Unfortunately Kotlin has none in the standard library. This is a real shame as
Java 8 that introduced Function interface gave programmers andThen and compose
methods in said interface.

So what choices do we have? Either we invoke those two functions and compose
them in place of the call:
*/

// baz1 :: String -> Boolean
fun baz1(s: String): Boolean =
        bar(foo(s))

// or we define our own method of function composition. Thanks to the feature
// of extension methods it's super simple in Kotlin.

// andThen :: (a -> b) -> (b -> c) -> (a -> c)
infix fun <A, B, C> ((A) -> B).andThen(f: (B) -> C): (A) -> C =
        { a: A ->
            f(this(a))
        }

// now we can write our baz function differently:

// baz2 :: String -> Boolean
val baz2 = ::foo andThen ::bar

/*
It was really easy (this is why I like this language) but on the other hand composition
is missing from the standard library. Someone asked about it on the official
forums and got a replay:

> "No, it’s not in the standard library. Function composition is very important
> in certain programming styles, and almost never occurs in others."

This quote is not an accusation from my side, just a confirmation that FP was not
the goal of the designers. There are wonderful libraries that fill this gap like:
funKtionale or arrow (former Kategory) which I strongly advise to check.

Ok, so no function composition by default, what about currying and partial application?

Let's say I have a function for adding two Int values:
 */

fun plus(a: Int, b: Int) = a + b

/*
Now I would like to partially apply it with first argument, let's say 1 to get a
one argument function that is always adding 1 to whatever else is passed to it.

Writing something like:

val plus1: (Int) -> Int = curriedPlus(1)

is just not possible, unless we do some extension magic again:
*/

fun <A, B, C> curry(f: (A, B) -> C): (A) -> (B) -> C =
        { a: A ->
            { b: B ->
                f(a, b)
            }
        }

// and now I can:
val curriedPlus: (Int) -> (Int) -> Int = curry(::plus)
val plus1 = curriedPlus(1)

/*

What about doing this the other way around? We need to write it ourselves again:
 */

fun <A, B, C> uncurry(f: (A) -> (B) -> C): (A, B) -> C =
        { a: A, b: B -> f(a)(b) }

val uncurriedPlus: (Int, Int) -> Int = uncurry(curriedPlus)

/*
Yet again external libraries have our back here. I know that the beauty is in the
eye of the beholder, but there is some ugliness in Kotlin syntax if we want to
work with curried forms of functions. It's visible in the uncurry implementation,
where we had to call the curried function like this `f(a)(b)`.

I think Brian Lonsdorf called it "weird butt looking thing" in one his JS FP talks,
it's hard to disagree with the description. Still I understand why it looks like
this and it's much much much (I can't even express how much) better then Java's
`f.apply(a).apply(b)`.

There is also one more ugly thing, which is the amount of bracers used. This is
a conscious language design decision, but a long enough curried function will look
like a "bracer orgy" and readability might suffer (as usual your mileage might vary).

Staying on the topic of design decisions
 */

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
val firstTwoLetters: (List<String>) -> List<String> = map2(firstTwo)

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

