package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.Iterator;

public final class IteratorIter<T> implements Iter<T> {
    private final Iterator<T> iterator;

    public IteratorIter(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Try<T> next() {
        // We can manually check if there are any elements and then return
        // Failure or Success accordingly

        // if (!iterator.hasNext())
        //     return Try.failure(new NoSuchElementException("Iterator does not have more elements!"));
        //
        // return Try.success(iterator.next());

        // usage of Try.ofSupplier might be more concise as calling next will always
        // result with an exception if there are no more elements, the main
        // problem lies in the efficiency because the exception needs to be thrown
        return Try.ofSupplier(iterator::next);
    }
}
