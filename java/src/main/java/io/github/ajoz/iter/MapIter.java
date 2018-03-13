package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Function;

public class MapIter<T, R> implements Iter<R> {
    private final Iter<T> upstream;
    private final Function<T, R> mapper;

    public MapIter(final Iter<T> upstream,
            final Function<T, R> mapper) {
        this.upstream = upstream;
        this.mapper = mapper;
    }

    @Override
    public Try<R> next() {
        return upstream.next().map(mapper);
    }
}
