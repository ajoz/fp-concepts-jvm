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

fun plus(a: Int, b: Int) =
        a + b

/*
Now I would like to partially apply it with firstCharacter argument, let's say 1 to get a
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
        { a: A, b: B ->
            f(a)(b)
        }

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

I think that DSL's where the primary focus of the language. When learning Kotlin
from it's great reference docs, it's easy to understand the DSL heritage after reading
"type-safe" builders example. The syntactic sugar was made especially for that.

A minute of browsing through the available API allows to find less complicated
example of this "sugar" in action:

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

There are two ways of calling this function: "sugar free" (forgive me this pun)
and "sugar full".
 */

val myLock = Any()
val locked1 = synchronized(myLock, { 42 })

// or better (sugary):

val locked2 = synchronized(myLock) {
    42
}

/*
This strangely resembles synchronized block in Java. Kotlin doesn't have a
reserved "synchronized" keyword, everything can be achieved through a sly function
and some sugar (I like it). This is explained in the docs:

> In Kotlin, there is a convention that if the last parameter to a function is a
> function, and you're passing a lambda expression as the corresponding
> argument, you can specify it outside of parentheses:
>
> lock (lock) {
>    sharedResource.operation()
>}

Due to mentioned DSL design decision any higher order function that takes
function as the firstCharacter argument looks hideous with a lambda.
 */

fun qux1(f: (String) -> Int, s: String): Int =
        f(s)

// using it:

val qux1v = qux1({ s -> s.length }, "This looks ugly")

// In a curried form qux function could be invoked differently

val qux2 = curry(::qux1)

// this way it can be used:

val qux2v = qux2 { it.length }("This is less ugly")

/*
We are back to our "weird butt looking" notation, where one of our buttocks is now
built from bracers. Like I said this is a matter of taste (I don't like it but I don't hate it).

Let's get this DSL stuff out of the way now cause there is one last thing I would like to talk
about. Kotlin is primarily an Object Oriented language, extension functions are a good
hint at that. Motivation behind creating them were Util classes known well from
Java.

So instead of doing `Collections.max(list)` we could do `list.max()`. Which is
much more in line with OO way of thinking but less with FP. Going back to the
function composition example, it would be nice to reason about functions without
the explicit and immediate need of thinking about their data.

Let's take [List#map] function as an example. It returns a list containing the
results of applying function to each element in the original list. Let's rewrite
so it's not an extension anymore.
 */

fun <T, R> map1(list: List<T>, function: (T) -> R): List<R> =
        list.map(function)

/*
It takes list as a firstCharacter argument and function as the last, allowing us to use
DSL sugar.
 */

val map1v1 = map1(listOf("This", "is a", "list")) {
    it.length
}

/*
But what if we would need to compose it? Let's say we need a function that takes
a list of strings and returns another list containing only the firstCharacter character.

Our function that returns firstCharacter character can look like this:
 */

// I'm using existing extensions just to have a working example
fun firstCharacter(string: String): String =
        string.take(1)

// getting a value is easy but how to create the needed function?
val firstLetterV1 = map1(listOf("This", "is a", "list")) {
    firstCharacter(it)
}

fun firstLetters(list: List<String>): List<String> =
        map1(list) {
            firstCharacter(it)
        }

/*
To get the needed function we need to redefine our map implementation:
- the order of arguments needs to be swapped (function should be firstCharacter)
- it needs to be curried by default (partial application should be possible)
*/

fun <T, R> map2(function: (T) -> R): (List<T>) -> List<R> =
        { list ->
            list.map(function)
        }

// Let's give it a spin and construct a value:

val firstLetterV2 = map2(::firstCharacter)(listOf("This", "is a", "list"))

// we can now define the firstLetters function as a composition

val firstLetter= map2(::firstCharacter)

/*
We can now pass our firstLetters function wherever a List<String> -> List<String>
is needed, we can compose it to create a more complicated functions, we can run
it.

So what does it say about the Kotlin's power? I won't deny that I would pick it
over Java any day of a month, but I can't agree its "the most powerful functional language"
out there available.

We managed to add all the missing but needed functionality through library
functions and extensions, but the language and its stdlib didn't support
everything out the box.

Syntax tailored more towards DSL's might prove less readable in FP context, but
it doesn't mean it won't be possible to do FP with it (as we easily demonstrated).
 */






