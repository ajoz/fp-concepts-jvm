package io.github.ajoz.sequence;

import io.github.ajoz.lists.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Seq<A> implements Iterable<A> {
    public abstract A getHead();

    public abstract Seq<A> getTail();

    public abstract boolean isEmpty();

    public abstract <B> Seq<B> map(final Function<A, B> func);

    public abstract <B> Seq<B> flatMap(final Function<A, Seq<B>> func);

    public Seq<A> take(final int threshold) {
        if (isEmpty()) {
            return new Seq.Nil<>();
        } else {
            if (threshold == 0)
                return new Seq.Cons<>(getHead(), Nil::new);
            else
                return new Seq.Cons<>(getHead(), () -> getTail().take(threshold - 1));
        }
    }

//    public Seq<A> drop(final int amount) {
//        if (isEmpty()) {
//            return new Seq.Nil<>();
//        } else if (amount < 0) {
//            throw new IllegalArgumentException("Drop amount needs to be a positive integer!");
//        } else {
//
//        }
//    }

    public <B, C> Seq<C> zip(final Seq<B> other,
                             final BiFunction<A, B, C> zipper) {
        if (isEmpty()) {
            return new Seq.Nil<>();
        }

        if (other.isEmpty()) {
            return new Seq.Nil<>();
        }

        final A a = getHead();
        final B b = other.getHead();

        return new Seq.Cons<>(zipper.apply(a, b), () -> getTail().zip(other.getTail(), zipper));
    }

    public static <A> Seq<A> continously(final A value) {
        return new Seq.Cons<>(value, () -> continously(value));
    }

    public static <A> Seq<A> generate(final A seed,
                                      final Function<A, A> generator) {
        return new Seq.Cons<>(seed, () -> generate(generator.apply(seed), generator));
    }

    public static <A> Seq<A> of(final List<A> list) {
        if (list.isEmpty()) {
            return new Seq.Nil<>();
        } else {
            final A head = Lists.head(list);
            final List<A> tail = Lists.tail(list);
            return new Seq.Cons<>(head, () -> Seq.of(tail));
        }
    }

    public static <A> Seq<A> concat(final Seq<A> seq,
                                    final Supplier<Seq<A>> other) {
        // if the sequence passed is empty then just return what is in the tail
        if (seq.isEmpty()) {
            return other.get();
            // if the next element of the sequence is empty then we need to concat it
        } else if (seq.getTail().isEmpty()) {
            return new Seq.Cons<>(seq.getHead(), other);
            // if there are more elements to go then just return value and defer
            // next concat
        } else {
            final A head = seq.getHead();
            final Seq<A> tail = seq.getTail();
            return new Seq.Cons<>(head, () -> concat(tail, other));
        }
    }

    public static final class Cons<A> extends Seq<A> {
        private final A head;
        private final Supplier<Seq<A>> tail;

        public Cons(final A head,
                    final Supplier<Seq<A>> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public Iterator<A> iterator() {
            return new Iterator<A>() {
                private Seq<A> current = Cons.this;

                @Override
                public boolean hasNext() {
                    return !current.isEmpty();
                }

                @Override
                public A next() {
                    final A value = current.getHead();
                    current = current.getTail();
                    return value;
                }
            };
        }

        @Override
        public A getHead() {
            return head;
        }

        @Override
        public Seq<A> getTail() {
            return tail.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public <B> Seq<B> map(final Function<A, B> func) {
            return new Cons<>(func.apply(head), () -> getTail().map(func));
        }

        @Override
        public <B> Seq<B> flatMap(final Function<A, Seq<B>> func) {
            return concat(func.apply(head), () -> getTail().flatMap(func));
        }
    }

    public static final class Nil<A> extends Seq<A> {

        @Override
        public Iterator<A> iterator() {
            return new Iterator<A>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public A next() {
                    throw new NoSuchElementException("");
                }
            };
        }

        @Override
        public A getHead() {
            throw new NoSuchElementException("");
        }

        @Override
        public Seq<A> getTail() {
            throw new NoSuchElementException("");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public <B> Seq<B> map(Function<A, B> func) {
            return new Nil<>();
        }

        @Override
        public <B> Seq<B> flatMap(Function<A, Seq<B>> func) {
            return new Nil<>();
        }
    }
}
