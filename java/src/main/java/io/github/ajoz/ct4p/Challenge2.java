package io.github.ajoz.ct4p;

import java.io.IOException;
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

    /*
     Ad 3. Most random number generators can be initialized with a seed.
     Implement a function that takes a seed, calls the random number generator
     with that seed, and returns the result. Memoize that function. Does it work?
     */
    public static void memoizeSeedRandom() {
        final Function<Integer, Integer> seedRandom = seed -> (new Random(seed)).nextInt();
        final Function<Integer, Integer> memoizeSeedRandom = memoize(seedRandom);

        System.out.println("Memoize with seed random: " + memoizeSeedRandom.apply(0));
        System.out.println("Memoize with seed random: " + memoizeSeedRandom.apply(0));
        System.out.println("Memoize with seed random: " + memoizeSeedRandom.apply(1));
        System.out.println("Memoize with seed random: " + memoizeSeedRandom.apply(1));
    }

    /*
     Ad 4. Which of these C++ functions are pure? Try to memoize them and
     observe what happens when you call them multiple times: memoized and not.
     */
    //a) factorial
    public static final Function<Long, Long> factorial =
            n -> {
                long result = 1;
                for (int i = 1; i <= n; i++) {
                    result *= i;
                }
                return result;
            };

    public static final Function<Long, Long> memoizedFactorial = memoize(factorial);

    //b) getChar
    public static final Function<Void, Character> getChar =
            ignored -> {
                try {
                    return (char) System.in.read();
                } catch (final IOException e) {
                    return ' ';
                }
            };

    public static final Function<Void, Character> memoizedGetChar = memoize(getChar);

    //c) function that is printing something to the console (deliberate side effect)
    public static final Function<Void, Boolean> f =
            ignored -> {
                System.out.println("Hello!");
                return true;
            };

    public static final Function<Void, Boolean> memoizedF = memoize(f);

    //d) function that is using global state
    public static int y = 0;

    public static Integer f2(final Integer x) {
        y += x;
        return y;
    }

    public static final Function<Integer, Integer> memoizedF2 = memoize(Challenge2::f2);

    public static void pureOrImpure() {
        System.out.println("Factorial(42) normal run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + factorial.apply(42L));
        }

        System.out.println("Factorial(42) memoized run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + memoizedFactorial.apply(42L));
        }

        System.out.println("getChar normal run: ");
        for (int i = 1; i <= 2; i++) {
            System.out.println("#" + i + ": " + getChar.apply(null));
        }

        System.out.println("getChar memoized run: ");
        for (int i = 1; i <= 2; i++) {
            System.out.println("#" + i + ": " + memoizedGetChar.apply(null));
        }

        System.out.println("function with side effect normal run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + f.apply(null));
        }

        System.out.println("function with side effect memoized run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + memoizedF.apply(null));
        }

        System.out.println("function messing up global state normal run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + f2(42));
        }

        System.out.println("function messing up global state memoized run: ");
        for (int i = 1; i < 10; i++) {
            System.out.println("#" + i + ": " + memoizedF2.apply(42));
        }
    }

    /*
     Ad 5. How many different functions are there from Bool to Bool ? Can
     you implement them all?

     data Bool = True | False

     We need functions that allow us to do:
     True -> True
     False -> False
     True -> False
     False -> True

     We can clearly see that those can be expressed by two functions

     True -> True
     False -> False

     is just
     x -> x
     hence identity

     id :: Bool -> Bool
     id x = x

     True -> False
     False -> True

     is just negating the value so we can call it not

     not :: Bool -> Bool
     not True = False
     not False = True
     */

    /*
     Ad 6. Draw a picture of a category whose only objects are the types
     Void , () (unit), and Bool ; with arrows corresponding to all possible
     functions between these types. Label the arrows with the names of the
     functions.

     Void does not have any value so we cannot call this function
     absurd :: Void -> ()
     absurd :: Void -> Bool

     Can we have an identity over type Void in Haskell?
     id :: a -> a
     does it mean that we can have
     id :: Void -> Void

     Does a void have a bottom? Can we have a function
     f :: () -> Void
     f _ = undefined

     or is this equally absurd? :)

     true :: () -> Bool
     false :: () -> Bool

     id :: Bool -> Bool
     not :: Bool -> Bool

     unit :: Bool -> ()
     */

    public static void main(String[] args) {
        // memoizeLongRunningFunction();
        // memoizeRandom();
        // memoizeSeedRandom();
//        pureOrImpure();
    }
}
