package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class IterIterator<T> implements Iterator<T> {
    private final Iter<T> iter;

    private boolean shouldCheck = true;
    private Try<T> next = Try.failure(new NoSuchElementException("No more elements in this Iter!"));

    public IterIterator(final Iter<T> iter) {
        this.iter = iter;
    }

    @Override
    public boolean hasNext() {
        if (shouldCheck) {
            next = iter.next();
            shouldCheck = false;
        }

        return next.isSuccess();
    }

    @Override
    public T next() {
        final T value = next.get();
        shouldCheck = true;
        return value;
    }
}
