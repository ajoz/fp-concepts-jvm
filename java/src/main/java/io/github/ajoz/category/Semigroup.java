package io.github.ajoz.category;

@FunctionalInterface
public interface Semigroup<A extends Semigroup<A>> {
    A append(A other);
}