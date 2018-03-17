package io.github.ajoz.ct4p;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

/*
 Solutions for the Challenges in the second chapter of Category Theory for Programmers
 */
public class Challenge2 {
    /*
     Ad 1. Define a higher-order function (or a function object) memoize in your
     favorite language. This function takes a pure function f as an argument and
     returns a function that behaves almost exactly like f , except that it only
     calls the original function once for every argument, stores the result
     internally, and subsequently returns this stored result every time it’s
     called with the same argument.
     */
    public static <A, B> Function<A, B> memoize(final Function<A, B> function) {
        return new Function<A, B>() {
            // permits null keys and values
            private final Map<A, B> cache = new HashMap<>();

            @Override
            public B apply(final A a) {
                if (cache.containsKey(a)) {
                    return cache.get(a);
                } else {
                    final B b = function.apply(a);
                    cache.put(a, b);
                    return b;
                }
            }
        };
    }

    /*
     You can tell the memoized function from the original by watching its
     performance. For instance, try to memoize a function that takes a long time
     to evaluate. You’ll have to wait for the result the first time you call it,
     but on subsequent calls, with the same argument, you should get the result
     immediately.
     */
    public static void memoizeLongRunningFunction() {
        final Function<Integer, Integer> sleepyAdd42 = x -> {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                //
            }
            return x + 42;
        };

        final Function<Integer, Integer> memoizeAdd42 = memoize(sleepyAdd42);
        System.out.println("Memoize long running, first run: " + memoizeAdd42.apply(0));
        System.out.println("Memoize long running, second run: " + memoizeAdd42.apply(0));
    }

    /*
     Ad 2. Try to memoize a function from your standard library that you normally
     use to produce random numbers. Does it work?
     */
    public static void memoizeRandom() {
        final Function<Void, Integer> random = o -> (new Random()).nextInt();
        final Function<Void, Integer> memoizeRandom = memoize(random);

        // will always return the same number
        System.out.println("Memoize random, run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + memoizeRandom.apply(null));
        }
    }


    public static void main(String[] args) {
        memoizeLongRunningFunction();
        memoizeRandom();

    }
}
