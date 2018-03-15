package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Consumer;

public final class OnEachIter<T> implements Iter<T> {
    private final Iter<T> upstream;
    private final Consumer<? super T> action;

    public OnEachIter(final Iter<T> upstream,
                      final Consumer<? super T> action) {
        this.upstream = upstream;
        this.action = action;
    }

    @Override
    public Try<T> next() {
        // Try implementation has ifSucces already so it's not necessary
        // to implement it manually
        return upstream.next().ifSuccess(action);
    }
}
