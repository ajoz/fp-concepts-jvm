package io.github.ajoz.util;

import java.util.function.BiFunction;
import java.util.function.Function;

/*
 Collection of functions for working with classes added in java.util.function package.
 */
public abstract class Functions {
    private Functions() {
    }

    /*
     We want to have access to two argument function that always returns the first argument.
     In Haskell the definition is very simple and straightforward:

     const :: a -> b -> a

     The implementation is even simpler as it boils down to a one line (we can mark the ignored param with "_")

     const x _ = x

     We cannot keep the same name in Java as in the Haskell version. "const" is not a keyword but it is
     reserved so not accessible to mere mortals ;-) Java syntax shows it's ugly head as to achieve this
     we need a static method returning a Function returning a Function.
     */
    public static <A, B> Function<A, Function<B, A>> constant() {
        return a -> ignored -> a;
    }

    /*
     Handy function that allows easy change from BiFunction to a curried form. I've added "2" in the name to remind
     that this is responsible only for handling two argument functions.

     As there is no such problem in Haskell I created an artificial example using a tuple:
     curry2 :: ((a, b) -> c) -> (a -> b -> c)

     Function "curry2" takes a tuple to a type function and returns the curried form. It's basically the same
     as BiFunction. Implementation is also a one liner:

     curry2 bif = \a -> \b -> bif (a, b)

     It's sad that when adding Function interface, Oracle didn't decide to add some syntactic sugar and allow
     invoking the function just with parens like in Kotlin. Constant need of doing foo.apply(param) is visually
     annoying.
     */
    public static <A, B, C> Function<A, Function<B, C>> curry2(final BiFunction<A, B, C> function) {
        return a -> b -> function.apply(a, b);
    }
}
