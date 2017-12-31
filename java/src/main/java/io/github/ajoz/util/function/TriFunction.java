package io.github.ajoz.util.function;

/**
 * Represents a function that accepts three arguments and produces a result.
 *
 * @param <A1> the type of the first function argument
 * @param <A2> the type of the second function argument
 * @param <A3> the type of the third function argument
 * @param <R>  the type of the result of the function
 */
public interface TriFunction<A1, A2, A3, R> {
    /**
     * Applies this function to the given arguments.
     *
     * @param a the first function argument
     * @param b the second function argument
     * @param c the third function argument
     * @return the function result
     */
    R apply(A1 a, A2 b, A3 c);
}