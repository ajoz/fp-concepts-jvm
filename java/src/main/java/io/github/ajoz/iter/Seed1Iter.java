package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Function;

public final class Seed1Iter<T> implements Iter<T> {
    private final Function<T, T> generator;
    private T seed;

    public Seed1Iter(final T seed,
                     final Function<T, T> generator) {
        this.seed = seed;
        this.generator = generator;
    }

    @Override
    public Try<T> next() {
        final Try<T> next = Try.success(seed);
        // should this halt if an exception occurs in generator function?
        seed = generator.apply(seed);
        return next;
    }
}
