package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.Iterator;
import java.util.function.Consumer;

public class Iters {
    public static <T> void forEach(final Consumer<T> action,
                                   final Iter<T> iter) {
        do {
            final Try<T> next = iter.next()
                    .ifSuccess(action);
            if (next.isFailure())
                break;
        } while (true);
    }

    public static <T> Iterable<T> toIterable(final Iter<T> iter) {
        return () -> new IterIterator<>(iter);
    }
}
