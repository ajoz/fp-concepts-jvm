package io.github.ajoz.fold;

import java.util.List;

/**
 * In this exercise we want to understand and implement fold in pure java. We will begin with a simple example and
 * implement a sum of integers, which in later attempts will be refactored into a full working fold.
 * <p/>
 * We do not worry about passed list being null or if any item on the list could be a null value.
 * <p/>
 * For starters we will implement a sum and product, initial values will be hardcoded. As in Java a {@link List} cannot
 * store primitive value we will use {@link Integer} instead.
 */
public final class Fold1ImperativeLibrary {
    /**
     * Calculates the sum of int's stored on a list.
     *
     * @param list Contains the list of ints, might be empty
     * @return result of the sum of integers on the passed list.
     */
    public static Integer sum(final List<Integer> list) {
        Integer sum = 0;
        for (Integer value : list) {
            sum += value;
        }

        return sum;
    }

    /**
     * Calculates the product of the values of a given integer list.
     *
     * @param list Contains the list of ints, might be empty
     * @return product of the values from the list or 1 if the list is empty.
     */
    public static Integer product(final List<Integer> list) {
        Integer product = 1;
        for (Integer value : list) {
            product *= value;
        }

        return product;
    }
}
