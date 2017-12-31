package io.github.ajoz.fold;

import java.util.List;
import java.util.function.BiFunction;

import static io.github.ajoz.lists.Lists.head;
import static io.github.ajoz.lists.Lists.tail;

/**
 * We will see if its possible to improve the recursive implementations somehow, or do they have to be necessarily bad.
 */
public class Fold7FunctionalLibrary {
    /**
     * @param list
     * @param initial
     * @param op
     * @param <A>
     * @param <B>
     * @return
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
     * @param list
     * @param initial
     * @param op
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A, B> B foldRightRecursive(final List<A> list,
                                              final B initial,
                                              final BiFunction<B, A, B> op) {
        if (list.isEmpty())
            return initial;
        else
            return op.apply(foldRightRecursive(tail(list), initial, op), head(list));
    }
}
