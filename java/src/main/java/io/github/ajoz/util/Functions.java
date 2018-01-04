package io.github.ajoz.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class Functions {
    private Functions() {
    }

    public static <A, B> Function<A, Function<B, A>> constant() {
        return a -> ignored -> a;
    }

    public static <A, B, C> Function<A, Function<B, C>> curry2(final BiFunction<A, B, C> function) {
        return a -> b -> function.apply(a, b);
    }
}
