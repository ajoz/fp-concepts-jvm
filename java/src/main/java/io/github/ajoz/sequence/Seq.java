package io.github.ajoz.sequence;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public abstract class Seq<A> implements Iterable<A> {
    public abstract A getHead();
    public abstract Seq<A> getTail();

    public abstract boolean isEmpty();

    public Seq<A> take(final int threshold) {
        if (threshold == 0)
            return new Seq.Cons<>(getHead());
        else
            return new Seq.Cons<>(getHead(), () -> getTail().take(threshold - 1));
    }

    public static final class Cons<A> extends Seq<A> {
        private final A head;
        private final Supplier<Seq<A>> tail;

        public Cons(final A head,
                    final Supplier<Seq<A>> tail) {
            this.head = head;
            this.tail = tail;
        }

        public Cons(final A head) {
            this(head, Nil::new);
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
    }
}
