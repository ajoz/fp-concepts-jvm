package io.github.ajoz.fold;

import java.util.List;
import java.util.function.BiFunction;

import static io.github.ajoz.lists.Lists.head;
import static io.github.ajoz.lists.Lists.tail;

/**
 * As another exercise lets implement foldLeft with the usage of recurrence. Additionally we will implement also
 * a foldRight method, which does the same thing as foldLeft but from the other side of the list. Then we will
 * use both of those methods to implement a sum. There shouldn't be any difference in the result.
 */
public class Fold6FunctionalLibrary {
    /**
     * Implementing a recursive foldLeft is pretty straight forward. First lets think about fold left in case of a sum. If
     * we want to sum up elements from the list from 1 to n, we do it like this: x1 + x2 + x3 + ... + xN. Our previous
     * implementation that was using an iteration could be described like this:
     * sum0 = x0 + x1
     * sum1 = sum0 + x2
     * ...
     * sumN = sumN-1 + xN
     * We are adding sum of the previous elements and the next element from the list. We can write it as such:
     * ((((x0 + x1) + x2) + ... ) + xN)
     * We can almost see the recursive nature of this function. Lets replace the "+" operator with a more generic
     * function, our foldLeft will look more like:
     * f(f(f(f(x0, x1), x2), ...), xN)
     * In this form its easy to think about this process as a recursive one. Lets imagine a list with three ints:
     * List(1, 2, 3), our function that we will be using to reduce this list is "+". Lets look how call to foldLeft will
     * look like:
     * list = List(1, 2, 3)
     * foldLeft(list, 0, plusFunc);
     * We remember that we want to apply this:
     * f(f(f(x0, x1), x2), x3) which could be written as plusFunc(plusFunc(plusFunc(0, 1), 2), 3)
     * Lets inline all the arguments in our foldLeft call:
     * foldLeft(List(1, 2, 3), 0, +)
     * next inside we want to call it recursively:
     * - we want to add initial argument and the first item from the list with the usage of passed operator
     * - we want to pass the result as the new initial value for the foldLeft
     * - we want to do the foldLeft operation on the list without its head (first element)
     * <p>
     * so next call to foldLeft should look like:
     * foldLeft(List(2, 3), 0 + 1, +)
     * - List(2, 3) is just tail of the given list: tail(list)
     * - 0 + 1 is a call to the passed operator with initial and first element: plusFunc(initial, head(list))
     * - we just pass again the operator we use for folding
     * <p>
     * We get:
     * foldLeft(tail(list), op.apply(initial, head(list)), op);
     * <p>
     * Simple.
     *
     * @param list    might be empty
     * @param initial Initial value from which the folding operation will begin
     * @param op      Operator that will be applied to each list
     * @param <A>     type of elements stored on the list
     * @param <B>     type of result that the foldLeft will reduce the list elements into
     * @return result to which elements of the list were reduced to
     */
    public static <A, B> B foldLeftRecursive(final List<A> list,
                                             final B initial,
                                             final BiFunction<B, A, B> op) {
        if (list.isEmpty())
            return initial;
        else
            return foldLeftRecursive(tail(list), op.apply(initial, head(list)), op);
    }

    /**
     * Now a different beast, we want to do a foldRight which for sum could be written as: x1 + x2 + ... + xN + x0. Its
     * different as we add the initial x0 to the last element instead of first one, so with the use of brackets it would
     * look like: x1 + (x2 + (... + (xN + x0))). Lets do the same thing as for foldLeft and change "+" operator for a
     * function call: f(x1, f(x2, f(..., f(xN, x0))))
     * Lets inline all the arguments in our foldRight call:
     * foldRight(List(1, 2, 3), 0, +)
     * next inside we want to call it recursively:
     * - we want to add initial argument and the result of the foldRight done on the tail of the list
     * - hmm?
     * <p>
     * So the operation would look like:
     * plusFunc(x1, foldRight(List(2, 3), 0, plusFunc)
     * - List(2, 3) is just tail of the given list: tail(list)
     * - 0 is the initial value
     * - plusFunc is our passed around operator
     * <p>
     * We get:
     * op.apply(head(list), foldRight(tail(list), 0, op))
     * <p>
     * Simple, yet there is a big difference between foldLeft and foldRight. Can we implement foldRight using an
     * iteration? and is a recursive approach a bad one as it involves using the call stack?
     *
     * @param list    might be empty
     * @param initial Initial value from which the folding operation will begin
     * @param op      Operator that will be applied to each list
     * @param <A>     type of elements stored on the list
     * @param <B>     type of result that the foldLeft will reduce the list elements into
     * @return result to which elements of the list were reduced to
     */
    public static <A, B> B foldRightRecursive(final List<A> list,
                                              final B initial,
                                              final BiFunction<B, A, B> op) {
        if (list.isEmpty())
            return initial;
        else
            return op.apply(foldRightRecursive(tail(list), initial, op), head(list));
    }

    /**
     * Calculates the sum of int's stored on a list. It uses a recursive algorithm of foldLeft.
     *
     * @param list Contains the list of ints, might be empty
     * @return result of the sum of integers on the passed list.
     */
    public static Integer sumLeftRecursive(final List<Integer> list) {
        return foldLeftRecursive(list, 0, (x, y) -> x + y);
    }

    /**
     * Calculates the sum of int's stored on a list. It uses a recursive algorithm of foldRight.
     *
     * @param list Contains the list of ints, might be empty
     * @return result of the sum of integers on the passed list.
     */
    public static Integer sumRightRecursive(final List<Integer> list) {
        return foldRightRecursive(list, 0, (x, y) -> x + y);
    }
}
