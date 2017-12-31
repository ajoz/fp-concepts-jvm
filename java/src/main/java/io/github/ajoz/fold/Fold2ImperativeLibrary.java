package io.github.ajoz.fold;

import java.util.List;

/**
 * We will implement now the sum and the product methods with a, possibility to specify the initial value. We do not
 * worry that its possible to pass 0 as the initial product value or if the list has possible null values.
 * <p>
 * Please notice how similar are both implementations of the sum and product methods. Can we change them into one
 * method? Maybe pass the operation somehow into the method?
 */
public final class Fold2ImperativeLibrary {
    /**
     * Calculates the sum of int's stored on a list with a given initial value.
     *
     * @param list    Contains the list of ints, might be empty
     * @param initial the initial value from which the sum needs to be calculated
     * @return result of the sum of integers on the passed list.
     */
    public static Integer sum(final List<Integer> list,
                              final Integer initial) {
        Integer sum = initial; //we can call this variable acc (accumulator)
        for (Integer value : list) {
            sum += value;
        }

        return sum;
    }

    /**
     * Calculates the product of the values of a given integer list with the given initial
     * value.
     *
     * @param list    Contains the list of ints, might be empty
     * @param initial the initial value with which the product needs to be calculateds
     * @return product of the values from the list or 1 if the list is empty.
     */
    public static Integer product(final List<Integer> list,
                                  final Integer initial) {
        Integer product = initial; //the same case as in sum (if we change the
        // name of this var to acc only operator is different)
        for (Integer value : list) {
            product *= value;
        }

        return product;
    }
}
