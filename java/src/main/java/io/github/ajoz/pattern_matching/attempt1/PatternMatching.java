package io.github.ajoz.pattern_matching.attempt1;


import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static io.github.ajoz.pattern_matching.attempt1.ClassCase.inCaseOf;
import static io.github.ajoz.pattern_matching.attempt1.ElseCase.otherwise;

abstract class Foo {
    public static class Bar extends Foo {
    }

    public static class Baz extends Foo {
    }
}

public class PatternMatching {
    public static void main(final String[] args) {
        final Match<Foo, String> matcher =
                new Match<>(
                        inCaseOf(Foo.Bar.class, bar -> "bar!"),
                        inCaseOf(Foo.Baz.class, baz -> "baz!"),
                        otherwise(foo -> "we don't know!")
                );

        final Foo foo = new Foo.Baz();
        matcher
                .match(foo)
                .ifPresent(System.out::println);
    }
}

class Match<A, B> {
    private Case<?, B>[] cases;

    @SafeVarargs
    public Match(final Case<?, B>... cases) {
        this.cases = cases;
    }

    public Optional<B> match(final A value) {
        for (final Case<?, B> c : cases) {
            if (c.test(value))
                return c.match(value);
        }

        return Optional.empty();
    }
}

interface Case<A, B> extends Predicate<Object> {
    default <C> Optional<B> match(final C value) {
        //noinspection unchecked
        final A c = (A) value;
        return runCase(c);
    }

    Optional<B> runCase(final A value);
}

final class ClassCase<A, B> implements Case<A, B> {
    private final Class<A> clazz;
    private final Function<A, B> function;

    private ClassCase(final Class<A> clazz,
                      final Function<A, B> function) {
        this.clazz = clazz;
        this.function = function;
    }

    @Override
    public boolean test(final Object value) {
        return clazz.isInstance(value);
    }

    @Override
    public Optional<B> runCase(final A value) {
        return Optional.ofNullable(
                function.apply(value)
        );
    }

    public static <A, B> Case<A, B> inCaseOf(final Class<A> clazz,
                                             final Function<A, B> function) {
        return new ClassCase<>(clazz, function);
    }
}

final class ElseCase<A, B> implements Case<A, B> {
    private final Function<A, B> function;

    ElseCase(final Function<A, B> function) {
        this.function = function;
    }

    @Override
    public boolean test(Object value) {
        return true;
    }

    @Override
    public Optional<B> runCase(final A value) {
        return Optional.ofNullable(
                function.apply(value)
        );
    }

    public static <A, B> Case<A, B> otherwise(final Function<A, B> function) {
        return new ElseCase<>(function);
    }
}