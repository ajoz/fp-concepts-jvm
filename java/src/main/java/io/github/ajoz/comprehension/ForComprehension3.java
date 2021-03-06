package io.github.ajoz.comprehension;

import io.github.ajoz.util.TriFunction;
import io.github.ajoz.util.Try;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

final public class ForComprehension3<A, B, C, D> {
    private final Supplier<Try<A>> first;
    private final Predicate<A> firstGuard;
    private final Supplier<Try<B>> second;
    private final Predicate<B> secondGuard;
    private final Supplier<Try<C>> third;
    private final Predicate<C> thirdGuard;

    ForComprehension3(
            final Supplier<Try<A>> first,
            final Predicate<A> firstGuard,
            final Supplier<Try<B>> second,
            final Predicate<B> secondGuard,
            final Supplier<Try<C>> third,
            final Predicate<C> thirdGuard
    ) {
        this.first = first;
        this.firstGuard = firstGuard;
        this.second = second;
        this.secondGuard = secondGuard;
        this.third = third;
        this.thirdGuard = thirdGuard;
    }

    ForComprehension3(
            final Supplier<Try<A>> first,
            final Supplier<Try<B>> second,
            final Supplier<Try<C>> third
    ) {
        this(
                first, __ -> true,
                second, __ -> true,
                third, __ -> true
        );
    }

    public Try<D> yield(final TriFunction<A, B, C, D> yieldExpr) {
        return first.get()
                .filter(firstGuard)
                .flatMap(a -> second.get()
                        .filter(secondGuard)
                        .flatMap(b -> third.get()
                                .filter(thirdGuard)
                                .map(c -> yieldExpr.apply(a, b, c))));
    }
}
