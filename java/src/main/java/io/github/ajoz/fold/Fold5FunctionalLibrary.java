package io.github.ajoz.fold;

import io.github.ajoz.tuple.Tuple;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import static java.util.Collections.emptyList;

/**
 * Let's play a bit with the foldLeft we created and prepare other functions that may use it. We already know how to
 * write sumBad and product of the given list. Now lets try:
 * <p>
 * 1. count - normally we wouldn't use a fold function to count the number of elements on the list but for practice sake
 * we will.
 * 2. last - finds the last item on the list
 * 3. reverse - returns a list with items with reversed order
 * 4. average - returns average value for the given list of Integers
 * 5. contains - return a boolean value indicating whether a search item is on the given list.
 * 6. join - returns a String created from a list of values joined with a given separator
 * 7. mkString - returns a String created from a list of values joined with a colon
 * 8. penultimate - returns the element next to the last element of the list. Hint: use Tuple
 * 9. unique - returns a list with unique elements from the given list
 * <p>
 * And some additional utility methods to make working with lists easier:
 * 1. head - returns first item of the list
 * 2. tail - returns all items of the list except the head
 * 3. prepend - takes a list and an element and returns a new list with the element as its head
 * 4. append - takes a list and an element and returns a new list with the element at the end of the list
 */
public class Fold5FunctionalLibrary {
    /**
     * Calculates the sum of int's stored on a list.
     *
     * @param list Contains the list of ints, might be empty
     * @return result of the sumBad of integers on the passed list.
     */
    public static Integer sum(final List<Integer> list) {
        return foldLeft(list, 0, (x, y) -> x + y);
    }

    /**
     * Calculates the product of the values of a given integer list.
     *
     * @param list Contains the list of ints, might be empty
     * @return product of the values from the list or 1 if the list is empty.
     */
    public static Integer product(final List<Integer> list) {
        return foldLeft(list, 1, (x, y) -> x * y);
    }

    /**
     * You would usually not use foldLeft to count the items on the list, but this is just a simple example of what
     * can be done with the operator passed. It shows how much value lambda expressions add in terms of visibility. We
     * could simply things with "(count, item) -> { return count + 1; }". Compiler would just have to deduce the types
     * of count and item.
     *
     * @param list list of elements, might be empty
     * @param <A>  type of elements stored on the list
     * @return amount of items stored on the list
     */
    public static <A> Integer count(final List<A> list) {
        return foldLeft(list, 0, (count, item) -> count + 1);
    }

    /**
     * More of an exercise then a real useful method, it shows how to find the last item on the list with the usage
     * of the foldLeft, also shows how java's code can be unreadable for even short things. There is a lot of bloat
     * if we do not use lambda expressions. All this code could be changed to a single line if we had access to
     * "(x, y) -> { return y; }" Its probably even too bloated this way a simple: "(x, y) => y" would be enough?
     *
     * @param list a list of items
     * @param <A>  type of elements stored on the list
     * @return last item of the list
     */
    public static <A> A last(final List<A> list) {
        return foldLeft(list, head(list), (x, y) -> y);
    }

    /**
     * Takes a list and returns a list with items in the reverse order.
     *
     * @param list a list of elements, might be empty
     * @param <A>  type of elements stored on the list
     * @return list with elements from the given list in the reverse order.
     */
    public static <A> List<A> reverse(final List<A> list) {
        return foldLeft(list, Collections.<A>emptyList(), (reversedList, item) -> prepend(item, reversedList));
    }

    /**
     * Another exercise in using the functions we have constructed. Calculates average from the list of integers.
     * Nothing really fancy but shows the power of constructing things from small reusable blocks.
     *
     * @param list a list of integers
     * @return average from the given list
     */
    public static Integer average(final List<Integer> list) {
        return list.isEmpty() ? 0 : sum(list) / list.size();
    }

