package io.github.ajoz.tuple;

public class Tuple<A, B> {
    public final A _1;
    public final B _2;

    private Tuple(final A arg1, final B arg2) {
        _1 = arg1;
        _2 = arg2;
    }

    public static <A, B> Tuple<A, B> of(final A arg1, final B arg2) {
        return new Tuple<>(arg1, arg2);
    }
}
