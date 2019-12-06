package io.github.ajoz.comprehension;

import io.github.ajoz.util.Try;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

final class ForComprehension2<A, B, C> {
    private final Supplier<Try<A>> first;
    private final Predicate<A> firstGuard;
    private final Supplier<Try<B>> second;
    private final Predicate<B> secondGuard;

    ForComprehension2(
            final Supplier<Try<A>> first,
            final Predicate<A> firstGuard,
            final Supplier<Try<B>> second,
            final Predicate<B> secondGuard
    ) {
        this.first = first;
        this.firstGuard = firstGuard;
        this.second = second;
        this.secondGuard = secondGuard;
    }

    ForComprehension2(
            final Supplier<Try<A>> first,
            final Supplier<Try<B>> second
    ) {
        this(
                first, __ -> true,
                second, __ -> true
        );
    }

    public Try<C> yield(final BiFunction<A, B, C> yieldExpr) {
        return first.get()
                .filter(firstGuard)
                .flatMap(a -> second.get()
                        .filter(secondGuard)
                        .map(b -> yieldExpr.apply(a, b)));
    }
}

//@FunctionalInterface
//interface GeneratorExpr<A> {
//    Try<A> generate();
//}

//@FunctionalInterface
//interface FilterExpr<A> {
//    boolean test(final A value);
//
//    static <A> FilterExpr<A> TRUE() {
//        return value -> true;
//    }
//
//    static <A> FilterExpr<A> FALSE() {
//        return value -> false;
//    }
//}

//@FunctionalInterface
//interface YieldExpr<A, B, C> {
//    C yield(final A first,
//            final B second);
//}