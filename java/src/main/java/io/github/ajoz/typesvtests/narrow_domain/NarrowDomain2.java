package io.github.ajoz.typesvtests.narrow_domain;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NarrowDomain2 {
    /*
      Similar issue with the domain being too large.
      In case of an empty List we will get a NoSuchElementException.
     */
    static <A> A getFirst(final List<A> list) {
        return list.get(0);
    }
}

/*
    Narrowed list:
 */
class NonEmptyList<A> {
    private final List<A> list;

    NonEmptyList(final List<A> list) {
        this.list = list;
    }

    public static <A> Optional<NonEmptyList<A>> of(final A... values) {
        if (values.length == 0)
            return Optional.empty();
        return Optional.of(new NonEmptyList<>(Arrays.asList(values)));
    }

    public A get(final int index) {
        return list.get(index);
    }
}

class NarrowedDomain2 {
    static <A> A getFirst(final NonEmptyList<A> list) {
        return list.get(0);
    }
}

/*
  In most cases developer will push the responsibility in the same direction
  that the code they are using is pushing. So if getFirst expects a NonEmptyList
  then his piece of code will expect NonEmptyList also.

  This is good because eventually it will be pushed back to the edge of our
  system.
 */
