package io.github.ajoz.lists;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import static java.util.Collections.emptyList;

/**
 * This class consists exclusively of static methods that operate on or return lists.
 */
public class Lists {
    /**
     * Joins all the items in the list into a single string with a given separator.
     *
     * @param list      a list of elements
     * @param separator string used as a separate for the list elements
     * @param <A>       type of the items stored on the list
     * @return List elements joined with a given separator
     */
    public static <A> String join(final List<A> list,
                                  final String separator) {
        if (list.isEmpty())
            return "";
        else
            return foldLeft(tail(list), head(list).toString(), (joined, item) -> joined + separator + item);
    }

    /**
     * Changes all list items to a single comma separated string in the form "List(item1, item2, ..., itemN)".
     * For this method to work properly the stored type needs to implement a {@link Object#toString()} method.
     *
     * @param list a list of elements, might be empty
     * @param <A>  type of the items stored on the list
     * @return List as string in the form of "List(item1, item2, ..., itemN)" or "List()" if the list is empty
     */
    public static <A> String mkString(final List<A> list) {
        if (list.isEmpty())
            return "List()";
        else
            return "List(" + join(list, ", ") + ")";
    }

    /**
     * Returns the first element of the list.
     *
     * @param list a list of elements
     * @param <A>  type of the items stored on the list
     * @return first element of the list
     * @throws NoSuchElementException when the list is empty
     */
    public static <A> A head(final List<A> list) {
        if (list.isEmpty())
            throw new NoSuchElementException("head of empty list");
        return list.get(0);
    }

    /**
     * Returns the tail of the given list (a list build from each item
     * without the first one - 'head').
     *
     * @param list a list of elements
     * @param <A>  type of the items stored on the list
     * @return a sublist without the first element, if the list has only
     * one element and empty list will be returned
     * @throws UnsupportedOperationException when the list is empty
     */
    public static <A> List<A> tail(final List<A> list) {
        if (list.isEmpty())
            throw new UnsupportedOperationException("tail of empty list");
        else if (list.size() == 1)
            return emptyList();
        else
            return list.subList(1, list.size());
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

    /**
     * Takes a list containing lists of type A and flattens them to
     * a single list. All items from the enclosing lists will be placed
     * into a single list in the order they were processed.
     *
     * @param list contains lists of type A, might be empty
     * @param <A>  type stored in the lists
     * @return single flat list containing all the items
     */
    public static <A> List<A> flatten(final List<List<A>> list) {
        return foldLeft(list, new LinkedList<>(), (BiFunction<List<A>, List<A>, List<A>>) (flat, list1) -> {
            flat.addAll(list1);
            return flat;
        });
    }
}
