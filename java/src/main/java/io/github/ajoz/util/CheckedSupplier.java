package io.github.ajoz.util;

@FunctionalInterface
public interface CheckedSupplier<T> {
    T get() throws Exception;
}
