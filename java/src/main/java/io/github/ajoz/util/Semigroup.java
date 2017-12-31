package io.github.ajoz.util;

public interface Semigroup<T extends Semigroup<T>> {
    T append(T other);
}