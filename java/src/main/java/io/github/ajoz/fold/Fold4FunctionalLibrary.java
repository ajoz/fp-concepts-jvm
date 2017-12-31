package io.github.ajoz.fold;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Last thing to do is now to make the foldLeft function more generic and allow it to take any type of arguments.
 */
public class Fold4FunctionalLibrary {
    /**
     * Function that calculates the sum of two values.
     */
    public static BiFunction<Integer, Integer, Integer> SUM_INT_FUNCTION = (a, b) -> a + b;

    /**
     * Function that calculates the product of two values.
     */
    public static BiFunction<Integer, Integer, Integer> PRODUCT_INT_FUNCTION = (a, b) -> a * b;

    /**
     * Calculates the sum of int's stored on a list.
     *
     * @param list Contains the list of ints, might be empty
     * @return result of the sum of integers on the passed list.
     */
    public static Integer sum(final List<Integer> list) {
        return foldLeft(list, 0, SUM_INT_FUNCTION);
    }

    /**
     * Calculates the product of the values of a given integer list.
     *
     * @param list Contains the list of ints, might be empty
     * @return product of the values from the list or 1 if the list is empty.
     */
    public static Integer product(final List<Integer> list) {
        return foldLeft(list, 1, PRODUCT_INT_FUNCTION);
    }

    /**
     * Applies a given binary operator on a given list from the given initial value.
     *
     * @param list    might be empty
     * @param initial Initial value from which the folding operation will begin
     * @param op      Operator that will be applied to each list
     * @return
     */
    public static <A, B> A foldLeft(final List<B> list,
                                    final A initial,
                                    final BiFunction<A, B, A> op) {
        A acc = initial;
        for (final B value : list) {
            acc = op.apply(acc, value);
        }

        return acc;
    }
}
