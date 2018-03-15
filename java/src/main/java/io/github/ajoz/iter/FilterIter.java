package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Predicate;

public final class FilterIter<T> implements Iter<T> {
    private final Iter<T> upstream;
    private final Predicate<? super T> predicate;

    public FilterIter(final Iter<T> upstream,
                      final Predicate<? super T> predicate) {
        this.upstream = upstream;
        this.predicate = predicate;
    }

    @Override
    public Try<T> next() {
        do {
            // take the next item from upstream
            final Try<T> next = upstream.next();
            // if there is no more items upstream, then just return a failure
            if (next.isFailure())
                return next;

            // if there is an item upstream, then check if satisfies the
            // given predicate and if it does then return it
            final Try<T> filtered = next.filter(predicate);
            if (filtered.isSuccess())
                return filtered;
            // if the item upstream does not satisfy the predicate then
            // try again
        } while (true);
    }
}
