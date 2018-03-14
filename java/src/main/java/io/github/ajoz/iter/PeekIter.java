package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Consumer;

public class PeekIter<T> implements Iter<T> {
    private final Iter<T> upstream;
    private final Consumer<T> action;

    public PeekIter(final Iter<T> upstream,
                    final Consumer<T> action) {
        this.upstream = upstream;
        this.action = action;
    }

    @Override
    public Try<T> next() {
        return upstream.next().ifSuccess(action);
    }
}
