package io.github.ajoz.ct4p;

import java.util.Objects;
import java.util.function.Function;

/*
 Solutions for the Challenges in the first chapter of Category Theory for Programmers
 */
public final class Challenge1 {
    /*
     Ad 1. Implement, as best as you can, the identity function in your favorite language
     */
    public static <A> Function<A, A> identity() {
        return x -> x;
    }

    /*
     Ad 2. Implement the composition function in your favorite language.
     It takes two functions as arguments and returns a function that
     is their composition.
     */

    // we can compose two functions f and g:
    public static <A, B, C> Function<A, C> compose(final Function<A, B> before,
                                                   final Function<B, C> after) {
        return a -> after.apply(before.apply(a));
    }
    // this is very similar to what is defined in java Function interface


    /*
     Ad 3. Write a program that tries to test that your composition function
     respects identity
     */
    public static <A, B> boolean testComposition(final Function<A, B> function,
                                                  final A value) {
        final Function<A, B> left = compose(function, identity());
        final Function<A, B> right = compose(identity(), function);

        // f (id) == id (f)
        return Objects.equals(left.apply(value), right.apply(value));
    }

    /*
     Ad 4. Is the world-wide web a category in any sense? Are links morphisms?

     Category is a super simple thing, you need objects and arrows that point
     between them. There is also needed a unit of composition and a way to
     compose the arrows.

     This really depends on how you look at things. We could say that yes, links
     are like morphisms because if there is a link from page A to page B and
     from page B to page C then any web crawler can go from page A to page C.

     But is there a unit of composition? I guess any "self-link" (with #) can be
     thought as such, but is this stretching the definition a little? This is
     also a bit problematic because links with # point to specific parts of the
     page, its different for every page. Also this depends if we are treating
     <a href="#"> as a valid link.
     */

    /*
     Ad 5. Is Facebook a category, with people as objects and friendships as morphisms?

     It depends on how we define "friendship":
     - if a person A a friend of person B and a person B a friend of person C then
     does this mean that person A is a friend of person C. I wouldn't bet on that.
     - can someone be a friend to himself?
     */

    /*
     Ad 6. When is a directed graph a category?

     When each node has a self pointing edge (unit of composition) this way
     we can treat edges between nodes as morphisms and we can compose them.
     */

    public static void main(String[] args) {
        final int value = 42;
        System.out.println("Identity arg: " + value + ", result: " + identity().apply(42));

        final Function<Integer, Integer> composed = compose(x -> x + 1, y -> y * 2);
        System.out.println("Composed arg: " + value + ", result: " + composed.apply(value));

        final Function<Integer, Integer> add42 = x -> x + 42;
        System.out.println("Composition respects identity: " + testComposition(add42, 0));
    }
}
