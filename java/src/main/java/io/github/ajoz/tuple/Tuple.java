package io.github.ajoz.tuple;

/**
 *
 * @param <A>
 * @param <B>
 */
public class Tuple<A, B> {
    public final A _1;
    public final B _2;

    /**
     *
     * @param arg1
     * @param arg2
     */
    private Tuple(final A arg1, final B arg2) {
        _1 = arg1;
        _2 = arg2;
    }

    /**
     *
     * @param arg1
     * @param arg2
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> Tuple<A, B> of(final A arg1, final B arg2) {
        return new Tuple<A, B>(arg1, arg2);
    }
}
