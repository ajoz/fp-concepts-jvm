package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Function;

public class FlatMapIter<T, R> implements Iter<R> {
    private final Iter<T> upstream;
    private final Function<T, Iter<R>> mapper;

    public FlatMapIter(final Iter<T> upstream,
                       final Function<T, Iter<R>> mapper) {
        this.upstream = upstream;
        this.mapper = mapper;
    }

    @Override
    public Try<R> next() {
        return upstream
                .next()
                .flatMap(
                        t -> mapper
                                .apply(t)
                                .next()
                );
    }
}
