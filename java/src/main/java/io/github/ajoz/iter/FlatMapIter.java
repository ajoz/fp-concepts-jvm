package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Function;

public class FlatMapIter<T, R> implements Iter<R> {
    private final Iter<T> upstream;
    private final Function<T, Try<R>> flatMapper;

    public FlatMapIter(final Iter<T> upstream,
                       final Function<T, Try<R>> flatMapper) {
        this.upstream = upstream;
        this.flatMapper = flatMapper;
    }

    @Override
    public Try<R> next() {
        return upstream
                .next()
                .flatMap(flatMapper);
    }
}
