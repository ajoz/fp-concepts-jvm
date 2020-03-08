package io.github.ajoz.typesvtests.theorems;

import java.util.List;

public class TheoremsForFree2 {
    /*
      How many valid implementations are there:
     */
    static <A> A foo(final A a) {
        throw new UnsupportedOperationException("Not yet implemented, read on!");
    }
    /*
      What do we know about this function:
      - it takes some type A as an argument
      - it returns some type A as a result
      - we do not know anything about type A
      - we cannot create type A

      What can we do:
      - we could return null! It is Java after all but let's not be silly!
      - we can only return the value we got!
     */
    static <A> A identity(final A a) {
        return a;
    }

    /*
      How about the amount of valid implementations of:
     */
    static <A, B> A bar(final A a, final B b) {
        throw new UnsupportedOperationException("Not yet implemented, read on!");
    }

    /*
      Just as with the identity function also here, we don't know much.
      We can only return A.
     */
    static <A, B> A constant(final A a, final B b) {
        return a;
    }

    /*
      What about this function:
     */
    static <A> List<A> reverse(final List<A> list) {
        throw new UnsupportedOperationException("Not yet implemented, read on!");
    }

    /*
      Similarly we cannot just create a new A out of thin air but
      - we can return an empty list
      - we can return a list of smaller size
      - we can return a list of larger size
      - we can return a list with the first element repeated
      - we can return a list with each element doubled or tripled

      I guess we could go on. Also you might think that this bares the end of
      our journey showing that tests have won and types cannot do anything about
      it.

      If only there was a way to encode the properties we need into the types:
      - the returned list should be of the same size
      - the returned list should have the same elements as the original one
      - there should be a way to encode the order into the type

      So if we got a list [1, 2, 3] we should return a list [3, 2, 1]

      Can such magic be done through types? Yes, kinda, but not in Java.
     */
}
