package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 This is an example of a Iterator<T> without the need for specialized hasNext
 method. Classic Java Iterator<T> defines three methods:

 - boolean hasNext()
 - T next()
 - void remove()

 The remove method can be treated as an additional operation, that is not
 associated in any way with the main concern of an Iterator which is just
 iteration.

 Although hasNext() and next() seem simple, they are unsafe. Calling next()
 without checking hasNext() might result with an exception depending on the
 implementation. This can be avoided if a type that can express uncertenity
 is used: Option, Optional, Maybe, Try or even Either.

 For this simple implementation Try instead of Option as it allows sending
 information about the cause of a failed computation.

 Why the name Iter? So it's not confused with an Iterator. But the name is
 similar enough so it rings a bell.

 Also didn't choose Seq, Sequence or Stream for similar reason.
*/
public interface Iter<T> {
    Try<T> next();

    default <R> Iter<R> map(final Function<T, R> mapper) {
        return new MapIter<>(this, mapper);
    }

    default <R> Iter<R> flatMap(final Function<T, Iter<R>> mapper) {
        return new FlatMapIter<>(this, mapper);
    }

    default Iter<T> filter(final Predicate<T> predicate) {
        return new FilterIter<>(this, predicate);
    }

    default Iter<T> onEach(final Consumer<T> action) {
        return new PeekIter<>(this, action);
    }

    default Iter<T> take(final int amount) {
        return new TakeIter<>(this, amount);
    }

    default void forEach(final Consumer<T> action) {
        Iters.forEach(action, this);
    }

    @SafeVarargs
    static <U> Iter<U> from(final U... items) {
        return new ArrayIter<>(items);
    }

    static <U> Iter<U> from(final U seed, final Function<U, U> fun) {
        return new Seed1Iter<>(seed, fun);
    }
}
