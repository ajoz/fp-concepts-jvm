package io.github.ajoz.category;

public interface Semigroup<T extends Semigroup<T>> {
    T append(T other);
}