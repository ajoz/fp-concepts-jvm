package io.github.ajoz.iter;

import io.github.ajoz.util.Try;

import java.util.Iterator;
import java.util.Objects;
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
public interface Iter<T> extends Iterable<T> {
    Try<T> next();

    default <R> Iter<R> map(final Function<? super T, ? extends R> mapper) {
        Objects.requireNonNull(mapper, "Function passed to Iter.map cannot be null!");
        return new MapIter<>(this, mapper);
    }

    default <R> Iter<R> flatMap(final Function<? super T, ? extends Iter<? extends R>> mapper) {
        Objects.requireNonNull(mapper, "Function passed to Iter.flatMap cannot be null!");
        return new FlatMapIter<>(this, mapper);
    }

    default Iter<T> filter(final Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "Predicate passed to Iter.filter cannot be null!");
        return new FilterIter<>(this, predicate);
    }

    default Iter<T> onEach(final Consumer<? super T> action) {
        Objects.requireNonNull(action, "Consumer passed to Iter.onEach cannot be null!");
        return new OnEachIter<>(this, action);
    }

    // there is no type that would allow expressing a need for positive integers
    // without zero :> like natural numbers
    default Iter<T> take(final int amount) {
        return new TakeIter<>(this, amount);
    }

    default Iterator<T> iterator() {
        return new IterIterator<>(this);
    }

    @SafeVarargs
    @SuppressWarnings("varargs") // if creating a Stream from an array is safe then creating an Iter is ;-)
    static <U> Iter<U> from(final U... items) {
        Objects.requireNonNull(items, "Array passed to Iter.from cannot be null!");
        return new ArrayIter<>(items);
    }

    static <U> Iter<U> from(final U seed, final Function<U, U> generator) {
        Objects.requireNonNull(generator, "Generator function passed to Iter.from cannot be null!");
        return new Seed1Iter<>(seed, generator);
    }

    static <U> Iter<U> empty() {
        return new EmptyIter<>();
    }
}
