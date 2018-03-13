package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.NoSuchElementException;

class EmptyIter<T> implements Iter<T> {
    @Override
    public Try<T> next() {
        return Try.failure(new NoSuchElementException("Empty sequence does not have a next element!"));
    }
}
