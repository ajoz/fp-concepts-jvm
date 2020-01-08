package io.github.ajoz.narrow_domain;

import java.util.List;
import java.util.Optional;

public class NarrowDomain3 {
    /*
      The implementation of the function below, does not matter
      for the example. The signature is important, the amount that
      we are passing, can it be negative? can it be zero?
     */
    public static <A> List<A> take(final Integer amount, final List<A> list) {
        throw new UnsupportedOperationException("");
    }
    /*
      Zero is probably ok for the example, but what does it mean to take
      -10 elements from the list, we need some type that would describe
      all positive integers plus a zero.
     */
}

final class Natural extends Number {
    private final Integer value;

    private Natural(final Integer value) {
        this.value = value;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public static Optional<Natural> of(final Integer value) {
        if (value < 0)
            return Optional.empty();
        return Optional.of(new Natural(value));
    }
}

/*
  Thanks to this we can rewrite the te take function to:
 */
class NarrowedDomain3 {
    static <A> List<A> take(final Natural amount, final List<A> list) {
        throw new UnsupportedOperationException("");
    }
}
