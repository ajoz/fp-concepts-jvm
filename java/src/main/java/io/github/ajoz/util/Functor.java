package io.github.ajoz.util;

import java.util.function.Function;

public interface Functor<A> {
    <B> Functor<B> map(Function<A, B> function);
}
