package io.github.ajoz.validation;

import io.github.ajoz.util.Functor;
import io.github.ajoz.util.Semigroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

class NonEmptyList<A> implements Semigroup<NonEmptyList<A>>, Functor<A> {
    private final List<A> list = new LinkedList<>();

    public NonEmptyList(final A item) {
        list.add(item);
    }

    private NonEmptyList(final List<A> seed) {
        list.addAll(seed);
    }

    public NonEmptyList<A> append(final A item) {
        final List<A> appened = new LinkedList<>();
        appened.addAll(list);
        appened.add(item);
        return new NonEmptyList<>(appened);
    }

    public NonEmptyList<A> append(final NonEmptyList<A> other) {
        final List<A> appened = new LinkedList<>();
        appened.addAll(list);
        appened.addAll(other.list);
        return new NonEmptyList<>(appened);
    }

    public <B> Functor<B> map(final Function<A, B> function) {
        final List<B> mapped = new LinkedList<>();
        for (final A item : list) {
            mapped.add(function.apply(item));
        }
        return new NonEmptyList<>(mapped);
    }

    @Override
    public String toString() {
        return "NonEmptyList{" + list + '}';
    }

    public static <A> NonEmptyList<A> nel(final A item) {
        return new NonEmptyList<>(item);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NonEmptyList<?> that = (NonEmptyList<?>) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }
}