    /**
     * Searches the given list for a specified item. Issue with this implementation is that it traverses the whole list
     * before it returns the result.
     *
     * @param list     a list of elements, might be empty
     * @param searched searched item
     * @param <A>      type of elements stored on the list
     * @return true if the element is on the list, false otherwise
     */
    public static <A> Boolean contains(final List<A> list,
                                       final A searched) {
        return foldLeft(list, false, (contains, item) -> contains || item.equals(searched));
    }

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
     * Changes all list items to a single comma separated string in the form "List(item1, item2, ..., itemN)". For this
     * method to work properly the stored type needs to implement a {@link Object#toString()} method.
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
     * Returns the item next to the last item on the list.
     *
     * @param list a list of elements, cannot be empty or cannot have only one element
     * @param <A>  type of the items stored on the list
     * @return item next to the last item on the given list.
     * @throws IllegalArgumentException if the given list is empty
     * @throws IllegalArgumentException if the list has only one element
     */
    public static <A> A penultimate(final List<A> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("list is empty");

        if (list.size() == 1)
            throw new IllegalArgumentException("list has only one element");

        return foldLeft(list, Tuple.of(head(list), head(tail(list))), (tuple, a) -> Tuple.of(tuple._2, a))._1;
    }

    /**
     * Prepends the given item at the beginning of the given list and returns a new list.
     *
     * @param list a list of elements, might be empty
     * @param item item that will be prepended at the beginning of the list
     * @param <A>  type of the items stored on the list
     * @return new list with the given element as the head of the list
     */
    public static <A> List<A> prepend(final A item,
                                      final List<A> list) {
        final List<A> newList = new LinkedList<>(); //to construct a new list
        newList.add(item);
        newList.addAll(list);
        return newList;
    }

    /**
     * Appends the given item at the end of the given list.
     *
     * @param list a list of elements, might be empty
     * @param item item that will be appended at the end of the list
     * @param <A>  type of the items stored on the list
     * @return new list with the given element on the end of the list
     */
    public static <A> List<A> append(final List<A> list, final A item) {
        final List<A> newList = new LinkedList<>(list);
        newList.add(item);
        return newList;
    }

    /**
     * Appends list1 to a list2, a new list will be created with list1 element placed at the beginning and elements of
     * list2 placed at the end of the new list.
     *
     * @param list1 a list of elements, might be empty, head of this list will be the new head of the resulting list
     * @param list2 a list of elements, might be empty
     * @param <A>   type of the items stored on the list
     * @return new list
     */
    public static <A> List<A> append(final List<A> list1,
                                     final List<A> list2) {
        final List<A> newList = new LinkedList<>(list1);
        newList.addAll(list2);
        return newList;
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
     * Returns the tail of the given list (a sub list built from each item without the first one - 'head').
     *
     * @param list a list of elements, cannot be empty
     * @param <A>  type of the elements stored on the list
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
     * Applies a given binary operator on a given list from the given initial value. It starts applying this operator
     * from the left side.
     *
     * @param list    list of elements, might be empty
     * @param initial initial value from which the folding operation will begin
     * @param op      operator that will be applied to each element of the list
     * @param <B>     type of elements stored on the list
     * @param <A>     type of result that the foldLeft will reduce the list elements into
     * @return result to which elements of the list were reduced to
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
     * We want to apply a similar operation on the list but from the right side. We want to apply the given operator on
     * the last item and the initial value first and then go through all the item until the first item of the list. The
     * simplest solution would be to just reverse the order of the list and apply a foldLeft operation on it. Or is it
     * the simplest solution? Everything depends on the structure we understand as an ordered list.
     * <p>
     * If the list is a linked list with only one direction. Each element of said list has only information about
     * the next element on the list. So processing from the end of the list will give us additional overhead of finding
     * the last element.
     * <p>
     * This kind of issue doesn't happen for a double linked list.
     *
     * @param list    list of elements, might be empty
     * @param initial initial value from which the folding operation will begin
     * @param op      operator that will be applied to each element of the list
     * @param <A>     type of result that the foldRight will reduce the list elements into
     * @param <B>     type of elements stored on the list
     * @return result to which elements of the list were reduced to
     */
    public static <A, B> A foldRight(final List<B> list,
                                     final A initial,
                                     final BiFunction<B, A, A> op) {
        return foldLeft(reverse(list), initial, (a, b) -> op.apply(b, a));
    }

    /**
     * Takes a list containing lists of type A and flattens them to a single list. All items from the enclosing lists
     * will be placed into a single list in the order they were processed.
     *
     * @param list contains lists of type A, might be empty
     * @param <A>  type of the elements stored on the lists
     * @return single flat list containing all the items
     */
    public static <A> List<A> flatten(final List<List<A>> list) {
        return foldLeft(list, new LinkedList<>(), (BiFunction<List<A>, List<A>, List<A>>) (flat, list1) -> append(flat, list1));
    }
}
