package io.github.ajoz.category;

@FunctionalInterface
public interface Semigroup<T extends Semigroup<T>> {
    T append(T other);
}