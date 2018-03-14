package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.NoSuchElementException;

public class TakeIter<T> implements Iter<T> {
    private final Iter<T> upstream;
    private final int amount;
    private int taken;

    public TakeIter(final Iter<T> upstream,
                    final int amount) {
        this.upstream = upstream;
        this.amount = amount;
    }

    @Override
    public Try<T> next() {
        // take an element from the upstream Iter
        final Try<T> next = upstream.next();
        // if the elemenent does not exist then just propagate the failure
        if (next.isFailure())
            return next;

        // if already taken enough elements then just propagate the failure
        if (taken >= amount) {
            return Try.failure(new NoSuchElementException("Reached the Iter amount: " + amount));
        }

        // increment the currently taken amount
        taken++;
        // return the upstream Iter item
        return next;
    }
}
