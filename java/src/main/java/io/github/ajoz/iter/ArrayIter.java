package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.NoSuchElementException;

public final class ArrayIter<T> implements Iter<T> {
    private final T[] array;
    private int current;

    @SafeVarargs
    public ArrayIter(final T... array) {
        this.array = array;
    }

    @Override
    public Try<T> next() {
        if (array.length == 0)
            return Try.failure(new NoSuchElementException("No elements in this Iter!"));

        if (current >= array.length)
            return Try.failure(new NoSuchElementException("No more elements in this Iter!"));

        final Try<T> next = Try.success(array[current]);
        current++;
        return next;
    }
}
