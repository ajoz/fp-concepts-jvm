package io.github.ajoz.util;

import java.util.function.Function;

public abstract class Functions {
    private Functions() {
    }

    public static <A, B> Function<A, Function<B, A>> constant() {
        return a -> ignored -> a;
    }
}
