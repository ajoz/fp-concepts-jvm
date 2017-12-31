package io.github.ajoz.fold;

import java.util.List;

/**
 * We will implement now a generic method that allows us to pass a binary operator which will be applied to each item
 * and the accumulated value. This way sum and product can be just a particular implementations of the operator.
 */
public final class Fold3FunctionalLibrary {
    /**
     * Represents a function that accepts two arguments and produces a result.
     */
    public interface BiFunction {
        /**
         * Applies this function to the given arguments.
         *
         * @param x the first function argument
         * @param y the second function argument
         * @return the function result
         */
        Integer apply(Integer x, Integer y);
    }

    /**
     * Function that calculates the sum of two values.
     */
    public static BiFunction SUM_INT_FUNCTION = (x, y) -> x + y;

    /**
     * Function that calculates the product of two values.
     */
    public static BiFunction PRODUCT_INT_FUNCTION = (x, y) -> x * y;

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
     * Applies a given binary operator on a given integer list from the given initial value.
     *
     * @param list    Contains the list of ints, might be empty
     * @param initial Initial value from which the folding operation will begin
     * @param op      Operator that will be applied to each list item
     * @return
     */
    public static Integer foldLeft(final List<Integer> list,
                                   final Integer initial,
                                   final BiFunction op) {
        Integer acc = initial;
        for (final Integer value : list) {
            acc = op.apply(acc, value);
        }

        return acc;
    }
}
